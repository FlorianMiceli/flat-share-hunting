package com.flatsharehunting;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.flatsharehunting.handleDatabase.Database;

public class Logement {

    /**
     * Exemple : 1 bis rue de la paix, 75000 Paris
     * @param idImmeuble String
     * @return fullAdress String
     */
    public static String getFullAdress(Map<String, Object> logement){
        String fullAdress = "";
        if(!logement.get("numeroAdresse").toString().equals("")){
            fullAdress += logement.get("numeroAdresse").toString();
        } 
        if(!logement.get("repetitionAdresse").toString().equals("")){
            fullAdress += " " + logement.get("repetitionAdresse").toString();
        }
        fullAdress += " " + logement.get("nomVoieAdresse").toString();
        fullAdress += ", " + logement.get("codePostalAdresse").toString();
        fullAdress += " " + logement.get("nomCommuneAdresse").toString();
        return fullAdress;
    }

    /**
     * Get a random logement in a specific city, with a debit higher than debitMin and saturation to false
     * @param ville 
     * @param debitMin 
     * @return Map<String, Object> logement
     */
    public static Map<String, Object> getRandomLogement(String ville, Float debitMin){
        List<Map<String, Object>> result = Database.select(
            """
            SELECT bi."idImmeuble", bi."typeImmeuble", bi."numeroAdresse", bi."repetitionAdresse", bi."nomVoieAdresse", bi."codePostalAdresse", bi."nomCommuneAdresse", cd."debitMin", cd."debitMax"
            FROM "baseImmeuble91" bi
            JOIN "eligibiliteActuel91" ea ON bi."idImmeuble" = ea."idImmeuble"
            JOIN "ClasseDebit" cd ON ea."classeDebitDescendant" = cd."codeEligibilite"
            WHERE bi."nomCommuneAdresse" =  """ +"'"+ ville +"'"+ """
             AND cd."debitMin" >= """ + debitMin + """
             AND ea."saturation" != 't'
            ORDER BY RANDOM()
            """
        );
        return result.get(0);
    }

    /**
     * Get the debit criteria of the project
     * @return Float critereDebitMin
     */
    public static Float getDebitMinProjetColoc(){
        List<Map<String, Object>> result = Database.select(
            """
            SELECT "critereDebitMin"
            FROM "ProjetColoc"
            WHERE "idProjetColoc" = 
            """ + CurrentUser.getIdProjetColoc()
        );
        return Float.parseFloat(result.get(0).get("critereDebitMin").toString());
    }

    // tests
    public static void main(String[] args) {
        Map<String, Object> logement = getRandomLogement("Massy", 100.0f);
        Display.printLogement(logement);
    }

}
