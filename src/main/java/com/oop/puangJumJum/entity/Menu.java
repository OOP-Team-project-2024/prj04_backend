package com.oop.puangJumJum.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Menu extends BaseUserDate{
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private MenuChoice menuChoice;
}
