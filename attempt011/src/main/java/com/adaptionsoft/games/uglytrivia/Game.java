package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game {

    private final List<PlayerName> players = new ArrayList<>();

    private final Catalogue catalogue = new Catalogue();

    private final PenaltyBox penaltyBox = new PenaltyBox();

    private int currentPlayerIndex = 0;

    public  Game(){
        catalogue.initQuestionCatalogue();
    }

    public void add(String playerName) {
        players.add(new PlayerName(playerName));
        places[players.size()] = 0;
        purses[players.size()] = 0;

        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
    }

    public void roll(int roll) {
        System.out.println(players.get(currentPlayerIndex) + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (penaltyBox.isInPenaltyBox(this)) {
            if (isEven(roll)) {
                penaltyBox.keepInPenaltyBox(currentPlayerIndex, this.players.get(currentPlayerIndex));
            } else {
                penaltyBox.fetchFromPenaltyBox(currentPlayerIndex, this.players.get(currentPlayerIndex));

                play(roll);
            }
        } else {
            play(roll);
        }
    }

    public boolean playerAnsweredCorrectly() {
        int playerIndex = this.currentPlayerIndex;

        if (penaltyBox.isNotAllowedToPlay(playerIndex, this)){
            selectNextPlayer();
            return true;
        } else {
            winCoin(playerIndex, players.get(playerIndex));
            selectNextPlayer();

            return shouldGameContinue(playerIndex);
        }
    }

    public boolean playerAnsweredWrong(){
        System.out.println("Question was incorrectly answered");
        penaltyBox.sendToPenaltyBox(currentPlayerIndex, this.players.get(currentPlayerIndex));

        selectNextPlayer();
        return true;
    }

    private static boolean isEven(int roll) {
        return roll % 2 == 0;
    }

    private void play(int roll) {
        int playerPlace = movePlayer(roll, currentPlayerIndex);
        catalogue.askQuestion(playerPlace, this);
    }

    private void selectNextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex == players.size()) currentPlayerIndex = 0;
    }

    /**
     * Category
     */
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

    /**
     * Places
     */
    private final int[] places = new int[6];
    private int movePlayer(int roll, int playerIndex) {
        places[playerIndex] = places[playerIndex] + roll;
        if (places[playerIndex] > 11) places[playerIndex] = places[playerIndex] - 12;

        System.out.println(players.get(playerIndex)
                           + "'s new location is "
                           + places[playerIndex]);
        return places[playerIndex];
    }

    /**
     * Purses
     */
    private final int[] purses = new int[6];
    private void winCoin(int playerIndex, PlayerName name) {
        System.out.println("Answer was correct!!!!");
        purses[playerIndex]++;
        System.out.println(name
                           + " now has "
                           + purses[playerIndex]
                           + " Gold Coins.");
    }

    private boolean shouldGameContinue(int playerIndex) {
        return purses[playerIndex] != 6;
    }

    /**
     * Extracted Types
     */
    private enum Category {
        Pop,
        Science,
        Sports,
        Rock
    }

    private record PlayerName(String rawValue){
        @Override
        public String toString() {
            return rawValue;
        }
    }

    private record Question(String rawValue){
        @Override
        public String toString() {
            return rawValue;
        }
    }

    public class PenaltyBox {

        /**
         * Penalty Box
         */
        private final boolean[] inPenaltyBox = new boolean[6];

        private final boolean[] isGettingOutOfPenaltyBox = new boolean[6];

        public PenaltyBox() {
        }

        public boolean isNotAllowedToPlay(int playerIndex, Game game) {
            return inPenaltyBox[playerIndex] && !isGettingOutOfPenaltyBox[playerIndex];
        }

        public boolean isInPenaltyBox(Game game) {
            return inPenaltyBox[game.currentPlayerIndex];
        }

        public void sendToPenaltyBox(int playerIndex, PlayerName name) {
            System.out.println(name + " was sent to the penalty box");
            inPenaltyBox[playerIndex] = true;
        }

        public void fetchFromPenaltyBox(int playerIndex, PlayerName name) {
            isGettingOutOfPenaltyBox[playerIndex] = true;
            System.out.println(name + " is getting out of the penalty box");
        }

        public void keepInPenaltyBox(int playerIndex, PlayerName name) {
            isGettingOutOfPenaltyBox[playerIndex] = false;
            System.out.println(name + " is not getting out of the penalty box");
        }
    }

    public static class Catalogue {

        private final List<Question> popQuestions = new LinkedList<Question>();

        private final List<Question> scienceQuestions = new LinkedList<Question>();

        private final List<Question> sportsQuestions = new LinkedList<Question>();

        private final List<Question> rockQuestions = new LinkedList<Question>();

        public Catalogue() {
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

        private void askQuestion(int place, Game game) {
            System.out.println("The category is " + game.currentCategory(place));

            if (game.currentCategory(place) == Category.Pop) System.out.println(popQuestions
                                       .remove(0));
            if (game.currentCategory(place) == Category.Science) System.out.println(scienceQuestions
                                       .remove(0));
            if (game.currentCategory(place) == Category.Sports) System.out.println(sportsQuestions
                                       .remove(0));
            if (game.currentCategory(place) == Category.Rock) System.out.println(rockQuestions
                                       .remove(0));
        }
    }
}