package ru.yandex.practicum;

import ru.yandex.practicum.exception.GameStateException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

public class WordleDictionary {

    public static final int WORD_LENGTH = 5;

    private final List<String> words;
    private final LinkedHashMap<String, Integer> wordCounts;
    private final PrintWriter log;

    public WordleDictionary(List<String> sourceWords, PrintWriter log) {
        this.log = log;

        wordCounts = new LinkedHashMap<>();
        for (String sourceWord : sourceWords) {
            String word = normalize(sourceWord);
            if (isValidFormat(word)) {
                if (wordCounts.containsKey(word)) {
                    int repetitions = wordCounts.get(word);
                    wordCounts.put(word, repetitions + 1);
                } else {
                    wordCounts.put(word, 1);
                }
            }
        }

        words = new ArrayList<>();
        for (String word : wordCounts.keySet()) {
            words.add(word);
        }

        log("Словарь создан. Количество уникальных слов: " + words.size());
    }

    public int size() {
        return words.size();
    }

    public boolean isEmpty() {
        return words.isEmpty();
    }

    public boolean contains(String word) {
        return wordCounts.containsKey(normalize(word));
    }

    public String get(int index) {
        return words.get(index);
    }

    public String getRandomWord(Random random) {
        if (words.isEmpty()) {
            throw new GameStateException("Нельзя выбрать слово из пустого словаря.");
        }
        return words.get(random.nextInt(words.size()));
    }

    public List<String> getWords() {
        return new ArrayList<>(words);
    }

    public static String normalize(String word) {
        if (word == null) {
            return "";
        }
        return word.trim().toLowerCase().replace("ё", "е");
    }

    public static boolean isValidFormat(String word) {
        if (word == null || word.length() != WORD_LENGTH) {
            return false;
        }

        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            if (letter < 'а' || letter > 'я') {
                return false;
            }
        }
        return true;
    }

    public static String compareWords(String answer, String guess) {
        if (!isValidFormat(answer) || !isValidFormat(guess)) {
            throw new IllegalArgumentException("Для сравнения нужны два пятибуквенных русских слова.");
        }

        char[] result = new char[WORD_LENGTH];
        boolean[] usedAnswerLetters = new boolean[WORD_LENGTH];

        for (int i = 0; i < WORD_LENGTH; i++) {
            result[i] = '-';
            if (answer.charAt(i) == guess.charAt(i)) {
                result[i] = '+';
                usedAnswerLetters[i] = true;
            }
        }

        for (int guessIndex = 0; guessIndex < WORD_LENGTH; guessIndex++) {
            if (result[guessIndex] == '+') {
                continue;
            }

            for (int answerIndex = 0; answerIndex < WORD_LENGTH; answerIndex++) {
                if (!usedAnswerLetters[answerIndex] && guess.charAt(guessIndex) == answer.charAt(answerIndex)) {
                    result[guessIndex] = '^';
                    usedAnswerLetters[answerIndex] = true;
                    break;
                }
            }
        }

        StringBuilder comparison = new StringBuilder(WORD_LENGTH);
        for (int i = 0; i < result.length; i++) {
            comparison.append(result[i]);
        }
        return comparison.toString();
    }

    private void log(String message) {
        log.println("[WordleDictionary] " + message);
        log.flush();
    }
}
