package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    private final PrintWriter log;

    public WordleDictionaryLoader(PrintWriter log) {
        this.log = log;
    }

    public WordleDictionary load(String filename) throws DictionaryLoadingException, EmptyDictionaryException {
        List<String> sourceWords = new ArrayList<>();
        log("Начинается загрузка словаря из файла " + filename);

        try (BufferedReader reader = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                sourceWords.add(reader.readLine());
            }
        } catch (IOException exception) {
            throw new DictionaryLoadingException("Не удалось прочитать файл словаря: " + filename, exception);
        }

        WordleDictionary dictionary = new WordleDictionary(sourceWords, log);
        if (dictionary.isEmpty()) {
            throw new EmptyDictionaryException("В файле нет подходящих пятибуквенных русских слов.");
        }

        log("Загрузка словаря завершена.");
        return dictionary;
    }

    private void log(String message) {
        log.println("[WordleDictionaryLoader] " + message);
        log.flush();
    }
}
