package com.decafmango.lab_1.human_being.dao;

import com.decafmango.lab_1.car.model.Car;
import com.decafmango.lab_1.coordinates.model.Coordinates;
import com.decafmango.lab_1.human_being.model.HumanBeing;
import com.decafmango.lab_1.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HumanBeingRepository extends JpaRepository<HumanBeing, Long> {

    List<HumanBeing> findAllByUser(User user);

    List<HumanBeing> findAllByCoordinates(Coordinates coordinates);

    List<HumanBeing> findAllByCar(Car car);

    @Query("SELECT h FROM HumanBeing AS h WHERE h.minutesOfWaiting = (SELECT MAX(h1.minutesOfWaiting) FROM HumanBeing AS h1)")
    List<HumanBeing> findAllWithMaxMinutesOfWaiting();

    List<HumanBeing> findByMinutesOfWaitingLessThan(Float minutesOfWaiting);

    void removeAllByImpactSpeed(Double impactSpeed);

    void removeAllByUserAndImpactSpeed(User user, Double impactSpeed);

    void removeAllByHasToothpickIsFalse();

    void removeAllByUserAndHasToothpickIsFalse(User user);

}
