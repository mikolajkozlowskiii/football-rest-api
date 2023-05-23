package com.example.football_api.entities.football;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(	name = "goals")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Match match;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Player player;
    private boolean isOwn;
}
