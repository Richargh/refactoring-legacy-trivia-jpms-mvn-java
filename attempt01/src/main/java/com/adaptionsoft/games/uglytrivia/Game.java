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
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true;
                System.out.println(players.get(currentPlayerIndex) + " is getting out of the penalty box");

                play(roll);
            } else {
                isGettingOutOfPenaltyBox = false;
                System.out.println(players.get(currentPlayerIndex) + " is not getting out of the penalty box");
            }
        } else {
            play(roll);
        }
    }

    private void play(int roll) {
        movePlayer(roll);
        System.out.println("The category is " + currentCategory());
        askQuestion();
    }

    private void movePlayer(int roll) {
        places[currentPlayerIndex] = places[currentPlayerIndex] + roll;
        if (places[currentPlayerIndex] > 11) places[currentPlayerIndex] = places[currentPlayerIndex] - 12;

        System.out.println(players.get(currentPlayerIndex)
                           + "'s new location is "
                           + places[currentPlayerIndex]);
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

    private String currentCategory() {
        if (places[currentPlayerIndex] == 0) return "Pop";
        if (places[currentPlayerIndex] == 4) return "Pop";
        if (places[currentPlayerIndex] == 8) return "Pop";
        if (places[currentPlayerIndex] == 1) return "Science";
        if (places[currentPlayerIndex] == 5) return "Science";
        if (places[currentPlayerIndex] == 9) return "Science";
        if (places[currentPlayerIndex] == 2) return "Sports";
        if (places[currentPlayerIndex] == 6) return "Sports";
        if (places[currentPlayerIndex] == 10) return "Sports";
        return "Rock";
    }

    private void askQuestion() {
        if (currentCategory() == "Pop")
            System.out.println(popQuestions.remove(0));
        if (currentCategory() == "Science")
            System.out.println(scienceQuestions.remove(0));
        if (currentCategory() == "Sports")
            System.out.println(sportsQuestions.remove(0));
        if (currentCategory() == "Rock")
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