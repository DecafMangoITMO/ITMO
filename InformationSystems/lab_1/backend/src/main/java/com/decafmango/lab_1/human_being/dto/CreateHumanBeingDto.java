package com.decafmango.lab_1.human_being.dto;

import com.decafmango.lab_1.human_being.model.Mood;
import com.decafmango.lab_1.human_being.model.WeaponType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateHumanBeingDto {

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private Long coordinatesId;

    @NotNull
    private Boolean realHero;

    @NotNull
    private Boolean hasToothpick;

    private Long carId;

    @NotNull
    private Mood mood;

    @NotNull
    private Double impactSpeed;

    private Float minutesOfWaiting;

    private WeaponType weaponType;

}
