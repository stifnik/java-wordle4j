package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.DictionaryLoadingException;
import ru.yandex.practicum.exception.EmptyDictionaryException;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class WordleDictionaryLoaderTest {

    private static WordleDictionaryLoader loader;

    @BeforeAll
    static void createLoader() {
        PrintWriter log = new PrintWriter(System.out, true);
        loader = new WordleDictionaryLoader(log);
    }

    @Test
    void shouldLoadGameWordsFromUtf8File() throws Exception {
        WordleDictionary dictionary = loader.load("words_ru.txt");
        assertTrue(!dictionary.isEmpty());
        assertTrue(dictionary.contains("аббат"));
    }

    @Test
    void shouldReportMissingFile() {
        try {
            loader.load("missing-dictionary.txt");
            fail("Ожидалось исключение DictionaryLoadingException.");
        } catch (DictionaryLoadingException exception) {
            assertTrue(exception.getMessage().contains("missing-dictionary.txt"));
        } catch (EmptyDictionaryException exception) {
            fail("Получено исключение неверного типа.");
        }
    }

    @Test
    void shouldReportDictionaryWithoutSuitableWords() {
        try {
            loader.load("README.md");
            fail("Ожидалось исключение EmptyDictionaryException.");
        } catch (EmptyDictionaryException exception) {
            assertTrue(exception.getMessage().contains("нет подходящих"));
        } catch (DictionaryLoadingException exception) {
            fail("Получено исключение неверного типа.");
        }
    }
}
