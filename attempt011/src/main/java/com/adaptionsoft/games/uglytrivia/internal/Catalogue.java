package com.adaptionsoft.games.uglytrivia.internal;

import com.adaptionsoft.games.uglytrivia.Game;
import com.adaptionsoft.games.uglytrivia.ports.Category;

import java.util.LinkedList;
import java.util.List;

public class Catalogue {

    private final List<Question> popQuestions = new LinkedList<>();

    private final List<Question> scienceQuestions = new LinkedList<>();

    private final List<Question> sportsQuestions = new LinkedList<>();

    private final List<Question> rockQuestions = new LinkedList<>();

    public Catalogue() {
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

    public void askQuestion(Category category) {
        System.out.println("The category is " + category);

        if (category == Category.Pop) System.out.println(popQuestions.remove(0));
        if (category == Category.Science) System.out.println(scienceQuestions.remove(0));
        if (category == Category.Sports) System.out.println(sportsQuestions.remove(0));
        if (category == Category.Rock) System.out.println(rockQuestions.remove(0));
    }

    private record Question(String rawValue) {

        @Override
        public String toString() {
            return rawValue;
        }
    }
}
