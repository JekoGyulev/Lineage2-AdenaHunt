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
@Table(name = "mobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mob {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int level;
    @Column(nullable = false)
    private double health;
    @Column(nullable = false)
    private double attack;
    @Column(nullable = false)
    private double defense;
    @Column(name = "is_alive",nullable = false)
    private boolean isAlive;
    @Column(name = "adena_drop")
    private int adenaDrop;
    @Column(name = "xp_drop")
    private int xpDrop;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MobType type;
    @Column(name = "image_url", nullable = false)
    private String imageUrl;
    @Column(name = "spawn_area", nullable = false)
    private String spawnArea;
    @Column(nullable = false)
    private String description;
    @CreationTimestamp
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private LocalDateTime updatedOn;
}

