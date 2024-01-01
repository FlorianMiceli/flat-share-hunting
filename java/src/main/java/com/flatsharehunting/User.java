package com.flatsharehunting;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.flatsharehunting.handleDatabase.Database;

public class User {

    /**
     * Create a user in the database
     * @param prenom
     * @param nom
     */
    public static void createUser(String prenom, String nom){
        Integer id = UUID.randomUUID().hashCode();
        System.out.println(id);

        Database.insert(
            "Personne", 
            "idPersonne, prenom, nom", 
            String.join(", ", id.toString(), "'"+prenom+"'", "'"+nom+"'")
        );
    }

    /**
     * Search for a user in db that matches params
     * @param prenom
     * @param nom
     * @return Map<String, Object> user with idPersonne, prenom, nom
     */
    public static Map<String, Object> getUser(String prenom, String nom){
        // get user from database
        String colomns = "idPersonne, prenom, nom";
        String tableName = "Personne";
        String condition = String.format("nom='%s' AND prenom='%s'", nom, prenom);
        List<Map<String, Object>> result = Database.select(colomns, tableName, condition);

        // check if not found or more than one user found
        if(result.size() < 1){throw new Error("No user found");} 
        if(result.size() > 1){throw new Error("More than one user found");}

        return result.get(0);
    }


    // tests
    public static void main(String[] args) {
        createUser("John", "Doe");
    }

}
