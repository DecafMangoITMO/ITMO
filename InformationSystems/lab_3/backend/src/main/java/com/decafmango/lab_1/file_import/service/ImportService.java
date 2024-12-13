package com.decafmango.lab_1.file_import.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.decafmango.lab_1.Pagination;
import com.decafmango.lab_1.car.dao.CarRepository;
import com.decafmango.lab_1.car.dto.CreateCarDto;
import com.decafmango.lab_1.car.model.Car;
import com.decafmango.lab_1.config.SocketHandler;
import com.decafmango.lab_1.coordinates.dao.CoordinatesRepository;
import com.decafmango.lab_1.coordinates.dto.CreateCoordinatesDto;
import com.decafmango.lab_1.coordinates.model.Coordinates;
import com.decafmango.lab_1.exception.exceptions.FileNotFoundException;
import com.decafmango.lab_1.exception.exceptions.ForbiddenException;
import com.decafmango.lab_1.exception.exceptions.InvalidImportException;
import com.decafmango.lab_1.file_import.dao.ImportAttemptRepository;
import com.decafmango.lab_1.file_import.dto.ImportObjectDto;
import com.decafmango.lab_1.file_import.dto.ImportResultDto;
import com.decafmango.lab_1.file_import.model.ImportAttempt;
import com.decafmango.lab_1.human_being.dao.HumanBeingRepository;
import com.decafmango.lab_1.human_being.dto.ImportHumanBeingDto;
import com.decafmango.lab_1.human_being.model.HumanBeing;
import com.decafmango.lab_1.security.JwtService;
import com.decafmango.lab_1.user.dao.UserRepository;
import com.decafmango.lab_1.user.dto.UserDto;
import com.decafmango.lab_1.user.model.Role;
import com.decafmango.lab_1.user.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final UserRepository userRepository;
    private final HumanBeingRepository humanBeingRepository;
    private final CarRepository carRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final ImportAttemptRepository importAttemptRepository;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final SocketHandler socketHandler;

    @Value("${minio.bucket}")
    private String bucket;
    private final MinioClient minioClient;

    public Long getImportResultsCount(HttpServletRequest request) {
        User user = findUserByRequest(request);

        if (user.getRole() == Role.USER)
            return importAttemptRepository.countByUser(user);
        else
            return importAttemptRepository.count();
    }

    public Resource getImportFileByName(String filename, HttpServletRequest request) {
        User user = findUserByRequest(request);

        ImportAttempt importAttempt = importAttemptRepository.findByFilename(filename)
            .orElseThrow(() -> new FileNotFoundException(String.format("File with name %s does not exist", filename)));

        if (user.getRole() != Role.ADMIN && !importAttempt.getUser().getId().equals(user.getId()))
            throw new ForbiddenException(String.format("User with id %d does not have an access to file with name %s", user.getId(), filename));
        
        InputStream resource;
        try {
            resource = minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(importAttempt.getFilename())
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException();
        }

        return new InputStreamResource(resource);
    }

    public List<ImportResultDto> getImportResults(Integer from, Integer size, HttpServletRequest request) {
        User user = findUserByRequest(request);
        Pageable page = Pagination.createPageTemplate(from, size);
        List<ImportAttempt> importAttempts = null;

        if (user.getRole() == Role.USER)
            importAttempts = importAttemptRepository.findAllByUser(user, page);
        else
            importAttempts = importAttemptRepository.findAll(page).getContent();

        return importAttempts.stream()
                .map((importAttempt) -> ImportResultDto.builder()
                        .id(importAttempt.getId())
                        .isSuccessfull(importAttempt.getIsSuccessful())
                        .objectCount(importAttempt.getObjectsCount())
                        .filename(importAttempt.getFilename())
                        .user(UserDto.builder()
                                .id(importAttempt.getUser().getId())
                                .username(importAttempt.getUser().getUsername())
                                .role(importAttempt.getUser().getRole())
                                .build())
                        .build())
                .toList();
    }

    public ImportResultDto importFile(MultipartFile file, HttpServletRequest request) {
        User user = findUserByRequest(request);
        String jsonContent;
        List<ImportObjectDto> jsonArray;

        String[] filenameAndType = file.getOriginalFilename().split("\\.");
        if (filenameAndType.length != 2 || !filenameAndType[1].equals("txt"))
            throw new InvalidImportException("Invalid file type (expected .txt)");

        try {
            jsonContent = new String(file.getBytes());
            jsonArray = objectMapper.readValue(jsonContent, new TypeReference<List<ImportObjectDto>>() {
            });
        } catch (IOException e) {
            throw new InvalidImportException("Invalid json");
        }

        List<HumanBeing> humanBeings = new ArrayList<>();
        List<Car> cars = new ArrayList<>();
        List<Coordinates> coordinates = new ArrayList<>();
        Long objectsCount = 0l;

        for (ImportObjectDto importObjectDto : jsonArray) {
            switch (importObjectDto.getType()) {
                case HUMAN_BEING:
                    ImportHumanBeingDto humanBeing = objectMapper.convertValue(importObjectDto.getData(),
                            ImportHumanBeingDto.class);
                    HumanBeing humanBeingToSave = validateHumanBeing(humanBeing, user);
                    if (humanBeing != null) {
                        humanBeings.add(humanBeingToSave);
                        objectsCount += 2;
                        if (humanBeing.getCar() != null)
                            objectsCount += 1;
                        break;
                    }
                    throw new InvalidImportException("Invalid json");
                case CAR:
                    CreateCarDto car = objectMapper.convertValue(importObjectDto.getData(), CreateCarDto.class);
                    Car carToSave = validateCar(car, user);
                    if (car != null) {
                        cars.add(carToSave);
                        objectsCount += 1;
                        break;
                    }
                    throw new InvalidImportException("Invalid json");
                case COORDINATES:
                    CreateCoordinatesDto coordinate = objectMapper.convertValue(importObjectDto.getData(),
                            CreateCoordinatesDto.class);
                    Coordinates coordinatesToSave = validateCoordinates(coordinate, user);
                    if (coordinatesToSave != null) {
                        coordinates.add(coordinatesToSave);
                        objectsCount += 1;
                        break;
                    }
                    throw new InvalidImportException("Invalid json");
            }
        }

        Boolean isSuccessful = true;
        String filename = user.getUsername() + ":" + LocalDateTime.now() + ".txt";
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(filename)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build());
        } catch (Exception e) {
            isSuccessful = false;
        }

        if (isSuccessful) {
            try {
                saveObjects(humanBeings, cars, coordinates);
            } catch (RuntimeException exception) {
                isSuccessful = false;
                try {
                    minioClient.removeObject(
                            RemoveObjectArgs.builder()
                                    .bucket(bucket)
                                    .object(filename)
                                    .build());
                } catch (Exception e) {
                }
            }
        }

        ImportAttempt importAttempt = ImportAttempt.builder()
                .isSuccessful(isSuccessful)
                .objectsCount(isSuccessful ? objectsCount : null)
                .filename(isSuccessful ? filename : null)
                .user(user)
                .build();

        importAttempt = importAttemptRepository.save(importAttempt);
        socketHandler.broadcast("");

        return ImportResultDto.builder()
                .id(importAttempt.getId())
                .isSuccessfull(importAttempt.getIsSuccessful())
                .objectCount(importAttempt.getObjectsCount())
                .filename(importAttempt.getFilename())
                .user(UserDto.builder()
                        .id(importAttempt.getUser().getId())
                        .username(importAttempt.getUser().getUsername())
                        .build())
                .build();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void saveObjects(
            List<HumanBeing> humanBeings,
            List<Car> cars,
            List<Coordinates> coordinates) {
        humanBeingRepository.saveAll(humanBeings);
        carRepository.saveAll(cars);
        coordinatesRepository.saveAll(coordinates);
    }

    private HumanBeing validateHumanBeing(ImportHumanBeingDto humanBeing, User user) {
        if (Strings.isBlank(humanBeing.getName()))
            return null;
        if (validateCoordinates(humanBeing.getCoordinates(), user) == null)
            return null;
        if (humanBeing.getRealHero() == null)
            return null;
        if (humanBeing.getHasToothpick() == null)
            return null;
        if (validateCar(humanBeing.getCar(), user) == null)
            return null;
        if (humanBeing.getMood() == null)
            return null;
        if (humanBeing.getImpactSpeed() == null)
            return null;
        if (humanBeing.getMinutesOfWaiting() == null)
            return null;

        return HumanBeing.builder()
                .name(humanBeing.getName())
                .coordinates(
                        Coordinates.builder()
                                .x(humanBeing.getCoordinates().getX())
                                .y(humanBeing.getCoordinates().getY())
                                .user(user)
                                .build())
                .creationDate(LocalDateTime.now())
                .realHero(humanBeing.getRealHero())
                .hasToothpick(humanBeing.getHasToothpick())
                .car(Car.builder()
                        .name(humanBeing.getCar().getName())
                        .user(user)
                        .build())
                .mood(humanBeing.getMood())
                .impactSpeed(humanBeing.getImpactSpeed())
                .minutesOfWaiting(humanBeing.getMinutesOfWaiting())
                .weaponType(humanBeing.getWeaponType())
                .user(user)
                .build();
    }

    private Car validateCar(CreateCarDto car, User user) {

        if (Strings.isBlank(car.getName()))
            return null;

        return Car.builder()
                .name(car.getName())
                .user(user)
                .build();
    }

    private Coordinates validateCoordinates(CreateCoordinatesDto coordinates, User user) {

        if (coordinates.getX() == null || coordinates.getX() > 647)
            return null;
        if (coordinates.getY() == null || coordinates.getY() > 123)
            return null;

        return Coordinates.builder()
                .x(coordinates.getX())
                .y(coordinates.getY())
                .user(user)
                .build();
    }

    private User findUserByRequest(HttpServletRequest request) {
        String username = jwtService.extractUsername(jwtService.extractJwtToken(request));
        return userRepository.findByUsername(username).get();
    }

}
