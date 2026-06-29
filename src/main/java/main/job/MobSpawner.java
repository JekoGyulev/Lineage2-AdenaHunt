package main.job;


import lombok.extern.slf4j.Slf4j;
import main.model.Mob;
import main.property.MobProperties;
import main.service.MobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MobSpawner {

    private final MobService mobService;
    private final MobProperties mobProperties;


    @Autowired
    public MobSpawner(MobService mobService, MobProperties mobProperties) {
        this.mobService = mobService;
        this.mobProperties = mobProperties;
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void spawnMobs() {
        this.mobProperties.getMobs()
                .forEach( mobDetails -> {
                    Mob createdMob = this.mobService.createMob(mobDetails);

                    log.info("Mob {} {} was spawned.", createdMob.getName(), createdMob.getLevel());
                });
    }








}
