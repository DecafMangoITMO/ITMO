package com.decafmango.lab_1.car.controller;

import com.decafmango.lab_1.car.dto.CarDto;
import com.decafmango.lab_1.car.dto.CreateCarDto;
import com.decafmango.lab_1.car.dto.PatchCarDto;
import com.decafmango.lab_1.car.service.CarService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping("/count")
    public Long getCarsCount() {
        return carService.getCarsCount();
    }

    @GetMapping
    public List<CarDto> getCars(@RequestParam int from, @RequestParam int size) {
        return carService.getCars(from, size);
    }

    @PostMapping
    public CarDto createCar(@RequestBody @Valid CreateCarDto createCarDto, HttpServletRequest request) {
        return carService.createCar(createCarDto, request);
    }

    @PatchMapping("/{carId}")
    public CarDto patchCar(@PathVariable Long carId, @RequestBody @Valid PatchCarDto patchCarDto,
                           HttpServletRequest request) {
        return carService.patchCar(carId, patchCarDto, request);
    }

    @DeleteMapping("/{carId}")
    public void deleteCar(@PathVariable Long carId, HttpServletRequest request) {
        carService.deleteCar(carId, request);
    }

}
