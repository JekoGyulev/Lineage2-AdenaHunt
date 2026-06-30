package main.service.impl;

import main.model.Party;
import main.model.Player;
import main.repository.PartyRepository;
import main.service.PartyService;
import main.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        this.playerService.update(receiver);
    }

    @Override
    public void dismiss(UUID partyId, Player currentPlayer) {

        Party party = this.partyRepository.findById(partyId)
                .orElseThrow(() -> new IllegalArgumentException("No party was found with such id"));

        List<Player> membersOfCurrentParty = this.playerService.getAllByParty(party);

        if (party.getLeader().equals(currentPlayer)) {

            if (membersOfCurrentParty.size() >= 2) {
                Player nextLeader = membersOfCurrentParty.get(1);
                party.setLeader(nextLeader);
            } else {
                party.setLeader(null);
            }

            this.partyRepository.save(party);
        }

        currentPlayer.setParty(null);

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

        this.playerService.update(sender);

        return party;
    }
}
