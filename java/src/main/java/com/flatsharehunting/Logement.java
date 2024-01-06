package com.flatsharehunting;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.flatsharehunting.handleDatabase.Database;

public class Logement {

    // /**
    //  * Get the type of a logement
    //  * @param idImmeuble
    //  * @return String type 'IM' or 'PA'
    //  */
    // public static String getType(String idImmeuble){
    //     List<Map<String, Object>> result = Database.select("typeImmeuble", "baseImmeuble91", "idImmeuble = " + idImmeuble);
    //     return result.get(0).get("typeImmeuble").toString();
    // }

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
     * Get the debit min and max of a logement
     * Randomly select a debit from the corresponding logement in ClasseDebit
     * The same logement will always have the same debit (Randomness is based on the idImmeuble)
     * @param idImmeuble String
     * @return Map<String, String> debitMin and debitMax
     */
    public static Map<String, String> getDebits(String idImmeuble){
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

    // tests
    public static void main(String[] args) {
        Map<String, String> debits = getDebits("16212883");
        System.out.println(debits.get("debitMin"));
        System.out.println(debits.get("debitMax"));
    }

}
