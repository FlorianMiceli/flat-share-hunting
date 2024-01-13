package com.flatsharehunting;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.flatsharehunting.handleDatabase.Database;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

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

    /**
     * Add a logement to the project
     * @param idLogementColoc String
     * @param idImmeuble String
     * @param debitMin Float
     * @param debitMax Float
     */
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

    /**
     * Get the logements of the project with all values needed to display, without the logements abandoned
     * @return List<Map<String,Object>> logements
     */
    public static List<Map<String,Object>> getLogementsColoc(){
        List<Map<String,Object>> result = Database.select(
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
            LC."idProjetColoc" = """ + CurrentUser.getIdProjetColoc() + """
            AND LC."abandon" = false
        """
        );

        //add a little percentage of chance to abandon a logement, to simulate rent by someone else
        Random seed = new Random(Integer.parseInt(result.get(0).get("idLogementColoc").toString()));
        Integer randomIndex = seed.nextInt(100);
        if(randomIndex < 3){
            Project.abandonLogement(result.get(0).get("idLogementColoc").toString());
            Display.print("Un logement de votre liste a été loué, il n'est plus disponible.");
        }

        return result;
    }

    /**
     * Get the average note of a logement
     * @param idLogementColoc
     * @return Float noteMoyenne
     */
    // ...

    public static Float getNoteMoyenne(String idLogementColoc) {
        List<Map<String, Object>> result = Database.select(
            """
            SELECT AVG("note") AS "noteMoyenne"
            FROM "NoteLogement"
            WHERE "idLogementColoc" = 
            """ + idLogementColoc
        );
        
        String noteMoyenneStr = result.get(0).get("noteMoyenne").toString();
        DecimalFormat df = new DecimalFormat("#.#");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        Float noteMoyenne = Float.parseFloat(df.format(Float.parseFloat(noteMoyenneStr)));
        
        return noteMoyenne;
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

    public static Map<String, Object> getLogementAccepted(){
        List<Map<String, Object>> result = Database.select(
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
                LC."idProjetColoc" = """ + CurrentUser.getIdProjetColoc() + """
                AND LC."abandon" = false
                AND LC."offreAcceptee" = true
            """
        );
        if(result.size() == 0){
            return null;
        }
        return result.get(0);
    }

    public static void printPersonnesProjet(){
        Database.select(
            "idPersonne, prenom, nom, idProjetColoc",
            "Personne",
            "idProjetColoc=" + CurrentUser.getIdProjetColoc()
        ).forEach(personne -> {
            Display.print(personne.get("prenom") + " " + personne.get("nom"));
        });
    }

    // tests
    public static void main(String[] args) {
        // getNoteMoyenne
        // System.out.println(getNoteMoyenne("1201331392"));
        // getLogementAccepted
        Map<String, Object> logement = getLogementAccepted();
        Display.printLogement(logement);
    }

}
