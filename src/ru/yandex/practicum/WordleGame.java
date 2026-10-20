package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

public class WordleGame {

    public static final int MAX_STEPS = 6;

    private final String answer;
    private final WordleDictionary dictionary;
    private final PrintWriter log;
    private final Random random;
    private final LinkedHashMap<String, String> moves;
    private final LinkedHashMap<String, Boolean> suggestedWords;

    private int steps;
    private boolean won;

    public WordleGame(WordleDictionary dictionary, PrintWriter log) throws EmptyDictionaryException {
        if (dictionary == null || dictionary.isEmpty()) {
            throw new EmptyDictionaryException("Нельзя начать игру с пустым словарём.");
        }
        this.dictionary = dictionary;
        this.log = log;
        this.random = new Random();
        this.moves = new LinkedHashMap<>();
        this.suggestedWords = new LinkedHashMap<>();
        this.steps = MAX_STEPS;
        this.won = false;
        this.answer = dictionary.getRandomWord(random);

        log("Игра началась. Ответ: " + this.answer + ", оставшиеся попытки: " + steps);
    }

    WordleGame(WordleDictionary dictionary, String answer, PrintWriter log) throws EmptyDictionaryException, WordNotFoundInDictionary {
        if (dictionary == null || dictionary.isEmpty()) {
            throw new EmptyDictionaryException("Нельзя начать игру с пустым словарём.");
        }

        String normalizedAnswer = WordleDictionary.normalize(answer);
        if (!dictionary.contains(normalizedAnswer)) {
            throw new WordNotFoundInDictionary(normalizedAnswer);
        }
        this.dictionary = dictionary;
        this.log = log;
        this.random = new Random();
        this.moves = new LinkedHashMap<>();
        this.suggestedWords = new LinkedHashMap<>();
        this.steps = MAX_STEPS;
        this.won = false;
        this.answer = normalizedAnswer;

        log("Игра началась. Ответ: " + this.answer + ", оставшиеся попытки: " + steps);
    }

    public String makeMove(String enteredWord) throws InvalidWordException, WordNotFoundInDictionary, GameAlreadyFinishedException {
        if (isFinished()) {
            throw new GameAlreadyFinishedException();
        }

        String word = WordleDictionary.normalize(enteredWord);

        if (!WordleDictionary.isValidFormat(word)) {
            throw new InvalidWordException(enteredWord);
        }
        if (!dictionary.contains(word)) {
            throw new WordNotFoundInDictionary(word);
        }

        String comparison = WordleDictionary.compareWords(answer, word);
        moves.put(word, comparison);
        steps--;
        won = word.equals(answer);

        log("Ход: " + word + ", результат: " + comparison + ", оставшиеся попытки: " + steps + ", победа: " + won);
        checkState();
        return comparison;
    }

    public String suggestWord() throws GameAlreadyFinishedException {
        if (isFinished()) {
            throw new GameAlreadyFinishedException();
        }

        List<String> allWords = dictionary.getWords();
        List<String> suitableWords = new ArrayList<>();

        for (String candidate : allWords) {
            if (!moves.containsKey(candidate) && !suggestedWords.containsKey(candidate) && matchesAllMoves(candidate)) {
                suitableWords.add(candidate);
            }
        }

        if (suitableWords.isEmpty()) {
            throw new GameStateException("Не найдено ни одного варианта, хотя ответ должен оставаться среди вариантов.");
        }

        String suggestion = suitableWords.get(random.nextInt(suitableWords.size()));
        suggestedWords.put(suggestion, true);
        log("Подсказка: " + suggestion + ", подходящих вариантов: " + suitableWords.size());
        return suggestion;
    }

    public int getSteps() {
        return steps;
    }

    public boolean isWon() {
        return won;
    }

    public boolean isFinished() {
        return won || steps == 0;
    }

    public String getAnswer() {
        return answer;
    }

    private boolean matchesAllMoves(String candidate) {
        for (String previousWord : moves.keySet()) {
            String expectedComparison = moves.get(previousWord);
            String candidateComparison = WordleDictionary.compareWords(candidate, previousWord);
            if (!expectedComparison.equals(candidateComparison)) {
                return false;
            }
        }
        return true;
    }

    private void checkState() {
        if (steps < 0 || steps > MAX_STEPS) {
            throw new GameStateException("Некорректное количество оставшихся попыток: " + steps);
        }
        if (won && !moves.containsKey(answer)) {
            throw new GameStateException("Игра отмечена как выигранная без правильного ответа.");
        }
    }

    private void log(String message) {
        log.println("[WordleGame] " + message);
        log.flush();
    }
}
