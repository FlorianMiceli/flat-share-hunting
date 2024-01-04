package com.flatsharehunting;

import java.io.IOException;
import java.util.HashMap;

import static org.fusesource.jansi.Ansi.ansi;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.PromtResultItemIF;
import de.codeshelf.consoleui.prompt.builder.ListPromptBuilder;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import jline.TerminalFactory;

public class Display {

    /**
     * Print a message using System.out.println
     * @param message String
     */
    public static void print(String message) {
        System.out.println(message);
    }

    /**
     * Print a message as a title
     * Example: 
     * ---------------------- Title ----------------------
     * @param message string
     */
    public static void printAsTitle(String message) {
        int messageLength = message.length();
        int numberOfDashes = 50 - messageLength;
        String dashes = "";
        for (int i = 0; i < numberOfDashes / 2; i++) {
            dashes += "-";
        }
        System.out.println(dashes +" "+ message +" "+ dashes);
    }

    /**
     * Display a list of items and ask the user to choose one using ConsoleUI
     * @param question
     * @param itemList list of items to choose from
     * @return the chosen item
     */
    public static String userChoice(String question, String[] itemList) throws Exception {
        System.out.println(ansi().eraseScreen());

        ConsolePrompt prompt = new ConsolePrompt();
        PromptBuilder promptBuilder = prompt.getPromptBuilder();

        ListPromptBuilder listPromptBuilder = promptBuilder.createListPrompt()
                .name("choice")
                .message(question);

        for (String item : itemList) {
            listPromptBuilder.newItem().text(item).add();
        }
        listPromptBuilder.addPrompt();

        HashMap<String, ? extends PromtResultItemIF> result = prompt.prompt(promptBuilder.build());
        String chosenItem = result.get("choice").toString();
        chosenItem = chosenItem
            .substring(chosenItem.indexOf("=") + 1, chosenItem.length() - 1)
            .replace("'", "");

        TerminalFactory.get().restore();
        return chosenItem;
    }

    /**
     * Ask the user to input a string using ConsoleUI
     * @param question
     * @return user input
     */
    public static String userInput(String question) throws Exception {
        System.out.println(ansi().eraseScreen());
        ConsolePrompt prompt = new ConsolePrompt();                 
        PromptBuilder promptBuilder = prompt.getPromptBuilder();
        promptBuilder.createInputPrompt()                              
            .name("Question")                                              
            .message(question)
            .addPrompt();
        HashMap<String, ? extends PromtResultItemIF> result = prompt.prompt(promptBuilder.build());
        String output = result.get("Question").toString();
        output = output
            .substring(output.indexOf("=") + 1, output.length() - 1)
            .replace("'", "");
        TerminalFactory.get().restore();
        return output;
    }

    // tests
    public static void main(String[] args) throws IOException {
        printAsTitle("Title");
    }

}
