package ru.yandex.practicum.exception;

public class GameAlreadyFinishedException extends GameException {

    public GameAlreadyFinishedException() {
        super("Игра уже завершена.");
    }
}
