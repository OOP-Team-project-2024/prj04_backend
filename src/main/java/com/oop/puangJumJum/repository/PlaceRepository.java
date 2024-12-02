package com.oop.puangJumJum.repository;

import com.oop.puangJumJum.entity.Place;
import com.oop.puangJumJum.entity.PlaceChoice;
import com.oop.puangJumJum.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    @Query("SELECT p FROM Place p WHERE p.user = :user AND FUNCTION('DATE', p.date) = :date")
    Optional<Place> findByUserAndDateAfter(@Param("user") User user, @Param("date") LocalDate date);

    Optional<Place> findByUserAndDate(User user, LocalDate date);

    List<Place> findByPlaceChoice(PlaceChoice placeChoice);
}
