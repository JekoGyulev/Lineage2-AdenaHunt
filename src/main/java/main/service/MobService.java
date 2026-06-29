package main.service;

import main.model.Mob;
import main.property.MobProperties.MobDetails;

import java.util.List;

public interface MobService {
    Mob createMob(MobDetails mobDetails);

    List<Mob> getTop3RecentMobs();

}
