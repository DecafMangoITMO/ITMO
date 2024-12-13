package com.decafmango.lab_1.human_being.dto;

import com.decafmango.lab_1.car.dto.CreateCarDto;
import com.decafmango.lab_1.coordinates.dto.CreateCoordinatesDto;
import com.decafmango.lab_1.human_being.model.Mood;
import com.decafmango.lab_1.human_being.model.WeaponType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class ImportHumanBeingDto {
    @NotBlank
    private String name;

    @NotNull
    private CreateCoordinatesDto coordinates;

    @NotNull
    private Boolean realHero;

    @NotNull
    private Boolean hasToothpick;

    private CreateCarDto car;

    @NotNull
    private Mood mood;

    @NotNull
    private Double impactSpeed;

    private Float minutesOfWaiting;

    private WeaponType weaponType;
}
