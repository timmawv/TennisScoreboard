package avlyakulov.timur.service;

import avlyakulov.timur.model.Match;
import avlyakulov.timur.dao.MatchesInProgress;
import avlyakulov.timur.model.Player;
import avlyakulov.timur.model.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MatchScoreCalculationServiceTest {

    Match match;
    UUID matchId;
    Player playerOne = new Player(1, "Timur");
    Player playerTwo = new Player(2, "Dima");

    MatchScoreCalculationService matchScoreCalculationService = new MatchScoreCalculationService();

    @BeforeEach
    void setUp() {
        match = new Match(playerOne, playerTwo);
        matchId = UUID.randomUUID();
        MatchesInProgress.createMatch(matchId, match);
    }

    //MethodName_ExpectedBehavior_StateUnderTest
    @Test
    void addPointToWinnerOfGame_add15PointFirstPlayer_scoreFirstPlayer() {
        int winnerId = 1;

        matchScoreCalculationService.addPointToWinnerOfGame(winnerId, matchId);

        assertEquals(15, match.getPointPlayerOne());
        assertEquals(0, match.getPointPlayerTwo());
        assertEquals(0, match.getGamePlayerOne());
        assertEquals(0, match.getGamePlayerTwo());
        assertEquals(0, match.getSetPlayerOne());
        assertEquals(0, match.getSetPlayerTwo());
        assertEquals(State.GAME, match.getState());
    }

    @Test
    void addPointToWinnerOfGame_add10PointFirstPlayer_scoreFirstPlayer() {
        int winnerId = 1;
        match.setPointPlayerOne(30);

        matchScoreCalculationService.addPointToWinnerOfGame(winnerId, matchId);

        assertEquals(40, match.getPointPlayerOne());
        assertEquals(0, match.getGamePlayerOne());
        assertEquals(0, match.getSetPlayerOne());
        assertEquals(State.GAME, match.getState());
    }

    @Test
    void addPointToWinnerOfGame_enableAdvantage_secondPlayerEnableAdvantage() {
        int winnerId = 2;
        match.setPointPlayerOne(40);
        match.setPointPlayerTwo(30);

        matchScoreCalculationService.addPointToWinnerOfGame(winnerId, matchId);

        assertEquals(40, match.getPointPlayerOne());
        assertEquals(40, match.getPointPlayerTwo());
        assertEquals(State.ADVANTAGE, match.getState());
    }


    @Test
    void addPointToWinnerOfGame_add5PointPlayerOne_advantageFirstPlayerScores() {
        int winnerId = 1;
        match.setPointPlayerOne(40);
        match.setPointPlayerTwo(40);
        match.setState(State.ADVANTAGE);

        matchScoreCalculationService.addPointToWinnerOfGame(winnerId, matchId);

        assertEquals(45, match.getPointPlayerOne());
        assertEquals(40, match.getPointPlayerTwo());
        assertEquals(State.ADVANTAGE, match.getState());
    }

    @Test
    void addPointToWinnerOfGame_set40PointPlayers_advantagePlayersScoreSecondPlayer() {
        int winnerId = 2;
        match.setPointPlayerOne(45);
        match.setPointPlayerTwo(40);
        match.setState(State.ADVANTAGE);

        matchScoreCalculationService.addPointToWinnerOfGame(winnerId, matchId);

        assertEquals(40, match.getPointPlayerOne());
        assertEquals(40, match.getPointPlayerTwo());
        assertEquals(State.ADVANTAGE, match.getState());
    }

    @Test
    void addPointToWinnerOfGame_setOneGameFirstPlayer_advantagePlayersScoreFirstPlayer() {
        int winnerId = 1;
        match.setPointPlayerOne(45);
        match.setPointPlayerTwo(40);
        match.setState(State.ADVANTAGE);

        matchScoreCalculationService.addPointToWinnerOfGame(winnerId, matchId);

        assertEquals(0, match.getPointPlayerOne());
        assertEquals(0, match.getPointPlayerTwo());
        assertEquals(1, match.getGamePlayerOne());
        assertEquals(0, match.getGamePlayerTwo());
        assertEquals(State.GAME, match.getState());
    }

    @Test
    void addPointToWinnerOfGame_addOneGameFirstPlayer_score40PointFirstPlayer() {
        int winnerId = 1;
        match.setPointPlayerOne(40);

        matchScoreCalculationService.addPointToWinnerOfGame(winnerId, matchId);

        assertEquals(0, match.getPointPlayerOne());
        assertEquals(1, match.getGamePlayerOne());
        assertEquals(0, match.getSetPlayerOne());
        assertEquals(State.GAME, match.getState());
    }

    @Test
    void addPointToWinnerOfGame_beginTieBreak_playerOne6GamePlayerTwo6Game() {
        int winnerId = 1;
        match.setPointPlayerOne(40);
        match.setGamePlayerOne(5);
        match.setGamePlayerTwo(6);
        match.setState(State.GAME);

        matchScoreCalculationService.addPointToWinnerOfGame(winnerId, matchId);

        assertEquals(0, match.getPointPlayerOne());
        assertEquals(0, match.getPointPlayerTwo());
        assertEquals(6, match.getGamePlayerOne());
        assertEquals(6, match.getGamePlayerTwo());
        assertEquals(0, match.getSetPlayerOne());
        assertEquals(0, match.getSetPlayerTwo());
        assertEquals(State.TIE, match.getState());
    }

    @Test
    void addPointToWinnerOfGame_tieBreakPlayerOneScore1Point_playerOne6GamePlayerTwo6Game() {
        int winnerId = 1;
        match.setGamePlayerOne(6);
        match.setGamePlayerTwo(6);
        match.setState(State.TIE);

        matchScoreCalculationService.addPointToWinnerOfGame(winnerId, matchId);

        assertEquals(1, match.getPointPlayerOne());
        assertEquals(0, match.getPointPlayerTwo());
        assertEquals(6, match.getGamePlayerOne());
        assertEquals(6, match.getGamePlayerTwo());
        assertEquals(0, match.getSetPlayerOne());
        assertEquals(0, match.getSetPlayerTwo());
        assertEquals(State.TIE, match.getState());
    }

    @Test
    void addPointToWinnerOfGame_tieBreakPlayerOneScore1PointGameContinues_playerOne6GamePlayerTwo6Game() {
        int winnerId = 1;
        match.setPointPlayerOne(6);
        match.setPointPlayerTwo(6);
        match.setGamePlayerOne(6);
        match.setGamePlayerTwo(6);
        match.setState(State.TIE);

        matchScoreCalculationService.addPointToWinnerOfGame(winnerId, matchId);

        assertEquals(7, match.getPointPlayerOne());
        assertEquals(6, match.getPointPlayerTwo());
        assertEquals(6, match.getGamePlayerOne());
        assertEquals(6, match.getGamePlayerTwo());
        assertEquals(0, match.getSetPlayerOne());
        assertEquals(0, match.getSetPlayerTwo());
        assertEquals(State.TIE, match.getState());
    }

    @Test
    void addPointToWinnerOfGame_tieBreakPlayerOneScore1PointWinGame_playerOne6GamePlayerTwo6Game() {
        int winnerId = 1;
        match.setPointPlayerOne(9);
        match.setPointPlayerTwo(8);
        match.setGamePlayerOne(6);
        match.setGamePlayerTwo(6);
        match.setState(State.TIE);

        matchScoreCalculationService.addPointToWinnerOfGame(winnerId, matchId);

        assertEquals(0, match.getPointPlayerOne());
        assertEquals(0, match.getPointPlayerTwo());
        assertEquals(0, match.getGamePlayerOne());
        assertEquals(0, match.getGamePlayerTwo());
        assertEquals(1, match.getSetPlayerOne());
        assertEquals(0, match.getSetPlayerTwo());
        assertEquals(State.GAME, match.getState());
    }

    @Test
    void addPointToWinnerOfGame_tieBreakPlayerOneWinGame_playerOne6PointPlayerTwo6Point() {
        int winnerId = 1;
        match.setPointPlayerOne(6);
        match.setPointPlayerTwo(5);
        match.setGamePlayerOne(6);
        match.setGamePlayerOne(6);
        match.setState(State.TIE);

        matchScoreCalculationService.addPointToWinnerOfGame(winnerId, matchId);

        assertEquals(0, match.getPointPlayerOne());
        assertEquals(0, match.getPointPlayerTwo());
        assertEquals(0, match.getGamePlayerOne());
        assertEquals(0, match.getGamePlayerTwo());
        assertEquals(1, match.getSetPlayerOne());
        assertEquals(0, match.getSetPlayerTwo());
        assertEquals(State.GAME, match.getState());
    }

    @Test
    void addPointToWinnerOfGame_gameContinues_score40Point6GameFirstPlayerAndScore0Point5GameSecondPlayer() {
        int winnerId = 1;
        match.setPointPlayerOne(40);
        match.setGamePlayerOne(5);
        match.setGamePlayerTwo(5);

        matchScoreCalculationService.addPointToWinnerOfGame(winnerId, matchId);

        assertEquals(0, match.getPointPlayerOne());
        assertEquals(6, match.getGamePlayerOne());
        assertEquals(5, match.getGamePlayerTwo());
        assertEquals(0, match.getSetPlayerOne());
        assertEquals(0, match.getSetPlayerTwo());
        assertEquals(State.GAME, match.getState());
    }

    @Test
    void addPointToWinnerOfGame_addOneSetFirstPlayer_gameScore40Point6GameFirstPlayerAndScore0Point5GameSecondPlayer() {
        int winnerId = 1;
        match.setPointPlayerOne(40);
        match.setGamePlayerOne(6);
        match.setGamePlayerTwo(5);

        matchScoreCalculationService.addPointToWinnerOfGame(winnerId, matchId);

        assertEquals(0, match.getPointPlayerOne());
        assertEquals(0, match.getGamePlayerOne());
        assertEquals(0, match.getGamePlayerTwo());
        assertEquals(1, match.getSetPlayerOne());
        assertEquals(0, match.getSetPlayerTwo());
        assertEquals(State.GAME, match.getState());
    }

    @Test
    void addPointToWinnerOfGame_addOneSetFirstPlayer_firstPlayerScoresGameScore40Point6GameFirstPlayer() {
        int winnerId = 1;
        match.setPointPlayerOne(40);
        match.setGamePlayerOne(5);
        match.setGamePlayerTwo(4);

        matchScoreCalculationService.addPointToWinnerOfGame(winnerId, matchId);

        assertEquals(0, match.getPointPlayerOne());
        assertEquals(0, match.getGamePlayerOne());
        assertEquals(0, match.getGamePlayerTwo());
        assertEquals(1, match.getSetPlayerOne());
        assertEquals(State.GAME, match.getState());
    }

    @Test
    void addPointToWinnerOfGame_addOneSetFirstPlayer_firstPlayerScoresGameScore40Point6GameFirstPlayerAndScore0Point5GameSecondPlayer() {
        int winnerId = 1;
        match.setPointPlayerOne(40);
        match.setGamePlayerOne(6);
        match.setGamePlayerTwo(5);

        matchScoreCalculationService.addPointToWinnerOfGame(winnerId, matchId);

        assertEquals(0, match.getPointPlayerOne());
        assertEquals(0, match.getGamePlayerOne());
        assertEquals(0, match.getGamePlayerTwo());
        assertEquals(1, match.getSetPlayerOne());
        assertEquals(State.GAME, match.getState());
    }

    @Test
    void addPointToWinnerOfGame_firstPlayerWinGame_score40Point1GameFirstPlayer() {
        int winnerId = 1;
        match.setPointPlayerOne(40);
        match.setGamePlayerOne(5);
        match.setSetPlayerOne(1);
        match.setState(State.GAME);

        matchScoreCalculationService.addPointToWinnerOfGame(winnerId, matchId);

        assertEquals(0, match.getPointPlayerOne());
        assertEquals(0, match.getGamePlayerOne());
        assertEquals(2, match.getSetPlayerOne());
        assertEquals(State.FINISHED, match.getState());
    }
}