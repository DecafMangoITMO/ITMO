package com.decafmango.lab_1.human_being.dto;

import com.decafmango.lab_1.car.dto.CarDto;
import com.decafmango.lab_1.coordinates.dto.CoordinatesDto;
import com.decafmango.lab_1.human_being.model.Mood;
import com.decafmango.lab_1.human_being.model.WeaponType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class HumanBeingDto {

    private Long id;

    private String name;

    private CoordinatesDto coordinates;

    private LocalDateTime creationDate;

    private Boolean realHero;

    private Boolean hasToothpick;

    private CarDto car;

    private Mood mood;

    private Double impactSpeed;

    private Float minutesOfWaiting;

    private WeaponType weaponType;

    private Long userId;
}
