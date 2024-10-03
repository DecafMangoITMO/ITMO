package com.decafmango.lab_1.car.service;

import com.decafmango.lab_1.Pagination;
import com.decafmango.lab_1.car.dao.CarRepository;
import com.decafmango.lab_1.car.dto.CarDto;
import com.decafmango.lab_1.car.dto.CreateCarDto;
import com.decafmango.lab_1.car.dto.PatchCarDto;
import com.decafmango.lab_1.car.model.Car;
import com.decafmango.lab_1.exception.exceptions.CarAlreadyExistsException;
import com.decafmango.lab_1.exception.exceptions.CarNotFoundException;
import com.decafmango.lab_1.exception.exceptions.ForbiddenException;
import com.decafmango.lab_1.human_being.dao.HumanBeingRepository;
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

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final HumanBeingRepository humanBeingRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public List<CarDto> getCars(int from, int size) {
        Pageable page = Pagination.createPageTemplate(from, size);
        List<Car> cars = carRepository.findAll(page).getContent();
        return cars
                .stream()
                .map(car -> new CarDto(car.getId(), car.getName(), car.getUser().getId()))
                .sorted(new Comparator<CarDto>() {
                    @Override
                    public int compare(CarDto o1, CarDto o2) {
                        return o1.getId().compareTo(o2.getId());
                    }
                })
                .toList();
    }

    public CarDto createCar(CreateCarDto createCarDto, HttpServletRequest request) {
        if (carRepository.existsCarByName(createCarDto.getName()))
            throw new CarAlreadyExistsException(
                    String.format("Car with name %s already exists", createCarDto.getName())
            );

        User user = findUserByRequest(request);

        Car car = Car
                .builder()
                .name(createCarDto.getName())
                .user(user)
                .build();

        car = carRepository.save(car);
        simpMessagingTemplate.convertAndSend("/topic", "New car added");

        return new CarDto(
                car.getId(),
                car.getName(),
                user.getId()
        );
    }

    public CarDto patchCar(Long carId, PatchCarDto patchCarDto, HttpServletRequest request) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() ->
                        new CarNotFoundException(String.format("Car with id %s not found", carId))
                );

        if (!checkPermission(car, request))
            throw new ForbiddenException(String.format("No access to car with id %d", carId));

        car.setName(patchCarDto.getName());
        car = carRepository.save(car);
        simpMessagingTemplate.convertAndSend("/topic", "Car updated");

        return new CarDto(
                car.getId(),
                car.getName(),
                car.getUser().getId()
        );
    }

    public void deleteCar(Long carId, HttpServletRequest request) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(String.format("Car with id %s not found", carId)));

        if (!checkPermission(car, request))
            throw new ForbiddenException(String.format("No access to car with id %d", carId));

        List<HumanBeing> humanBeingsWithThisCar = humanBeingRepository.findAllByCar(car);
        humanBeingRepository.deleteAll(humanBeingsWithThisCar);

        carRepository.deleteById(carId);
        simpMessagingTemplate.convertAndSend("/topic", "Car deleted");
    }

    private User findUserByRequest(HttpServletRequest request) {
        String username = jwtService.extractUsername(jwtService.extractJwtToken(request));
        return userRepository.findByUsername(username).get();
    }

    private boolean checkPermission(Car car, HttpServletRequest request) {
        String username = jwtService.extractUsername(jwtService.extractJwtToken(request));
        User fromUser = userRepository.findByUsername(username).get();
        return car.getUser().getUsername().equals(username) || fromUser.getRole() == Role.ADMIN;
    }

}
