package main.service;

import main.model.Player;

import java.util.UUID;

public interface PartyService {
    void invitePlayer(UUID senderId, UUID invitedPlayerId);

    void dismiss(UUID partyId, Player currentPlayer);
}
