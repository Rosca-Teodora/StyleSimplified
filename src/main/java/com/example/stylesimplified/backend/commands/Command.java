package com.example.stylesimplified.backend.commands;

// interfata pentru implementarea design pattern-ului "command"
// pt ca design pattern-ul "command" foloseste un tip de callback am nevoie sa stiu mereu numele metodei apelate de clasa
// => fortez fiecare clasa care face vreo modificare sa implementeze acelasi nume pt metode prin interfata Command

public interface Command {
    void execute();
    void undo();
}
