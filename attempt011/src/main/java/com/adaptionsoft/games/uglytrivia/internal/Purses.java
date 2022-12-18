package com.adaptionsoft.games.uglytrivia.internal;

import com.adaptionsoft.games.uglytrivia.ports.Player;
import com.adaptionsoft.games.uglytrivia.ports.PlayerId;

import java.util.HashMap;
import java.util.Map;

public class Purses {

    private final Map<PlayerId, Integer> allPurses = new HashMap<>();

    public void winCoin(Player player) {
        System.out.println("Answer was correct!!!!");
        var earnedCoins = allPurses.get(player.id()) + 1;
        allPurses.put(player.id(), earnedCoins);
        System.out.println(player.name() + " now has " + earnedCoins + " Gold Coins.");
    }

    public boolean shouldGameContinue(PlayerId playerId) {
        return allPurses.get(playerId) != 6;
    }

    public void addPlayer(PlayerId playerId) {
        allPurses.put(playerId, 0);
    }
}
