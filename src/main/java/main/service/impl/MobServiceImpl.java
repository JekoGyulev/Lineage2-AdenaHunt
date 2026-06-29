package main.service.impl;

import main.model.Mob;
import main.model.MobType;
import main.repository.MobRepository;
import main.property.MobProperties.MobDetails;
import main.service.MobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
public class MobServiceImpl implements MobService {

    private final MobRepository mobRepository;


    @Autowired
    public MobServiceImpl(MobRepository mobRepository) {
        this.mobRepository = mobRepository;
    }


    @Override
    public Mob createMob(MobDetails mobDetails) {

        Random random = new Random();

        int randomLevel = random
                .nextInt(mobDetails.getLevelRange()[0], mobDetails.getLevelRange()[1] + 1);


        int randomAdenaDrop = random
                .nextInt(mobDetails.getAdenaDrop()[0],  mobDetails.getAdenaDrop()[1] + 1);

        int randomXpDrop = random
                .nextInt(mobDetails.getXpDrop()[0], mobDetails.getXpDrop()[1] + 1);

        int randomMobTypeIndex = random.nextInt(0, MobType.values().length);

        MobType randomMobType = MobType.values()[randomMobTypeIndex];


        double statMultiplier = 1.00;
        int dropMultiplier = 1;

        switch (randomMobType) {
            case BLUE_CHAMPION:
                statMultiplier = 1.05;
                dropMultiplier = 2;
                break;

            case RED_CHAMPION:
                statMultiplier = 1.10;
                dropMultiplier = 3;
                break;
        }


        Mob mob = Mob.builder()
                .name(mobDetails.getName())
                .level(randomLevel)
                .health(mobDetails.getHealthFactor() * randomLevel * statMultiplier)
                .attack(mobDetails.getAttackFactor() * randomLevel * statMultiplier)
                .defense(mobDetails.getDefenseFactor() * randomLevel * statMultiplier)
                .isAlive(true)
                .imageUrl(mobDetails.getImage())
                .description(mobDetails.getDescription())
                .spawnArea(mobDetails.getSpawnArea())
                .adenaDrop(randomAdenaDrop * dropMultiplier)
                .xpDrop(randomXpDrop * dropMultiplier)
                .type(randomMobType)
                .build();


        this.mobRepository.save(mob);

        return mob;
    }

    @Override
    public List<Mob> getTop3RecentMobs() {
        return this.mobRepository.findTop3OrderByCreatedOnDesc();
    }
}
