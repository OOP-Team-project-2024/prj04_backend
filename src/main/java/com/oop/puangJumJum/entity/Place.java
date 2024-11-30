package com.oop.puangJumJum.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Place extends BaseUserDate{
    @ManyToOne
    @JoinColumn(name = "place_id")
    private PlaceChoice placeChoice;
}
