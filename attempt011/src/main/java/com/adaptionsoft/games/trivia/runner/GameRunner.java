package com.adaptionsoft.games.trivia.runner;
import java.util.Random;

import com.adaptionsoft.games.uglytrivia.Game;
import com.adaptionsoft.games.uglytrivia.ports.GameState;

public class GameRunner {

    private static GameState state;

    public static void main(String[] args) {
        Game aGame = new Game();

        aGame.add("Chet");
        aGame.add("Pat");
        aGame.add("Sue");

        Random rand = new Random();

        do {
            aGame.roll(rand.nextInt(5) + 1);

            if (rand.nextInt(9) == 7) {
                state = aGame.playerAnsweredWrong();
            } else {
                state = aGame.playerAnsweredCorrectly();
            }
        } while (state != GameState.GameOver);

    }
}