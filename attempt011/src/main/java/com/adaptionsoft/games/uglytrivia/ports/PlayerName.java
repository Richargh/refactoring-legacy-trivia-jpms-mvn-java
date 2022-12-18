package com.adaptionsoft.games.uglytrivia.ports;

public record PlayerName(String rawValue) {

    @Override
    public String toString() {
        return rawValue;
    }
}
