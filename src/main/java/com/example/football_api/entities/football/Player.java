package com.example.football_api.entities.football;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@Table(	name = "players")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class  Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy = "player",
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    private Set<PlayerTeamHistory> teams;
    @Column(length = 30)
    private String firstName;
    @Column(length = 30, nullable = false)
    private String lastName;
    @Column(length = 3)
    private String position;
    private int height;
    private int weight;
    private boolean strongerFeet;
    private LocalDate birthDate;
}
