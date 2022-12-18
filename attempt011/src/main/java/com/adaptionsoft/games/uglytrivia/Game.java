package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.internal.Catalogue;
import com.adaptionsoft.games.uglytrivia.internal.PenaltyBox;
import com.adaptionsoft.games.uglytrivia.ports.Category;
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

    public static class Purses {

        private final Map<PlayerId, Integer> allPurses = new HashMap<>();

        public void winCoin(Player player) {
            System.out.println("Answer was correct!!!!");
            var earnedCoins = allPurses.get(player.id()) + 1;
            allPurses.put(player.id(), earnedCoins);
            System.out.println(player.name()
                               + " now has "
                               + earnedCoins
                               + " Gold Coins.");
        }

        public boolean shouldGameContinue(PlayerId playerId) {
            return allPurses.get(playerId) != 6;
        }

        private void addPlayerPurse(PlayerId playerId) {
            allPurses.put(playerId, 0);
        }
    }

    public static class Board {

        private final Map<PlayerId, Integer> places = new HashMap<>();

        private void addPlayerToBoard(PlayerId playerId) {
            places.put(playerId, 0);
        }

        public Category movePlayer(int roll, Player player) {
            var id = player.id();
            places.put(id, places.get(id) + roll);
            if (places.get(id) > 11) places.put(id, places.get(id) - 12);

            System.out.println(player.name()
                               + "'s new location is "
                               + places.get(id));

            return currentCategory(places.get(id));
        }

        private Category currentCategory(int place) {
            if (place == 0) return Category.Pop;
            if (place == 4) return Category.Pop;
            if (place == 8) return Category.Pop;
            if (place == 1) return Category.Science;
            if (place == 5) return Category.Science;
            if (place == 9) return Category.Science;
            if (place == 2) return Category.Sports;
            if (place == 6) return Category.Sports;
            if (place == 10)return Category.Sports;
            return Category.Rock;
        }
    }
}