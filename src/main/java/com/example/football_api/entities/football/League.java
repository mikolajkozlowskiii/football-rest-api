package com.example.football_api.entities.football;

import com.example.football_api.entities.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@NamedEntityGraph(name = "league-teams-graph",
        attributeNodes = @NamedAttributeNode("teams"))
@Entity
@Table(
        name = "leagues",
        uniqueConstraints = {@UniqueConstraint(
                        name = "unique_league_season_country_name",
                        columnNames = {"country", "name", "season"})
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class League {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 30)
    @Size(min = 3, max = 30)
    private String name;
    @Column(length = 7)
    @Size(min = 7, max = 7)
    private String season;
    @Column(length = 3)
    @Size(min = 3, max = 3)
    private String country;
    @ManyToMany( fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "leagues_teams",
            joinColumns = @JoinColumn(name = "league_id", foreignKey = @ForeignKey(name ="FK_leaguesteams_leagues")),
            inverseJoinColumns = @JoinColumn(name = "team_id", foreignKey = @ForeignKey(name ="FK_leaguesteams_teams"))
    )
    private Set<Team> teams;
}
