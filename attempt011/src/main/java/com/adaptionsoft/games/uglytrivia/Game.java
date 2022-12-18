package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.internal.Board;
import com.adaptionsoft.games.uglytrivia.internal.Catalogue;
import com.adaptionsoft.games.uglytrivia.internal.PenaltyBox;
import com.adaptionsoft.games.uglytrivia.internal.Purses;
import com.adaptionsoft.games.uglytrivia.ports.Player;
import com.adaptionsoft.games.uglytrivia.ports.PlayerId;
import com.adaptionsoft.games.uglytrivia.ports.PlayerName;

import java.util.*;

public class Game {

    private final List<Player> players = new ArrayList<>();

    private final Catalogue catalogue = new Catalogue();

    private final PenaltyBox penaltyBox = new PenaltyBox();

    private final Board board = new Board();

    private final Purses purses = new Purses();

    private Player currentPlayer = null;

    public void add(String playerName) {
        var player = new Player(new PlayerId(players.size()), new PlayerName(playerName));
        players.add(player);
        board.addPlayerToBoard(player.id());
        purses.addPlayerPurse(player.id());
        if(currentPlayer == null)
            currentPlayer = players.get(0);

        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
    }

    public void roll(int roll) {
        System.out.println(currentPlayer.name() + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (penaltyBox.isInPenaltyBox(this.currentPlayer.id())) {
            if (isEven(roll)) {
                penaltyBox.keepInPenaltyBox(currentPlayer);
            } else {
                penaltyBox.fetchFromPenaltyBox(currentPlayer);

                play(roll);
            }
        } else {
            play(roll);
        }
    }

    public boolean playerAnsweredCorrectly() {
        var id = currentPlayer.id();

        if (penaltyBox.isNotAllowedToPlay(id)){
            selectNextPlayer();
            return true;
        } else {
            purses.winCoin(currentPlayer);
            selectNextPlayer();

            return purses.shouldGameContinue(id);
        }
    }

    public boolean playerAnsweredWrong(){
        System.out.println("Question was incorrectly answered");
        penaltyBox.sendToPenaltyBox(currentPlayer);

        selectNextPlayer();
        return true;
    }

    private static boolean isEven(int roll) {
        return roll % 2 == 0;
    }

    private void play(int roll) {
        var category = board.movePlayer(roll, currentPlayer);
        catalogue.askQuestion(category);
    }

    private void selectNextPlayer() {
        var nextPlayerIndex = players.indexOf(currentPlayer) + 1;
        if (nextPlayerIndex == players.size()) nextPlayerIndex = 0;
        currentPlayer = players.get(nextPlayerIndex);
    }
}