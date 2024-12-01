package com.oop.puangJumJum.repository;

import com.oop.puangJumJum.entity.Menu;
import com.oop.puangJumJum.entity.MenuChoice;
import com.oop.puangJumJum.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Optional<Menu> findByUserAndDate(User user, LocalDate date);

    List<Menu> findByMenuChoice(MenuChoice menuChoice);
}
