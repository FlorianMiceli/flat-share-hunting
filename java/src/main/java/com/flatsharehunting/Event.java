package com.flatsharehunting;

import java.util.Map;

public class Event {

    /**
     * Home : bienvenue, choix d'action
     */
    public static void home() throws Exception{
        // Welcome
        Display.clearTerminal();
        Display.printAsTitle("Bienvenue " + CurrentUser.getPrenom() + " " + CurrentUser.getNom());

        // Select action depending on if the user is in a project or not
        if(CurrentUser.getIdProjetColoc() == null){
            String choice2 = Display.userChoice(
                "Que voulez vous faire ?", 
                new String[] {"Creer un projet de colocation"} 
            );
            switch (choice2) {
                case "Creer un projet de colocation":
                    Event.createProjetColoc();
                    break;
            }
        } else {
            String choice2 = Display.userChoice(
                "Que voulez vous faire ?", 
                new String[] {"Voir les logements du projet", "Ajouter un logement", "Ajouter une personne a la colocation"} 
            );
            switch (choice2) {
                case "Voir les logements du projet":
                    // voir les logements
                    break;
                case "Ajouter un logement":
                    // ajouter un logement
                    break;
                case "Ajouter une personne a la colocation":
                    Event.addPersonneToProject();
                    break;
                case "Voir les personnes de la colocation":
                    // voir les personnes de la colocation
                    break;
            }
        }
    }

    /**
     * Demande prenom nom, et le connecte si il existe
     */
    public static void seConnecter() throws Exception{
        String fullName = Display.userInput("Entrez votre prenom et votre nom : ", false);
        if(fullName.split(" ").length != 2) {
            throw new Error(Display.red("Nom et prenom requis"));
        }
        Map<String, Object> user = User.getUser(fullName.split(" ")[0], fullName.split(" ")[1]);
        CurrentUser.setCurrentUser(user);
    }

    /**
     * Demande prenom nom, cree un compte si il n'existe pas, et le connecte
     */
    public static void creerCompte() throws Exception{
        String newFullName = Display.userInput("Entrez votre prenom et nom :", false);
        if(newFullName.split(" ").length != 2) {
            throw new Error(Display.red("Nom et prenom requis"));
        }
        Map<String, Object> newUser = User.createUser(newFullName.split(" ")[0], newFullName.split(" ")[1]);
        CurrentUser.setCurrentUser(newUser);
    }

    /**
     * Demande ville et debit minimum, cree un projet de colocation, et assigne le projet a l'utilisateur
     */
    public static void createProjetColoc() throws Exception{
        String idProjetColoc = Math.abs(java.util.UUID.randomUUID().hashCode()) + "";
        String critereVille = Display.userInput("Entrez la ville du logement souhaite:", false);
        String critereDebitMin = Display.userInput("Entrez le debit minimum de la connexion souhaite: ", false);
        Project.createProject(critereVille, critereDebitMin);
        CurrentUser.setIdProjetColoc(
            CurrentUser.getIdPersonne(),
            Integer.parseInt(idProjetColoc)
        );
        Event.home();
    }

    public static void addPersonneToProject() throws Exception{
        String fullName = Display.userInput("Entrez son prenom et son nom : ", false);
        if(fullName.split(" ").length != 2) {
            throw new Error(Display.red("Nom et prenom requis"));
        }
        Map<String, Object> user = User.getUser(fullName.split(" ")[0], fullName.split(" ")[1]);
        if(user.get("idProjetColoc") != null){
            throw new Error(Display.red("Cette personne est deja dans un projet"));
        }
        CurrentUser.setIdProjetColoc(
            (Integer) user.get("idPersonne"),
            CurrentUser.getIdProjetColoc()
        );
        Display.print("Personne ajoutee au projet avec succes");
        Display.print("Retour au menu principal...");
        Thread.sleep(3000);
        Event.home();
    }
}
