package com.flatsharehunting;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
     * Get the debit min and max of a logement
     * Randomly select a debit from the corresponding logement in ClasseDebit
     * The same logement will always have the same debit (Randomness is based on the idImmeuble)
     * @param idImmeuble String
     * @return Map<String, String> debitMin and debitMax
     */
    public static Map<String,String> getDebits(String idImmeuble){
        List<Map<String, Object>> result = Database.select(
            """
            SELECT cd."debitMin", cd."debitMax"
            FROM "baseImmeuble91" bi
            JOIN "eligibiliteActuel91" ea ON bi."idImmeuble" = ea."idImmeuble"
            JOIN "ClasseDebit" cd ON ea."classeDebitMontant" = cd."codeEligibilite"
            WHERE bi."idImmeuble" = 
            """ + idImmeuble
        );
        // Get a random debit, but the same logement always have the same debit
        // To do that, we generate a random seed from the idImmeuble
        // The same idImmeuble will always have the same seed
        Random seed = new Random(Integer.parseInt(idImmeuble));
        Integer randomIndex = seed.nextInt(result.size());
        Map<String, Object> debit = result.get(randomIndex);
        return Map.of(
            "debitMin", debit.get("debitMin").toString(),
            "debitMax", debit.get("debitMax").toString()
        );
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

    /**
     * Get the average debit min of a street, takes every logement into account
     * @param nomVoieAdresse
     * @return Float averageDebitMin #.# format
     */
    public static Float getAverageDebitMinInStreet(String nomVoieAdresse) {
        String result = Database.select(
            """
            SELECT AVG(cd."debitMin") AS avg
            FROM "baseImmeuble91" bi
            JOIN "eligibiliteActuel91" ea ON bi."idImmeuble" = ea."idImmeuble"
            JOIN "ClasseDebit" cd ON ea."classeDebitDescendant" = cd."codeEligibilite"
            WHERE bi."nomVoieAdresse" =
            """ + "'" + nomVoieAdresse + "'"
        ).get(0).get("avg").toString();
        Float res = Float.parseFloat(result);
        DecimalFormat df = new DecimalFormat("#.#");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        Float averageDebitMin = Float.parseFloat(df.format(res));
        return averageDebitMin;
    }

    // tests
    public static void main(String[] args) {
        Map<String, Object> logement = getRandomLogement("Massy", 100.0f);
        Display.printLogement(logement);
    }

}
