package ru.yandex.practicum;

public class InvalidWordException extends GameException {

    public InvalidWordException(String word) {
        super("Введите существующее русское слово из пяти букв. Введено: «"
                + word + "».");
    }
}
