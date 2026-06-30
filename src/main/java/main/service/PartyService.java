package main.service;

import java.util.UUID;

public interface PartyService {
    void invitePlayer(UUID senderId, UUID invitedPlayerId);
}
