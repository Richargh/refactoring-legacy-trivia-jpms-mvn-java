package com.adaptionsoft.games.uglytrivia;

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

    public static class PenaltyBox {

        private final boolean[] inPenaltyBox = new boolean[6];

        private final boolean[] isGettingOutOfPenaltyBox = new boolean[6];

        public boolean isNotAllowedToPlay(PlayerId playerId) {
            return inPenaltyBox[playerId.rawValue()] && !isGettingOutOfPenaltyBox[playerId.rawValue()];
        }

        public boolean isInPenaltyBox(PlayerId playerId) {
            return inPenaltyBox[playerId.rawValue()];
        }

        public void sendToPenaltyBox(Player player) {
            System.out.println(player.name() + " was sent to the penalty box");
            inPenaltyBox[player.id().rawValue()] = true;
        }

        public void fetchFromPenaltyBox(Player player) {
            isGettingOutOfPenaltyBox[player.id().rawValue()] = true;
            System.out.println(player.name() + " is getting out of the penalty box");
        }

        public void keepInPenaltyBox(Player player) {
            isGettingOutOfPenaltyBox[player.id().rawValue()] = false;
            System.out.println(player.name() + " is not getting out of the penalty box");
        }
    }

    public static class Catalogue {

        private final List<Question> popQuestions = new LinkedList<>();

        private final List<Question> scienceQuestions = new LinkedList<>();

        private final List<Question> sportsQuestions = new LinkedList<>();

        private final List<Question> rockQuestions = new LinkedList<>();

        public Catalogue() {
            initQuestionCatalogue();
        }

        private void initQuestionCatalogue() {
            for (int i = 0; i < 50; i++) {
                popQuestions
                        .add(new Question("Pop Question " + i));
                scienceQuestions
                        .add(new Question(("Science Question " + i)));
                sportsQuestions
                        .add(new Question(("Sports Question " + i)));
                rockQuestions
                        .add(new Question("Rock Question " + i));
            }
        }

        public void askQuestion(Category category) {
            System.out.println("The category is " + category);

            if (category == Category.Pop) System.out.println(popQuestions
                                       .remove(0));
            if (category == Category.Science) System.out.println(scienceQuestions
                                       .remove(0));
            if (category == Category.Sports) System.out.println(sportsQuestions
                                       .remove(0));
            if (category == Category.Rock) System.out.println(rockQuestions
                                       .remove(0));
        }

        private record Question(String rawValue){
            @Override
            public String toString() {
                return rawValue;
            }
        }
    }
}