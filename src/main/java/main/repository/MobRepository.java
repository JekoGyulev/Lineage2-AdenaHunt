package main.repository;

import main.model.Mob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface MobRepository extends JpaRepository<Mob, UUID> {


    @Query(value = """
                    SELECT * FROM mobs m 
                            ORDER BY m.created_on DESC 
                            LIMIT 3
                    """, nativeQuery = true)
    List<Mob> findTop3OrderByCreatedOnDesc();


    List<Mob> findAllByOrderByCreatedOnDescHealthAscAttackAscDefenseAsc();
}
