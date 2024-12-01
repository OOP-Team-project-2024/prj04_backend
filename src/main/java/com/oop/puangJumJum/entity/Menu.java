package com.oop.puangJumJum.entity;

import jakarta.persistence.*;

@Entity
public class Menu extends BaseUserDate{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private MenuChoice menuChoice;
}
