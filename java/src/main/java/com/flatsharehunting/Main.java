package com.flatsharehunting;

public class Main {

    public static void main(String[] args) throws Exception {
        Display.clearTerminal();
        Display.printAsTitle("🏠 Bienvenue sur FlatShare Hunting 🏠");

        // Se connecter ou creer un compte
        String choice = Display.userChoice(
            "Se connecter ou créer un compte ?", 
            new String[] {"Se connecter", "Créer un compte"} 
        );
        switch (choice) {
            case "Se connecter":
                Event.seConnecter(); break;
            case "Créer un compte":
                Event.creerCompte(); break;
        }

        Event.home();
    }
}