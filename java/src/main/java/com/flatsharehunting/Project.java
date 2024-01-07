package com.flatsharehunting;

import java.util.List;
import java.util.Map;

import com.flatsharehunting.handleDatabase.Database;

public class Project {

    /**
     * Create a project in the database
     * @param String critereVille
     * @param Float critereDebitMin
     * @return Integer idProjetColoc
     */
    public static void createProject(String critereVille, String critereDebitMin, String idProjetColoc) {
        Database.insert(
            "ProjetColoc",
            "idProjetColoc, critereVille, critereDebitMin",
            idProjetColoc+",'"+critereVille+"',"+critereDebitMin.toString()
        );
    }

    /**
     * Add a user to a project
     * @param idPersonne
     */
    public static void addUserToProject(Integer idPersonne, Integer idProjetColoc){
        Database.update(
            "Personne", 
            "idProjetColoc", 
            idProjetColoc.toString(),
            String.format("idPersonne='%s'", idPersonne)
        );
    }

    public static void getNoteLogementColoc(String idLogementColoc){
        List<Map<String, Object>> result = Database.select(
            """
            SELECT AVG(note) AS "note"
            FROM "VoteLogement"
            WHERE "idLogementColoc" = 
            """ + idLogementColoc
        );
        // TODO test + return
    }

    /**
     * Get the city criteria of the project
     * @return String critereVille
     */
    public static String getVilleProjetColoc(){
        List<Map<String, Object>> result = Database.select(
            """
            SELECT "critereVille"
            FROM "ProjetColoc"
            WHERE "idProjetColoc" = 
            """ + CurrentUser.getIdProjetColoc()
        );
        return result.get(0).get("critereVille").toString();
    }

    public static void addLogementToProject(String idLogementColoc, String idImmeuble){
        Database.insert(
            "LogementColoc", 
            "idLogementColoc, idImmeuble, idProjetColoc, idPersonneAjout, offreAcceptee, abandon", 
            String.format("%s, %s, '%s', '%s', %s, %s", 
                idLogementColoc, 
                idImmeuble,
                CurrentUser.getIdProjetColoc(), 
                CurrentUser.getIdPersonne(), 
                false, 
                false
            )
        );
    }

    // tests
    public static void main(String[] args) {
    }

}
