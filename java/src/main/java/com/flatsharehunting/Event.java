package com.flatsharehunting;

import java.util.Map;

import com.flatsharehunting.handleDatabase.Database;

public class Event {

    /**
     * Home : bienvenue, choix d'action
     */
    public static void home() throws Exception{
        // Welcome
        Display.clearTerminal();
        Display.printAsTitle("👋 Bienvenue " + CurrentUser.getPrenom() + " " + CurrentUser.getNom());

        // Select action depending on if the user is in a project or not
        if(CurrentUser.getIdProjetColoc() == null){
            String choice2 = Display.userChoice(
                "Que voulez vous faire ?", 
                new String[] {"Créer un projet de colocation"} 
            );
            switch (choice2) {
                case "Créer un projet de colocation":
                    Event.createProjetColoc();
                    break;
            }
        } else {
            String choice2 = Display.userChoice(
                "Que voulez vous faire ?", 
                new String[] {
                    "Voir les logements du projet", 
                    "Ajouter un logement", 
                    "Ajouter une personne a la colocation",
                    "Voir les personnes de la colocation"
                } 
            );
            switch (choice2) {
                case "Voir les logements du projet":
                    // voir les logements
                    break;
                case "Ajouter un logement":
                    Event.addLogementToProject();
                    break;
                case "Ajouter une personne a la colocation":
                    Event.addPersonneToProject();
                    break;
                case "Voir les personnes de la colocation":
                    Event.seePeopleInProjectList();
                    break;
            }
        }
    }

    /**
     * Demande prenom nom, et le connecte si il existe
     */
    public static void seConnecter() throws Exception{
        String fullName = Display.userInput("Entrez votre prénom et votre nom : ", false);
        if(fullName.split(" ").length != 2) {
            throw new Error(Display.red("Nom et prénom requis"));
        }
        Map<String, Object> user = User.getUser(fullName.split(" ")[0], fullName.split(" ")[1]);
        CurrentUser.setCurrentUser(user);
    }

    /**
     * Demande prenom nom, cree un compte si il n'existe pas, et le connecte
     */
    public static void creerCompte() throws Exception{
        String newFullName = Display.userInput("Entrez votre prénom et nom :", false);
        if(newFullName.split(" ").length != 2) {
            throw new Error(Display.red("Nom et prénom requis"));
        }
        Map<String, Object> newUser = User.createUser(newFullName.split(" ")[0], newFullName.split(" ")[1]);
        CurrentUser.setCurrentUser(newUser);
    }

    /**
     * Demande ville et debit minimum, cree un projet de colocation, et assigne le projet a l'utilisateur
     */
    public static void createProjetColoc() throws Exception{
        String idProjetColoc = Math.abs(java.util.UUID.randomUUID().hashCode()) + "";
        String critereVille = Display.userInput("Entrez la ville du logement souhaité:", false);
        String critereDebitMin = Display.userInput("Entrez le debit minimum de la connexion souhaité: ", false);
        Project.createProject(critereVille, critereDebitMin);
        CurrentUser.setIdProjetColoc(
            CurrentUser.getIdPersonne(),
            Integer.parseInt(idProjetColoc)
        );
        Event.home();
    }

    /**
     * Demande prenom nom, et l'ajoute au projet coloc si il n'est pas deja dans un projet
     */
    public static void addPersonneToProject() throws Exception{
        String fullName = Display.userInput("Entrez son prénom et son nom : ", false);
        if(fullName.split(" ").length != 2) {
            throw new Error(Display.red("Nom et prénom requis"));
        }
        Map<String, Object> user = User.getUser(fullName.split(" ")[0], fullName.split(" ")[1]);
        if(user.get("idProjetColoc") != null){
            throw new Error(Display.red("Cette personne est déjà dans un projet"));
        }
        CurrentUser.setIdProjetColoc(
            (Integer) user.get("idPersonne"),
            CurrentUser.getIdProjetColoc()
        );
        Display.print("Personne ajoutée au projet ✅");
        Display.print("Retour au menu principal...");
        Thread.sleep(3000);
        Event.home();
    }

    /**
     * Print la liste des personnes du projet
     */
    public static void seePeopleInProjectList() throws Exception{
        Database.select(
            "idPersonne, prenom, nom, idProjetColoc",
            "Personne",
            "idProjetColoc=" + CurrentUser.getIdProjetColoc()
        ).forEach(personne -> {
            Display.print(personne.get("prenom") + " " + personne.get("nom"));
        });
        Display.waitForEnter("Appuyez sur entrée pour revenir au menu principal...");
        Event.home();
    }

    public static void addLogementToProject(){
        Display.clearTerminal();
        // get a random logement in the city of the project, with a debit higher than the project's debit and saturation to false

        // print it
        

        // prompt user to choose if we should add it to the project or not
        // 3 choices :
        // - add it to the project
        // - get another random logement
        // - go back to home
    }
}
