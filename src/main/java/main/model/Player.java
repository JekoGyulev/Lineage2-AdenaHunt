package main.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String nickname;
    @Enumerated(EnumType.STRING)
    @Column(name = "player_class")
    private PlayerClass playerClass;
    @Column(name = "banner_img")
    private String bannerImg;
    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;
    @Column(nullable = false)
    private int level;
    @Column(nullable = false)
    private double health;
    @Column(nullable = false)
    private double attack;
    @Column(nullable = false)
    private double defense;
    @Column(nullable = false)
    private double xp;
    @Column
    private int adena;
    @CreationTimestamp
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

}

