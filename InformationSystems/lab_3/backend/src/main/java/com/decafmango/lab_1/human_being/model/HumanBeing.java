package com.decafmango.lab_1.human_being.model;

import com.decafmango.lab_1.car.model.Car;
import com.decafmango.lab_1.coordinates.model.Coordinates;
import com.decafmango.lab_1.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import org.eclipse.persistence.annotations.CascadeOnDelete;

@Entity
@Table(name = "human_beings")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class HumanBeing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "coordinates_id", nullable = false)
    @CascadeOnDelete
    private Coordinates coordinates;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "real_hero", nullable = false)
    private Boolean realHero;

    @Column(name = "has_toothpick", nullable = false)
    private Boolean hasToothpick;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    @CascadeOnDelete
    private Car car;

    @Column(name = "mood", nullable = false)
    @Enumerated(EnumType.STRING)
    private Mood mood;

    @Column(name = "impact_speed", nullable = false)
    private Double impactSpeed;

    @Column(name = "minutes_of_waiting")
    private Float minutesOfWaiting;

    @Column(name = "weapon_type")
    @Enumerated(EnumType.STRING)
    private WeaponType weaponType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}
