package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class WordleGameTest {

    private static PrintWriter log;
    private WordleDictionary dictionary;
    private WordleGame game;

    @BeforeAll
    static void createLog() {
        log = new PrintWriter(System.out, true);
    }

    @BeforeEach
    void createGame() throws Exception {
        List<String> words = new ArrayList<>();
        words.add("герой");
        words.add("гонец");
        words.add("маска");
        words.add("касса");
        dictionary = new WordleDictionary(words, log);
        game = new WordleGame(dictionary, "герой", log);
    }

    @Test
    void validMoveShouldReturnComparisonAndSpendStep() throws Exception {
        assertEquals("+^-^-", game.makeMove("ГОНЕЦ"));
        assertEquals(5, game.getSteps());
        assertFalse(game.isFinished());
    }

    @Test
    void invalidWordShouldNotSpendStep() throws Exception {
        try {
            game.makeMove("cat");
            fail("Ожидалось исключение InvalidWordException.");
        } catch (InvalidWordException exception) {
            assertEquals(6, game.getSteps());
        }

        try {
            game.makeMove("вагон");
            fail("Ожидалось исключение WordNotFoundInDictionary.");
        } catch (WordNotFoundInDictionary exception) {
            assertEquals(6, game.getSteps());
        }
    }

    @Test
    void correctWordShouldWinTheGame() throws Exception {
        assertEquals("+++++", game.makeMove("герой"));
        assertTrue(game.isWon());
        assertTrue(game.isFinished());
    }

    @Test
    void gameShouldFinishAfterSixUnsuccessfulMoves() throws Exception {
        for (int i = 0; i < WordleGame.MAX_STEPS; i++) {
            game.makeMove("маска");
        }
        assertEquals(0, game.getSteps());
        assertTrue(game.isFinished());
        assertFalse(game.isWon());
    }

    @Test
    void suggestionShouldRespectPreviousComparison() throws Exception {
        game.makeMove("гонец");
        assertEquals("герой", game.suggestWord());
    }

    @Test
    void suggestionsShouldNotRepeat() throws Exception {
        String firstSuggestion = game.suggestWord();
        String secondSuggestion = game.suggestWord();
        assertNotEquals(firstSuggestion, secondSuggestion);
    }
}
