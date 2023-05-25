package com.example.football_api.entities.football;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;


@Entity
@Table(	name = "goals")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Check(constraints = "time>=0")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    //@Column(nullable = false)
    private Match match;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
   // @Column(nullable = false)
    private Player player;
    @Column(nullable = false)
    private boolean isOwn;
    @Column(nullable = false, length = 3)
    private int time;

    @PrePersist
    public void onPrePersist(){
        // TODO if constraint doesnt work
    }
}
