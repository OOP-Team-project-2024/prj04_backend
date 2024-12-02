package com.oop.puangJumJum.repository;

import com.oop.puangJumJum.entity.Place;
import com.oop.puangJumJum.entity.PlaceChoice;
import com.oop.puangJumJum.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByUserAndDateAfter(User user, LocalDateTime date);

    Optional<Place> findByUserAndDate(User user, LocalDate date);

    List<Place> findByPlaceChoice(PlaceChoice placeChoice);
}
