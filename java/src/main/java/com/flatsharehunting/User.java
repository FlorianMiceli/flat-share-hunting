package com.flatsharehunting;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.flatsharehunting.handleDatabase.Database;

public class User {

    /**
     * Create a user if not already in db
     * @param prenom
     * @param nom
     * @return Map<String, Object> user with idPersonne, prenom, nom
     */
    public static Map<String, Object> createUser(String prenom, String nom){
        try {
            User.getUser(prenom, nom);
        } catch (Error e) {
            if(e.getMessage() == "No user found"){
                Integer id = Math.abs(UUID.randomUUID().hashCode());
                Database.insert(
                    "Personne", 
                    "idPersonne, prenom, nom", 
                    String.join(", ", id.toString(), "'"+prenom+"'", "'"+nom+"'")
                );
                Map<String, Object> newUser = User.getUser(prenom, nom);
                return newUser;
            }
        }
        throw new Error(Display.red("Déjà inscrit"));
    }

    /**
     * Search for a user in db that matches params
     * @param prenom
     * @param nom
     * @return Map<String, Object> user with idPersonne, prenom, nom
     */
    public static Map<String, Object> getUser(String prenom, String nom){
        // get user from database
        String colomns = "idPersonne, prenom, nom, idProjetColoc";
        String tableName = "Personne";
        String condition = String.format("nom='%s' AND prenom='%s'", nom, prenom);
        List<Map<String, Object>> result = Database.select(colomns, tableName, condition);


        // check if not found or more than one user found
        if(result.size() < 1){throw new Error("No user found");} 
        if(result.size() > 1){throw new Error("More than one user found");}

        return result.get(0);
    }

    /**
     * Search for a user in db that matches params
     * @param idPersonne
     * @return Map<String, Object> user with idPersonne, prenom, nom
     */
    public static Map<String, Object> getUser(Integer idPersonne){
        // get user from database
        String colomns = "idPersonne, prenom, nom";
        String tableName = "Personne";
        String condition = String.format("idPersonne='%s'", idPersonne);
        List<Map<String, Object>> result = Database.select(colomns, tableName, condition);

        // check if not found or more than one user found
        if(result.size() < 1){throw new Error(Display.red("No user found"));} 
        if(result.size() > 1){throw new Error(Display.red("More than one user found"));}

        return result.get(0);
    }

    /**
     * Set idProjetColoc in db
     * @param idPersonneToAdd
     * @param idProjetColoc
     */
    public static void setIdProjetColoc(Integer idPersonneToAdd, Integer idProjetColoc) {
        Database.update(
            "Personne", 
            "idProjetColoc", 
            idProjetColoc.toString(),
            String.format("idPersonne='%s'", idPersonneToAdd)
        );
    }
    // tests
    public static void main(String[] args) {
        createUser("John", "Doe");
    }

}
