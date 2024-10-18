package com.decafmango.lab_1.coordinates.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CoordinatesDto {
    private Long id;
    private Double x;
    private Long y;
    private Long userId;
}
