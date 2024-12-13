package com.decafmango.lab_1.car.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CarDto {
    private Long id;
    private String name;
    private Long userId;
}
