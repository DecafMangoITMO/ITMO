package com.decafmango.lab_1.human_being.service;

import com.decafmango.lab_1.Pagination;
import com.decafmango.lab_1.car.dao.CarRepository;
import com.decafmango.lab_1.car.dto.CarDto;
import com.decafmango.lab_1.car.model.Car;
import com.decafmango.lab_1.coordinates.dao.CoordinatesRepository;
import com.decafmango.lab_1.coordinates.dto.CoordinatesDto;
import com.decafmango.lab_1.coordinates.model.Coordinates;
import com.decafmango.lab_1.exception.exceptions.CarNotFoundException;
import com.decafmango.lab_1.exception.exceptions.CoordinatesNotFoundException;
import com.decafmango.lab_1.exception.exceptions.ForbiddenException;
import com.decafmango.lab_1.exception.exceptions.HumanBeingNotFoundException;
import com.decafmango.lab_1.human_being.dao.HumanBeingRepository;
import com.decafmango.lab_1.human_being.dto.CreateHumanBeingDto;
import com.decafmango.lab_1.human_being.dto.HumanBeingDto;
import com.decafmango.lab_1.human_being.dto.PatchHumanBeingDto;
import com.decafmango.lab_1.human_being.model.HumanBeing;
import com.decafmango.lab_1.security.JwtService;
import com.decafmango.lab_1.user.dao.UserRepository;
import com.decafmango.lab_1.user.model.Role;
import com.decafmango.lab_1.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class HumanBeingService {

    private final HumanBeingRepository humanBeingRepository;
    private final CarRepository carRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public List<HumanBeingDto> getHumanBeings(int from, int size) {
        Pageable page = Pagination.createPageTemplate(from, size);
        List<HumanBeing> humanBeings = humanBeingRepository.findAll(page).getContent();
        return humanBeings
                .stream()
                .map(this::toHumanBeingDto)
                .sorted(new Comparator<HumanBeingDto>() {
                    @Override
                    public int compare(HumanBeingDto o1, HumanBeingDto o2) {
                        return o1.getId().compareTo(o2.getId());
                    }
                })
                .toList();
    }

    public HumanBeingDto getRandomHumanBeingWithMaxMinutesOfWaiting() {
        List<HumanBeing> variants = humanBeingRepository.findAllWithMaxMinutesOfWaiting();
        if (variants.isEmpty())
            throw new HumanBeingNotFoundException("There are not variants of human being with max minutes of waiting");
        Random random = new Random();
        HumanBeing randomHumanBeing = variants.get(random.nextInt(variants.size()));
        return toHumanBeingDto(randomHumanBeing);
    }

    public List<HumanBeingDto> getHumanBeingsWithMinutesOfWaitingLess(Float minutesOfWaiting) {
        List<HumanBeing> variants = humanBeingRepository.findByMinutesOfWaitingLessThan(minutesOfWaiting);
        return variants
                .stream()
                .map(this::toHumanBeingDto)
                .sorted(new Comparator<HumanBeingDto>() {
                    @Override
                    public int compare(HumanBeingDto o1, HumanBeingDto o2) {
                        return o1.getId().compareTo(o2.getId());
                    }
                })
                .toList();
    }

    public HumanBeingDto createHumanBeing(CreateHumanBeingDto createHumanBeingDto,
                                          HttpServletRequest request) {

        Car car = null;
        if (createHumanBeingDto.getCarId() != null)
            carRepository.findById(createHumanBeingDto.getCarId())
                    .orElseThrow(() ->
                            new CarNotFoundException(
                                    String.format("Car with id %s not found", createHumanBeingDto.getCarId())
                            )
                    );

        Coordinates coordinates = coordinatesRepository.findById(createHumanBeingDto.getCoordinatesId())
                .orElseThrow(() -> new CoordinatesNotFoundException(
                                String.format("Coordinates with id %s not found", createHumanBeingDto.getCoordinatesId())
                        )
                );

        User user = findUserByRequest(request);

        HumanBeing humanBeing = HumanBeing
                .builder()
                .name(createHumanBeingDto.getName())
                .coordinates(coordinates)
                .creationDate(LocalDateTime.now())
                .realHero(createHumanBeingDto.getRealHero())
                .hasToothpick(createHumanBeingDto.getHasToothpick())
                .car(car)
                .mood(createHumanBeingDto.getMood())
                .impactSpeed(createHumanBeingDto.getImpactSpeed())
                .minutesOfWaiting(createHumanBeingDto.getMinutesOfWaiting())
                .weaponType(createHumanBeingDto.getWeaponType())
                .user(user)
                .build();

        humanBeing = humanBeingRepository.save(humanBeing);
        simpMessagingTemplate.convertAndSend("/topic", "New humanBeing added");

        return toHumanBeingDto(humanBeing);
    }

    public HumanBeingDto patchHumanBeing(Long humanBeingId, PatchHumanBeingDto patchHumanBeingDto,
                                         HttpServletRequest request) {
        HumanBeing humanBeing = humanBeingRepository.findById(humanBeingId)
                .orElseThrow(() -> new HumanBeingNotFoundException(
                        String.format("HumanBeing with id %s not found", humanBeingId)
                ));

        if (!checkPermission(humanBeing, request))
            throw new ForbiddenException(String.format("No access to humanBeing with id %s", humanBeingId));

        if (patchHumanBeingDto.getName() != null)
            humanBeing.setName(patchHumanBeingDto.getName());
        if (patchHumanBeingDto.getCoordinatesId() != null) {
            Coordinates coordinates = coordinatesRepository.findById(patchHumanBeingDto.getCoordinatesId())
                    .orElseThrow(() -> new CoordinatesNotFoundException(
                            String.format("Coordinates with id %s not found", patchHumanBeingDto.getCoordinatesId())
                    ));
            humanBeing.setCoordinates(coordinates);
        }
        if (patchHumanBeingDto.getRealHero() != null)
            humanBeing.setRealHero(patchHumanBeingDto.getRealHero());
        if (patchHumanBeingDto.getHasToothpick() != null)
            humanBeing.setHasToothpick(patchHumanBeingDto.getHasToothpick());
        if (patchHumanBeingDto.getCarId() != null) {
            Car car = carRepository.findById(patchHumanBeingDto.getCarId())
                    .orElseThrow(() -> new CarNotFoundException(
                            String.format("Car with id %s not found", patchHumanBeingDto.getCarId())
                    ));
            humanBeing.setCar(car);
        } else
            humanBeing.setCar(null);
        if (patchHumanBeingDto.getMood() != null)
            humanBeing.setMood(patchHumanBeingDto.getMood());
        if (patchHumanBeingDto.getImpactSpeed() != null)
            humanBeing.setImpactSpeed(patchHumanBeingDto.getImpactSpeed());
        if (patchHumanBeingDto.getMinutesOfWaiting() != null)
            humanBeing.setMinutesOfWaiting(patchHumanBeingDto.getMinutesOfWaiting());
        else
            humanBeing.setMinutesOfWaiting(null);
        if (patchHumanBeingDto.getWeaponType() != null)
            humanBeing.setWeaponType(patchHumanBeingDto.getWeaponType());

        humanBeing = humanBeingRepository.save(humanBeing);
        simpMessagingTemplate.convertAndSend("/topic", "HumanBeing updated");

        return toHumanBeingDto(humanBeing);
    }

    public void giveLadaKalina(HttpServletRequest request) {
        User user = findUserByRequest(request);

        if (!carRepository.existsCarByName("Red Lada Kalina"))
            carRepository.save(new Car(null, "Red Lada Kalina", user));


        Car ladaKalina = carRepository.findByName("Red Lada Kalina");

        List<HumanBeing> humanBeings = null;

        if (user.getRole() == Role.USER) {
            humanBeings = humanBeingRepository.findAllByUser(user);
        } else {
            humanBeings = humanBeingRepository.findAll();
        }

        humanBeings
                .stream()
                .filter(humanBeing -> humanBeing.getCar() == null)
                .forEach(humanBeingWithoutCar -> humanBeingWithoutCar.setCar(ladaKalina));
        humanBeingRepository.saveAll(humanBeings);
        simpMessagingTemplate.convertAndSend("/topic", "HumanBeings without car got lada kalina");
    }

    public void deleteHumanBeing(Long humanBeingId, HttpServletRequest request) {
        if (!humanBeingRepository.existsById(humanBeingId))
            throw new HumanBeingNotFoundException(String.format("HumanBeing with id %s not found", humanBeingId));

        HumanBeing humanBeing = humanBeingRepository.findById(humanBeingId)
                .orElseThrow(() ->
                        new HumanBeingNotFoundException(String.format("HumanBeing with id %s not found", humanBeingId))
                );

        if (!checkPermission(humanBeing, request))
            throw new ForbiddenException(String.format("No access to humanBeing with id %s", humanBeingId));

        humanBeingRepository.deleteById(humanBeingId);
        simpMessagingTemplate.convertAndSend("/topic", "HumanBeing deleted");
    }

    @Transactional
    public void deleteHumanBeingsByImpactSpeed(Double impactSpeed, HttpServletRequest request) {
        User user = findUserByRequest(request);

        if (user.getRole() == Role.USER) {
            humanBeingRepository.removeAllByUserAndImpactSpeed(user, impactSpeed);
        } else {
            humanBeingRepository.removeAllByImpactSpeed(impactSpeed);
        }
        simpMessagingTemplate.convertAndSend("/topic", "HumanBeings deleted by impact speed");
    }

    @Transactional
    public void deleteHumanBeingsWithToothpick(HttpServletRequest request) {
        User user = findUserByRequest(request);

        if (user.getRole() == Role.USER) {
            humanBeingRepository.removeAllByUserAndHasToothpickIsFalse(user);
        } else {
            humanBeingRepository.removeAllByHasToothpickIsFalse();
        }
        simpMessagingTemplate.convertAndSend("/topic", "HumanBeings deleted by has toothpick");
    }

    private HumanBeingDto toHumanBeingDto(HumanBeing humanBeing) {
        return HumanBeingDto
                .builder()
                .id(humanBeing.getId())
                .name(humanBeing.getName())
                .coordinates(new CoordinatesDto(
                        humanBeing.getCoordinates().getId(),
                        humanBeing.getCoordinates().getX(),
                        humanBeing.getCoordinates().getY(),
                        humanBeing.getCoordinates().getUser().getId()
                )).creationDate(humanBeing.getCreationDate())
                .realHero(humanBeing.getRealHero())
                .hasToothpick(humanBeing.getHasToothpick())
                .car(humanBeing.getCar() != null ?
                        new CarDto(
                                humanBeing.getCar().getId(),
                                humanBeing.getCar().getName(),
                                humanBeing.getCar().getUser().getId()
                        ) : null
                ).mood(humanBeing.getMood())
                .impactSpeed(humanBeing.getImpactSpeed())
                .minutesOfWaiting(humanBeing.getMinutesOfWaiting())
                .weaponType(humanBeing.getWeaponType())
                .userId(humanBeing.getUser().getId())
                .build();
    }

    private User findUserByRequest(HttpServletRequest request) {
        String username = jwtService.extractUsername(jwtService.extractJwtToken(request));
        return userRepository.findByUsername(username).get();
    }

    private boolean checkPermission(HumanBeing humanBeing, HttpServletRequest request) {
        String username = jwtService.extractUsername(jwtService.extractJwtToken(request));
        User fromUser = userRepository.findByUsername(username).get();
        return humanBeing.getUser().getUsername().equals(username) || fromUser.getRole() == Role.ADMIN;
    }

}
