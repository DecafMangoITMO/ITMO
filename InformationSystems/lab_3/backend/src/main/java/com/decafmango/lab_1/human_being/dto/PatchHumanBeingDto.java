package com.decafmango.lab_1.human_being.dto;

import com.decafmango.lab_1.human_being.model.Mood;
import com.decafmango.lab_1.human_being.model.WeaponType;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatchHumanBeingDto {

    @Size(min = 1)
    private String name;

    @Positive
    private Long coordinatesId;

    private Boolean realHero;

    private Boolean hasToothpick;

    @Positive
    private Long carId;

    private Mood mood;

    private Double impactSpeed;

    private Float minutesOfWaiting;

    private WeaponType weaponType;

}
