package com.decafmango.lab_1.coordinates.dto;

import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatchCoordinatesDto {
    @Max(value = 647)
    private Double x;
    @Max(value = 123)
    private Long y;
}
