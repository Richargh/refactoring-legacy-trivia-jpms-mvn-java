package com.adaptionsoft.games.uglytrivia.internal;

import com.adaptionsoft.games.uglytrivia.ports.Player;
import com.adaptionsoft.games.uglytrivia.ports.PlayerId;

public class PenaltyBox {

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
        inPenaltyBox[player.id()
                .rawValue()] = true;
    }

    public void returnFromPenaltyBox(Player player) {
        isGettingOutOfPenaltyBox[player.id()
                .rawValue()] = true;
        System.out.println(player.name() + " is getting out of the penalty box");
    }

    public void stayInPenaltyBox(Player player) {
        isGettingOutOfPenaltyBox[player.id()
                .rawValue()] = false;
        System.out.println(player.name() + " is not getting out of the penalty box");
    }
}
