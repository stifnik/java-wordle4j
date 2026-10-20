package ru.yandex.practicum;

public class GameAlreadyFinishedException extends GameException {

    public GameAlreadyFinishedException() {
        super("Игра уже завершена.");
    }
}
