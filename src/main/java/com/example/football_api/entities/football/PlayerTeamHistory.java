package com.example.football_api.entities.football;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(	name = "players_history")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "player")
public class PlayerTeamHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "playerId")
    private Player player;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    private Team team;
    @Column(nullable = false)
    private LocalDate start;
    @Column(nullable = true)
    private LocalDate ends;

}
