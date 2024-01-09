package com.flatsharehunting;

import java.util.List;
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

        // Select action depending on if the user is in a project or not, and if a logement is accepted or not
        Boolean currenUserHasProject = (
            (CurrentUser.getIdProjetColoc() != null) 
            && 
            (Database.select(
                "idProjetColoc",
                "ProjetColoc",
                "idProjetColoc=" + CurrentUser.getIdProjetColoc()
            ).size() != 0)
        );
        if(currenUserHasProject){
            Map<String, Object> logementAccepted = Project.getLogementAccepted();
            if(logementAccepted != null){
                Display.projectOutcome(logementAccepted);
                return;
            }
        }

        if(!currenUserHasProject){
            String choice = Display.userChoice(
                "Que voulez vous faire ?", 
                new String[] {"Créer un projet de colocation"} 
            );
            switch (choice) {
                case "Créer un projet de colocation":
                    Event.createProjetColoc();
                    break;
            }
        } else {
            String choice = Display.userChoice(
                "Que voulez vous faire ?", 
                new String[] {
                    "Voir les logements du projet", 
                    "Ajouter un logement", 
                    "Ajouter une personne a la colocation",
                    "Voir les personnes de la colocation"
                } 
            );
            switch (choice) {
                case "Voir les logements du projet":
                    Event.seeLogementInProjectListAndActions(); break;
                case "Ajouter un logement":
                    Event.addLogementToProject(); break;
                case "Ajouter une personne a la colocation":
                    Event.addPersonneToProject(); break;
                case "Voir les personnes de la colocation":
                    Event.seePeopleInProjectList(); break;
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
        Project.createProject(critereVille, critereDebitMin, idProjetColoc);
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

    /**
     * Recherche de logement selon les criteres du projet
     * Demande si l'utilisateur veut l'ajouter, en ajoute un autre, ou retourne au menu principal
     */
    public static void addLogementToProject() throws Exception{
        Display.clearTerminal();
        Display.printAsTitle("🔎 Recherche de logement");
        String ville = Project.getVilleProjetColoc();
        Float debitMin = Logement.getDebitMinProjetColoc();

        String choice = "Autre logement";
        while (choice.equals("Autre logement")) {
            Display.clearTerminal();
            Display.printAsTitle("🔎 Recherche de logement");
            Display.printItalic("Recherche selon les critères...");

            // get a random logement in the city of the project, with a debit higher than the project's debit and saturation to false
            Map<String, Object> logement = Logement.getRandomLogement(ville, debitMin);
            if (logement == null) {
                Display.print("Aucun logement ne correspond aux critères du projet");
                Display.waitForEnter("Appuyez sur entrée pour revenir au menu principal...");
                Event.home();
            }
    
            // print it
            Display.printLogement(logement);
            Display.printDivider();
    
            choice = Display.userChoice(
                "Que voulez vous faire ?", 
                new String[] {
                    "Ajouter ce logement au projet", 
                    "Autre logement", 
                    "Retour au menu principal"
                }
            );
            switch (choice) {
                case "Ajouter ce logement au projet":
                    String idLogementColoc = String.valueOf(Math.abs(java.util.UUID.randomUUID().hashCode())); 
                    Event.rateLogementColoc(idLogementColoc);
                    Project.addLogementToProject(
                        idLogementColoc,
                        logement.get("idImmeuble").toString(),
                        Float.parseFloat(logement.get("debitMin").toString()),
                        Float.parseFloat(logement.get("debitMax").toString())
                    );

                    Display.print("Logement ajouté au projet ✅");
                    Display.print("Retour au menu principal...");
                    Thread.sleep(3000);
                    Event.home();
                    break;
                case "Retour au menu principal":
                    Event.home();
                    break;
            }
        }
    }

    /**
     * Print la liste des logements du projet
     * Et propose de les noter, mettre une date de visite, abandonner, ou marquer le bail comme signé
     * @throws Exception
     */
    public static void seeLogementInProjectListAndActions() throws Exception{
        Display.printItalic("Chargement...");
        List<Map<String, Object>> logements = Project.getLogementsColoc();

        //if no logement
        if(logements.size() == 0){
            Display.printDivider();
            Display.print("Aucun logement dans le projet");
            Display.printDivider();
            Display.waitForEnter("Appuyez sur entrée pour revenir au menu principal...");
            Event.home();
            return;
        }

        //order by noteMoyenne desc
        logements.sort((logement1, logement2) -> {
            Float note1 = Project.getNoteMoyenne(logement1.get("idLogementColoc").toString());
            Float note2 = Project.getNoteMoyenne(logement2.get("idLogementColoc").toString());
            return note2.compareTo(note1);
        });

        // print logements
        for (Map<String, Object> logement : logements) {
            Display.printLogement(logement);
        }
        Display.printDivider();

        // actions
        String choice = Display.userChoice(
            "Que voulez vous faire ?", 
            new String[] {
                "Noter un logement", 
                "Mettre une date de visite", 
                "Abandonner un logement", 
                "Marquer l'offre d'un logement comme acceptée", 
                "Retour au menu principal"
            }
        );
        switch (choice) {
            case "Noter un logement":
                String idLogementColoc = Display.userInput("Entrez l'identifiant du logement à noter: ", false);
                Event.rateLogementColoc(idLogementColoc);
                Display.print("Logement noté ✅");
                Display.printItalic("Retour au menu principal...");
                Thread.sleep(3000);
                Event.home();
                break;
            case "Mettre une date de visite":
                String idLogementColoc2 = Display.userInput("Entrez l'identifiant du logement : ", false);
                String dateVisite = Display.userInput("Entrez la date de visite : ", false);
                Project.setDateVisite(idLogementColoc2, dateVisite);
                Display.print("Date de visite ajoutée ✅");
                Display.printItalic("Retour au menu principal...");
                Thread.sleep(3000);
                Event.home();
                break;
            case "Abandonner un logement":
                String idLogementColoc3 = Display.userInput("Entrez l'identifiant du logement : ", false);
                Project.abandonLogement(idLogementColoc3);
                Display.print("Logement abandonné ✅");
                Display.printItalic("Retour au menu principal...");
                Thread.sleep(3000);
                Event.home();
                break;
            case "Marquer l'offre d'un logement comme acceptée":
                String idLogementColoc4 = Display.userInput("Entrez l'identifiant du logement : ", false);
                Project.acceptOffreLogement(idLogementColoc4);
                Display.print("Offre acceptée ✅");
                Display.printItalic("Retour au menu principal...");
                Thread.sleep(3000);
                Event.home();
                break;
            case "Retour au menu principal":
                Event.home(); break;
        }
        return;
    }

    /**
     * Demande une note entre 1 et 5 et l'ajoute à NoteLogement
     * @param idLogementColoc
     */
    public static void rateLogementColoc(String idLogementColoc) throws Exception{
        String note = Display.userChoice(
            "Notez ce logement :", 
            new String[] {
                "✨",
                "✨✨",
                "✨✨✨",
                "✨✨✨✨",
                "✨✨✨✨✨"
            }
        );
        CurrentUser.rateLogementColoc(
            idLogementColoc, 
            note.length()
        );
    }
}
