package com.example.football_api.entities.football;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(	name = "players")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Team team;
    @Column(length = 30, nullable = false)
    private String firstName;
    @Column(length = 30)
    private String lastName;
    @Column(length = 3)
    private String position;
    private int height;
    private int weight;
    private boolean strongerFeet;
    private Date birthDate;
}
