package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game {

    private final List<PlayerName> players = new ArrayList<>();
    private final int[] places = new int[6];
    private final int[] purses = new int[6];
    private final boolean[] inPenaltyBox  = new boolean[6];

    private final List<Question> popQuestions = new LinkedList<>();
    private final List<Question> scienceQuestions = new LinkedList<>();
    private final List<Question> sportsQuestions = new LinkedList<>();
    private final List<Question> rockQuestions = new LinkedList<>();

    private int currentPlayerIndex = 0;
    private boolean isGettingOutOfPenaltyBox;

    public  Game(){
        initQuestionCatalogue();
    }

    private void initQuestionCatalogue() {
        for (int i = 0; i < 50; i++) {
            popQuestions.add(new Question("Pop Question " + i));
            scienceQuestions.add(new Question(("Science Question " + i)));
            sportsQuestions.add(new Question(("Sports Question " + i)));
            rockQuestions.add(new Question("Rock Question " + i));
        }
    }

    public void add(String playerName) {
        players.add(new PlayerName(playerName));
        places[howManyPlayers()] = 0;
        purses[howManyPlayers()] = 0;
        inPenaltyBox[howManyPlayers()] = false;

        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
    }

    public void roll(int roll) {
        System.out.println(players.get(currentPlayerIndex) + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (inPenaltyBox[currentPlayerIndex]) {
            if (isEven(roll)) {
                isGettingOutOfPenaltyBox = false;
                System.out.println(players.get(currentPlayerIndex) + " is not getting out of the penalty box");
            } else {
                isGettingOutOfPenaltyBox = true;
                System.out.println(players.get(currentPlayerIndex) + " is getting out of the penalty box");

                play(roll);
            }
        } else {
            play(roll);
        }
    }

    private static boolean isEven(int roll) {
        return roll % 2 == 0;
    }

    private void play(int roll) {
        movePlayer(roll, currentPlayerIndex);
        int playerPlace = places[currentPlayerIndex];
        System.out.println("The category is " + currentCategory(playerPlace));
        askQuestion(playerPlace);
    }

    private void movePlayer(int roll, int playerIndex) {
        places[playerIndex] = places[playerIndex] + roll;
        if (places[playerIndex] > 11) places[playerIndex] = places[playerIndex] - 12;

        System.out.println(players.get(playerIndex)
                           + "'s new location is "
                           + places[playerIndex]);
    }

    public boolean playerAnsweredCorrectly() {
        int playerIndex = this.currentPlayerIndex;

        if (inPenaltyBox[playerIndex] && !isGettingOutOfPenaltyBox){
            selectNextPlayer();
            return true;
        } else {
            winCoin(playerIndex);
            selectNextPlayer();

            return didPlayerWin(playerIndex);
        }
    }

    private void winCoin(int playerIndex) {
        System.out.println("Answer was correct!!!!");
        purses[playerIndex]++;
        System.out.println(players.get(playerIndex)
                           + " now has "
                           + purses[playerIndex]
                           + " Gold Coins.");
    }

    public boolean playerAnsweredWrong(){
        System.out.println("Question was incorrectly answered");
        sendToPenaltyBox(currentPlayerIndex);

        selectNextPlayer();
        return true;
    }

    private void selectNextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex == players.size()) currentPlayerIndex = 0;
    }

    private void sendToPenaltyBox(int playerIndex) {
        System.out.println(players.get(playerIndex) + " was sent to the penalty box");
        inPenaltyBox[playerIndex] = true;
    }

    private String currentCategory(int place) {
        if (place == 0) return "Pop";
        if (place == 4) return "Pop";
        if (place == 8) return "Pop";
        if (place == 1) return "Science";
        if (place == 5) return "Science";
        if (place == 9) return "Science";
        if (place == 2) return "Sports";
        if (place == 6) return "Sports";
        if (place == 10) return "Sports";
        return "Rock";
    }

    private void askQuestion(int place) {
        if (currentCategory(place) == "Pop")
            System.out.println(popQuestions.remove(0));
        if (currentCategory(place) == "Science")
            System.out.println(scienceQuestions.remove(0));
        if (currentCategory(place) == "Sports")
            System.out.println(sportsQuestions.remove(0));
        if (currentCategory(place) == "Rock")
            System.out.println(rockQuestions.remove(0));
    }

    private int howManyPlayers() {
        return players.size();
    }

    private boolean didPlayerWin(int playerIndex) {
        return purses[playerIndex] != 6;
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
}