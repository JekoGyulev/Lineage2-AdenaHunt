package main.service;

import main.model.Mob;
import main.model.Player;
import main.property.MobProperties.MobDetails;
import main.web.dto.FightResult;

import java.util.List;
import java.util.UUID;

public interface MobService {
    Mob createMob(MobDetails mobDetails);

    List<Mob> getTop3RecentMobs();

    List<Mob> getAllMobs();

    FightResult attackMob(UUID mobId, Player player);

    Mob getMobById(UUID mobId);
}
