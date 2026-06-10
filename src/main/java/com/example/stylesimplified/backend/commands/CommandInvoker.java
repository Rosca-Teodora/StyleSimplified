package com.example.stylesimplified.backend.commands;

import com.example.stylesimplified.backend.services.AuditService;
import java.util.Stack;


// "middleman"
// tine minte comenzile executate pt a le putea reversa
// history = stiva
public class CommandInvoker {
    //private Stack<Command> history = new Stack<>();

    public void executeCommand(Command command){
        command.execute();
        AuditService.getInstance().logAction(command.getCommandText());
        //history.push(command);
    }

//    public void undoLastCommand(){
//        if (!history.empty()){
//            Command lastCommand = history.pop();
//            lastCommand.undo();
//        }
//        else {
//            System.out.println("There's nothing to undo yet :P");
//        }
//    }

}
