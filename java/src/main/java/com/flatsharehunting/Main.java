package com.flatsharehunting;

import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {

        // Se connecter ou creer un compte
        String choice = Display.userChoice(
            "Se connecter ou creer un compte ?", 
            new String[] {"Se connecter", "Creer un compte"} 
        );
        switch (choice) {
            case "Se connecter":
                String fullName = Display.userInput("Entrez votre nom et prenom : ");
                if(fullName.split(" ").length != 2) {
                    throw new Error(Display.red("Nom et prenom requis"));
                }
                Map<String, Object> user = User.getUser(fullName.split(" ")[0], fullName.split(" ")[1]);
                CurrentUser.setCurrentUser(user);
                break;
            case "Creer un compte":
                Display.print("Creer un compte");
                String newFullName = Display.userInput("Entrez votre prenom et nom :");
                if(newFullName.split(" ").length != 2) {
                    throw new Error(Display.red("Nom et prenom requis"));
                }
                try {
                    User.getUser(newFullName.split(" ")[0], newFullName.split(" ")[1]);
                    Display.print("slkrghlsrk");
                } catch (Error e) {
                    if(e.getMessage() == "No user found"){
                        User.createUser(newFullName.split(" ")[0], newFullName.split(" ")[1]);
                        Map<String, Object> newUser = User.getUser(newFullName.split(" ")[0], newFullName.split(" ")[1]);
                        CurrentUser.setCurrentUser(newUser);
                        break;
                    }
                }
                throw new Error(Display.red("Déjà inscrit"));
        }

        // Welcome and select action
        Display.clearTerminal();
        Display.printAsTitle("Bienvenue " + CurrentUser.getPrenom() + " " + CurrentUser.getNom());
    }
}