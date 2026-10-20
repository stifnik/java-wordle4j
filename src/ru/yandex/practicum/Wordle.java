package ru.yandex.practicum;

import ru.yandex.practicum.exception.GameException;
import ru.yandex.practicum.exception.LogFileCreationException;
import ru.yandex.practicum.exception.ProgramException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Wordle {

    private static final String DICTIONARY_FILENAME = "words_ru.txt";
    private static final String LOG_FILENAME = "wordle.log";

    public static void main(String[] args) {
        PrintWriter log;
        try {
            log = createLog();
        } catch (LogFileCreationException exception) {
            System.err.println(exception.getMessage());
            return;
        }

        try (PrintWriter openedLog = log) {
            try {
                startGame(openedLog);
            } catch (Exception exception) {
                openedLog.println("[Wordle] Работа программы завершена из-за ошибки.");
                exception.printStackTrace(openedLog);
                openedLog.flush();
                System.out.println("Произошла внутренняя ошибка. Подробности записаны в " + LOG_FILENAME + ".");
            }
        }
    }

    private static PrintWriter createLog() throws LogFileCreationException {
        try {
            FileWriter fileWriter = new FileWriter(LOG_FILENAME, StandardCharsets.UTF_8, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            return new PrintWriter(bufferedWriter);
        } catch (IOException exception) {
            throw new LogFileCreationException("Не удалось создать лог-файл " + LOG_FILENAME + ".", exception);
        }
    }

    private static void startGame(PrintWriter log) throws ProgramException {
        WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
        WordleDictionary dictionary = loader.load(DICTIONARY_FILENAME);
        WordleGame game = new WordleGame(dictionary, log);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Игра Wordle");
        System.out.println("Введите русское слово из пяти букв.");
        System.out.println("Нажмите Enter без слова, чтобы компьютер сделал ход.");

        while (!game.isFinished()) {
            System.out.println();
            System.out.println("Осталось попыток: " + game.getSteps());
            System.out.print("> ");

            if (!scanner.hasNextLine()) {
                System.out.println();
                System.out.println("Ввод завершён пользователем.");
                break;
            }

            String enteredWord = scanner.nextLine();
            try {
                String wordForMove = enteredWord;
                if (enteredWord.isEmpty()) {
                    wordForMove = game.suggestWord();
                    System.out.println("Подсказка компьютера: " + wordForMove);
                }

                String comparison = game.makeMove(wordForMove);
                System.out.println("> " + comparison);
            } catch (GameException exception) {
                System.out.println(exception.getMessage());
            }
        }

        if (game.isWon()) {
            System.out.println("Вы угадали слово!");
        } else if (game.getSteps() == 0) {
            System.out.println("Попытки закончились.");
        }
        System.out.println("Загаданное слово: " + game.getAnswer());
    }
}
