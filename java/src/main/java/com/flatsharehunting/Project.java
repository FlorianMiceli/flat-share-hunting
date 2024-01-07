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

    public static void addLogementToProject(String idLogementColoc, String idImmeuble, Float debitMin, Float debitMax){
        Database.insert(
            "LogementColoc", 
            "idLogementColoc, idImmeuble, idProjetColoc, idPersonneAjout, debitMin, debitMax, offreAcceptee, abandon", 
            String.format("%s, %s, '%s', '%s', %s, %s, %s, %s", 
                idLogementColoc, 
                idImmeuble,
                CurrentUser.getIdProjetColoc(), 
                CurrentUser.getIdPersonne(), 
                debitMin,
                debitMax,
                false, 
                false
            )
        );
    }

    public static List<Map<String,Object>> getLogementsColoc(){
        return Database.select(
        """
        SELECT
            LC."idLogementColoc",
            LC."dateVisite",
            LC."idPersonneAjout",
            LC."dateVisite",
            LC."abandon",
            P."nom" AS nomPersonneAjout,
            P."prenom" AS prenomPersonneAjout,
            BI."typeImmeuble",
            BI."numeroAdresse",
            BI."repetitionAdresse",
            BI."nomVoieAdresse",
            BI."codePostalAdresse",
            BI."nomCommuneAdresse",
            LC."debitMin",
            LC."debitMax"
        FROM
            LogementColoc LC
        JOIN
            Personne P ON LC."idPersonneAjout" = P."idPersonne"
        JOIN
            baseImmeuble91 BI ON LC."idImmeuble" = BI."idImmeuble"
        WHERE
            LC."idProjetColoc" = '37050816'
            AND LC."abandon" = false
        """
        );
    }

    /**
     * Get the average note of a logement
     * @param idLogementColoc
     * @return Float noteMoyenne
     */
    public static Float getNoteMoyenne(String idLogementColoc){
        List<Map<String, Object>> result = Database.select(
            """
            SELECT AVG("note") AS "noteMoyenne"
            FROM "NoteLogement"
            WHERE "idLogementColoc" = 
            """ + idLogementColoc
        );
        return Float.parseFloat(result.get(0).get("noteMoyenne").toString());
    }

    /**
     * Set dateVisite in db
     * @param idLogementColoc String
     * @param dateVisite String
     */
    public static void setDateVisite(String idLogementColoc, String dateVisite){
        Display.print("dateVisite : " + dateVisite);
        Database.update(
            "LogementColoc", 
            "dateVisite", 
            "'"+dateVisite+"'",
            String.format("idLogementColoc='%s'", idLogementColoc)
        );
    }

    /**
     * Set abandon = true in db
     * @param idLogementColoc
     */
    public static void abandonLogement(String idLogementColoc){
        Database.update(
            "LogementColoc", 
            "abandon", 
            "true",
            String.format("idLogementColoc='%s'", idLogementColoc)
        );
    }

    /**
     * Set offreAcceptee = true in db
     * @param idLogementColoc
     */
    public static void acceptOffreLogement(String idLogementColoc){
        Database.update(
            "LogementColoc", 
            "offreAcceptee", 
            "true",
            String.format("idLogementColoc='%s'", idLogementColoc)
        );
    }

    // tests
    public static void main(String[] args) {
        // getNoteMoyenne
        System.out.println(getNoteMoyenne("1201331392"));
    }

}
