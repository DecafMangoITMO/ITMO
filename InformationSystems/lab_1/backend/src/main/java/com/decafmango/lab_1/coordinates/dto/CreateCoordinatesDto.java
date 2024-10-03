package com.decafmango.lab_1.coordinates.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateCoordinatesDto {

    @NotNull
    @Max(value = 647)
    private Double x;

    @NotNull
    @Max(value = 123)
    private Long y;

}
