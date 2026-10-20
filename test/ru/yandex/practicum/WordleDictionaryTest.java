package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordleDictionaryTest {

    private static PrintWriter log;
    private WordleDictionary dictionary;

    @BeforeAll
    static void createLog() {
        log = new PrintWriter(System.out, true);
    }

    @BeforeEach
    void createDictionary() {
        List<String> words = new ArrayList<>();
        words.add(" Герой ");
        words.add("герой");
        words.add("актёр");
        words.add("дом");
        words.add("house");
        dictionary = new WordleDictionary(words, log);
    }

    @Test
    void shouldNormalizeFilterAndRemoveDuplicates() {
        assertEquals(2, dictionary.size());
        assertTrue(dictionary.contains("ГЕРОЙ"));
        assertTrue(dictionary.contains("актер"));
        assertFalse(dictionary.contains("house"));
    }

    @Test
    void returnedWordListShouldBeACopy() {
        List<String> copy = dictionary.getWords();
        copy.clear();
        assertEquals(2, dictionary.size());
    }

    @Test
    void shouldCompareWordsByWordleRules() {
        assertEquals("+^-^-", WordleDictionary.compareWords("герой", "гонец"));
        assertEquals("^++-+", WordleDictionary.compareWords("маска", "касса"));
        assertEquals("+++++", WordleDictionary.compareWords("герой", "герой"));
    }
}
