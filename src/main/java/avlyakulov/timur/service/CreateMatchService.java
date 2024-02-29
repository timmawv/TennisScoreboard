package avlyakulov.timur.service;

import avlyakulov.timur.model.Match;
import avlyakulov.timur.model.MatchesInProgress;
import avlyakulov.timur.model.Player;

import java.util.UUID;

public class CreateMatchService {
    private PlayerService playerService;

    public CreateMatchService() {
        this.playerService = new PlayerService();
    }

    public UUID createMatch(String playerOneName, String playerTwoName) {
        playerOneName = capitalizeFirstLetter(playerOneName);
        playerTwoName = capitalizeFirstLetter(playerTwoName);
        Match match = addPlayersToMatch(playerOneName, playerTwoName);
        UUID matchId = UUID.randomUUID();
        MatchesInProgress.createMatch(matchId, match);
        return matchId;
    }

    private Match addPlayersToMatch(String playerOneName, String playerTwoName) {
        Player playerOne = new Player(playerOneName);
        Player playerTwo = new Player(playerTwoName);
        playerOne.setId(playerService.getPlayerByNameOrCreateHimIfNotExist(playerOne));
        playerTwo.setId(playerService.getPlayerByNameOrCreateHimIfNotExist(playerTwo));

        return new Match(playerOne, playerTwo);
    }

    private String capitalizeFirstLetter(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}