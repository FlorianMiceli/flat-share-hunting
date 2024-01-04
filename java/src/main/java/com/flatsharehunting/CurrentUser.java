package com.flatsharehunting;

import java.util.Map;

public class CurrentUser {

    private static Integer idPersonne;
    private static String prenom;
    private static String nom;

    public static Integer getIdPersonne() {
        return idPersonne;
    }

    public static String getNom() {
        return nom;
    }

    public static String getPrenom() {
        return prenom;
    }

    /**
     * Logs a user
     * @param user Map<String, Object>, use getUser
     */
    public static void setCurrentUser(Map<String, Object> user){
        CurrentUser.idPersonne = (Integer) user.get("idPersonne");
        CurrentUser.prenom = (String) user.get("prenom");
        CurrentUser.nom = (String) user.get("nom");
    }

    // tests
    public static void main(String[] args) {
        //setCurrentUser
        Map<String, Object> user = User.getUser("John", "Doe");
        setCurrentUser(user);
        System.out.println(getIdPersonne());
        System.out.println(getPrenom());
        System.out.println(getNom());
    }

}
