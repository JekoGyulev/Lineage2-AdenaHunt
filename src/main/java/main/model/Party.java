package main.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "parties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "leader_id", nullable = false)
    private Player leader;
    @CreationTimestamp
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private LocalDateTime updatedOn;
}
