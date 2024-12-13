package com.decafmango.lab_1.coordinates.dao;

import com.decafmango.lab_1.coordinates.model.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordinatesRepository extends JpaRepository<Coordinates, Long> {
    boolean existsCoordinatesByXAndY(Double x, Long y);
}
