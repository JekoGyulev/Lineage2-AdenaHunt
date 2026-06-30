package main.service.impl;

import jakarta.transaction.Transactional;
import main.model.Party;
import main.model.Player;
import main.repository.PartyRepository;
import main.service.PartyService;
import main.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class PartyServiceImpl implements PartyService {

    private final PartyRepository partyRepository;
    private final PlayerService playerService;


    @Autowired
    public PartyServiceImpl(PartyRepository partyRepository, PlayerService playerService) {
        this.partyRepository = partyRepository;
        this.playerService = playerService;
    }


    @Override
    public void invitePlayer(UUID senderId, UUID invitedPlayerId) {
        Player sender = this.playerService.getPlayerById(senderId);
        Player receiver = this.playerService.getPlayerById(invitedPlayerId);

        Party party = getParty(sender);

        receiver.setParty(party);
        receiver.setJoinedPartyOn(LocalDateTime.now());

        this.playerService.update(receiver);
    }

    @Override
    @Transactional
    public void dismiss(UUID partyId, Player currentPlayer) {

        Party party = this.partyRepository.findById(partyId)
                .orElseThrow(() -> new IllegalArgumentException("No party was found with such id"));

        List<Player> membersOfCurrentParty = this.playerService.getAllByParty(party);

        if (party.getLeader().getId().equals(currentPlayer.getId())) {

            if (membersOfCurrentParty.size() >= 2) {
                Player nextLeader = membersOfCurrentParty.stream()
                        .filter(p -> !p.getId().equals(currentPlayer.getId()))
                        .findFirst()
                        .orElseThrow();

                party.setLeader(nextLeader);

                this.partyRepository.save(party);
            } else {
                this.partyRepository.delete(party);
            }

        }

        currentPlayer.setParty(null);
        currentPlayer.setJoinedPartyOn(null);

        this.playerService.update(currentPlayer);
    }

    private Party getParty(Player sender) {

        if (sender.getParty() != null) {
            return sender.getParty();
        }

        Party party = Party.builder()
                .leader(sender)
                .build();

        this.partyRepository.save(party);

        sender.setParty(party);
        sender.setJoinedPartyOn(LocalDateTime.now());

        this.playerService.update(sender);

        return party;
    }
}
