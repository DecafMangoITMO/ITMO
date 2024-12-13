package com.decafmango.lab_1.car.dao;

import com.decafmango.lab_1.car.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    boolean existsCarByName(String name);

    Car findByName(String name);
}
