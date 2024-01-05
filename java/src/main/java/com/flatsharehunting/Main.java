package com.flatsharehunting;

public class Main {

    public static void main(String[] args) throws Exception {
        Display.clearTerminal();

        // Se connecter ou creer un compte
        String choice = Display.userChoice(
            "Se connecter ou creer un compte ?", 
            new String[] {"Se connecter", "Creer un compte"} 
        );
        switch (choice) {
            case "Se connecter":
                Event.seConnecter();
                break;
            case "Creer un compte":
                Event.creerCompte();
                break;
        }

        Event.home();
    }
}