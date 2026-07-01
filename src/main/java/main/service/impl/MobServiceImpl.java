package main.service.impl;

import lombok.extern.slf4j.Slf4j;
import main.model.*;
import main.property.ClassProperties;
import main.property.ClassProperties.Booster;
import main.repository.MobRepository;
import main.property.MobProperties.MobDetails;
import main.service.MobService;
import main.service.PartyService;
import main.service.PlayerService;
import main.web.dto.FightResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class MobServiceImpl implements MobService {

    private final MobRepository mobRepository;

    private final PartyService partyService;
    private final PlayerService playerService;


    @Autowired
    public MobServiceImpl(MobRepository mobRepository, PartyService partyService, PlayerService playerService) {
        this.mobRepository = mobRepository;
        this.partyService = partyService;
        this.playerService = playerService;
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

    @Override
    public List<Mob> getAllMobs() {
        return this.mobRepository.findAllByOrderByCreatedOnDescHealthAscAttackAscDefenseAsc();
    }

    @Override
    public FightResult attackMob(UUID mobId, Player player) {

        Mob mob = getMobById(mobId);

        FightOutcome fightOutcome = executeFight(player, mob);

        List<Player> partyMembers = this.playerService.getAllByParty(player.getParty());


        if (fightOutcome == FightOutcome.MOB_WIN) {
            return FightResult
                    .builder()
                    .mobName(mob.getName())
                    .outcome(fightOutcome)
                    .adenaEarned(0)
                    .xpEarned(0)
                    .build();
        }

        int xpReward = mob.getXpDrop();
        int adenaReward = mob.getAdenaDrop();


        if (partyMembers.isEmpty()) {
            rewardPlayer(player, xpReward, adenaReward);
        } else {
            xpReward = xpReward / partyMembers.size();
            adenaReward = adenaReward / partyMembers.size();

            for (Player member : partyMembers) {
                rewardPlayer(member, xpReward, adenaReward);
            }
        }

        mob.setAlive(false);
        this.mobRepository.save(mob);

        return FightResult.builder()
                .mobName(mob.getName())
                .outcome(fightOutcome)
                .xpEarned(xpReward)
                .adenaEarned(adenaReward)
                .build();
    }

    private void rewardPlayer(Player player, int xpReward, int adenaReward) {

        List<Booster> boosters = this.playerService.getBoosters(player.getPlayerClass());

        double xpBooster = boosters.stream()
                .filter(booster -> booster.getType() == BoosterType.XP_BOOSTER)
                .map(Booster::getValue)
                .findFirst()
                .orElse(0.00);

        double adenaBooster = boosters.stream()
                .filter(booster -> booster.getType() == BoosterType.ADENA_BOOSTER)
                .map(Booster::getValue)
                .findFirst()
                .orElse(0.00);


        double finalXp = xpReward + (xpReward * xpBooster);
        double finalAdena = adenaReward + (adenaReward * adenaBooster);

        player.setXp(player.getXp() + finalXp);
        player.setAdena((int) (player.getAdena() + finalAdena));

        this.playerService.levelUp(player);
        this.playerService.update(player);
    }

    @Override
    public Mob getMobById(UUID mobId) {
        return this.mobRepository.findById(mobId)
                .orElseThrow(() -> new IllegalArgumentException("No mob was found with such id"));
    }

    private FightOutcome executeFight(Player player, Mob mob) {

        double playerHealth = player.getHealth();
        double mobHealth = mob.getHealth();

        List<Booster> boosters = this.playerService.getBoosters(player.getPlayerClass());

        double attackBooster = boosters.stream()
                .filter(boosterAbility -> boosterAbility.getType() == BoosterType.ATTACK_BOOSTER)
                .map(Booster::getValue)
                .findFirst()
                .orElse(0.00);

        double defenseBooster = boosters.stream()
                .filter(boosterAbility -> boosterAbility.getType() == BoosterType.ATTACK_BOOSTER)
                .map(Booster::getValue)
                .findFirst()
                .orElse(0.00);

        while (playerHealth > 0 && mobHealth > 0) {

            // First: Player attacks mob
            double playerAttackWithBooster = player.getAttack() + (player.getAttack() * attackBooster);

            double damageToMob = Math.floor(Math.max(1, playerAttackWithBooster - (mob.getDefense() * 0.5)));

            mobHealth = Math.max(0, mobHealth - damageToMob);

            log.info("{} deals {} damage to {}. Mob HP: {}",
                    player.getNickname(), (int) damageToMob, mob.getName(), (int) mobHealth);

            // Did the mob die after player's attack?
            if (mobHealth <= 0) {
                log.info("{} killed {} and left with {} HP.", player.getNickname(), mob.getName(), (int) playerHealth);
                return FightOutcome.PLAYER_WIN;
            }

            // Second: Mob attacks player
            double playerDefenseWithBooster = player.getDefense() + (player.getDefense() * defenseBooster);

            double damageToPlayer = Math.floor(Math.max(1, mob.getAttack() - (playerDefenseWithBooster * 0.5)));

            playerHealth = Math.max(0, playerHealth - damageToPlayer);

            // Did the player die after mob's attack?
            if (playerHealth <= 0) {
                log.info("{} is defeated. {} wins with {} HP left.",
                        player.getNickname(), mob.getName(), (int) mobHealth);

                return FightOutcome.MOB_WIN;
            }
        }

        return playerHealth > 0 ? FightOutcome.PLAYER_WIN : FightOutcome.MOB_WIN;
    }
}
