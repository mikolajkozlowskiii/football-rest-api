package com.example.football_api.entities.football;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(	name = "player_stats")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PlayerStat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Match match;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Player player;
    @Length(max = 2)
    private int goals;
    @Length(max = 2)
    private int assits;
    @Length(max = 3)
    private int minutesPlayed;
    @Length(max = 2)
    private int fouls;
    @Length(max = 1)
    private int yellowCards;
    private boolean redCard;
}
