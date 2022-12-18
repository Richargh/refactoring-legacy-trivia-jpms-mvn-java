package com.adaptionsoft.games.uglytrivia.internal;

import com.adaptionsoft.games.uglytrivia.ports.Category;
import com.adaptionsoft.games.uglytrivia.ports.Player;
import com.adaptionsoft.games.uglytrivia.ports.PlayerId;

import java.util.HashMap;
import java.util.Map;

public class Board {

    private final Map<PlayerId, Integer> places = new HashMap<>();

    public void addPlayerToBoard(PlayerId playerId) {
        places.put(playerId, 0);
    }

    public Category movePlayer(int roll, Player player) {
        var id = player.id();
        places.put(id, places.get(id) + roll);
        if (places.get(id) > 11) places.put(id, places.get(id) - 12);

        System.out.println(player.name() + "'s new location is " + places.get(id));

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
        if (place == 10) return Category.Sports;
        return Category.Rock;
    }
}
