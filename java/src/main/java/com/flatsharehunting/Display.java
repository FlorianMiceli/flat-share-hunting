package com.flatsharehunting;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
     * Print a message in italic
     * @param message String
     */
    public static void printItalic(String message) {
        System.out.println("\u001B[3m" + message + "\u001B[0m");
    }

    /**
     * Clear the terminal
     */
    public static void clearTerminal() {
        System.out.println(ansi().eraseScreen().restorCursorPosition());
    }

    /**
     * Print a divider
     */
    public static void printDivider() {
        Display.print("----------------------------------------------------");
    }

    /**
     * Print a message in red
     * @param message String
     */
    public static String red(String message) {
        return "\u001B[31m" + message + "\u001B[0m";
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
    public static String userInput(String question, Boolean clearTerminal) throws Exception {
        if(clearTerminal){clearTerminal();}
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

    /**
     * Wait for the user to press enter with a message in italic
     * @param message String
     */
    public static void waitForEnter(String message) throws IOException {
        printItalic(message);
        System.in.read();
    }

    /**
     * Print a logement
     * Example:
     * ----------------------------------------------------
     * 🏢 Immeuble
     * 📌 11 Rue du Chemin des Femmes, 91377 Massy
     * 📶 1 Gbps
     *
     * ⭐ 4.2/5
     * 👤 Ajouté par : Florian Miceli
     * 🆔 627828762
     * ----------------------------------------------------
     * @param logement Map<String, Object>
     */
    public static void printLogement(Map<String, Object> logement) {
        printDivider();

        String type = logement.get("typeImmeuble").toString();
        Float debitMin = Float.parseFloat(logement.get("debitMin").toString());
        Float debitMax = Float.parseFloat(logement.get("debitMax").toString());
        switch (type) {
            case "IM":
                type = "🏢 Immeuble"; break;
            case "PA":
                type = "🏠 Pavillon"; break;
            default:
                type = "❔ Type de logement inconnu";  break;
        }
        // Type de logement
        print(type);
        // Adresse complète
        print("📌 " + Logement.getFullAdress(logement));

        // Debit min et max
        if (debitMin == 1000.0f){
            print("📶 1 Gbps");
        }else{
            print("📶 " + debitMin + "-" + debitMax+" Mbps");
        }

        if(logement.containsKey("idPersonneAjout")){
            if(logement.get("idPersonneAjout") != null){
                print("");

                // Moyenne des notes du goupe
                // si CurrentUser a voté, print (Votre note : note/5) en vert après la note moyenne
                String currentUserNote = CurrentUser.getNoteForLogement(logement.get("idLogementColoc").toString());
                String currentUserNoteMessage = "";
                if(currentUserNote != null){
                    currentUserNoteMessage ="\u001B[3m\u001B[32mVotre note : " + CurrentUser.getNoteForLogement(logement.get("idLogementColoc").toString()) + "/5\u001B[0m \u001B[0m";
                }
                print("✨ " + Project.getNoteMoyenne(logement.get("idLogementColoc").toString()) + "/5 " + currentUserNoteMessage);
                
                // Ajouté par
                Map<String, Object> user = User.getUser(Integer.parseInt(logement.get("idPersonneAjout").toString()));
                print(
                    "👤 Ajouté par : " + 
                    user.get("prenom").toString() + " " + 
                    user.get("nom").toString()
                );

                // Date de visite
                if(logement.containsKey("dateVisite")){
                    if(logement.get("dateVisite") != null){
                        print("📅 Visite le " + logement.get("dateVisite").toString());
                    }
                }

                //identifiant 
                print("🆔 "+logement.get("idLogementColoc").toString());
            }
        }
    }

    /**
     * Print final message when a logement is accepted
     * @param logementAccepted Map<String, Object>
     */
    public static void projectOutcome(Map<String, Object> logementAccepted) throws Exception{
        Display.print("🎉 Félicitations ! Votre colocation va prendre vie !");
        Display.printLogement(logementAccepted);
        Display.printDivider();
        Display.print("👥 Voici les colocataires :");
        Project.printPersonnesProjet();
        Display.printDivider();
        Display.waitForEnter("Appuyez sur entrée pour quitter l'application");
    }


    // tests
    public static void main(String[] args) throws IOException {
        System.out.println("✨✨".length());

    }

}
