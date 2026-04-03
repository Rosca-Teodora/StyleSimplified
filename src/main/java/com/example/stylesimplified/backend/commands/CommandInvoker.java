package com.example.stylesimplified.backend.commands;

import java.util.Stack;
// "middleman"
// tine minte comenzile executate pt a le putea reversa
// history = stiva
public class CommandInvoker {
    private Stack<Command> history = new Stack<>();

    public void executeCommand(Command command){
        command.execute();
        history.push(command);
    }

    public void undoLastCommand(){
        if (!history.empty()){
            Command lastCommand = history.pop();
            lastCommand.undo();
        }
        else {
            System.out.println("There's nothing to undo yet :P");
        }
    }

    // daca o sa afisez comenzile pt ultimul pas din etapa 2 -> am nevoie de un nume pt comanda sau de ceva descriptiv?
//    public void showHistory(){
//        Stack<Command> cpyHistory = history;
//
//        while (!cpyHistory.empty()){
//            System.out.println(cpyHistory.pop().name);
//        }
//    }
}
