package main.service;

import main.model.Mob;
import main.property.MobProperties.MobDetails;

public interface MobService {
    Mob createMob(MobDetails mobDetails);
}
