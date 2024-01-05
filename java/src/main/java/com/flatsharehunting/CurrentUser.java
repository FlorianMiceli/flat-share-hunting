package com.flatsharehunting;

import java.util.Map;

public class CurrentUser {

    private static Integer idPersonne;
    private static String prenom;
    private static String nom;
    private static Integer idProjetColoc;

    public static Integer getIdPersonne() {
        return idPersonne;
    }

    public static String getNom() {
        return nom;
    }

    public static String getPrenom() {
        return prenom;
    }

    public static Integer getIdProjetColoc() {
        return idProjetColoc;
    }

    /**
     * Logs a user
     * @param user Map<String, Object>, use getUser
     */
    public static void setCurrentUser(Map<String, Object> user){
        CurrentUser.idPersonne = (Integer) user.get("idPersonne");
        CurrentUser.prenom = (String) user.get("prenom");
        CurrentUser.nom = (String) user.get("nom");
        if (user.containsKey("idProjetColoc")) {
            CurrentUser.idProjetColoc = (Integer) user.get("idProjetColoc");
        }
    }

    /**
     * Set idProjetColoc in CurrentUser and db
     * @param idProjetColoc
     * @param idPersonneToAdd
     */
    public static void setIdProjetColoc(Integer idPersonneToAdd, Integer idProjetColoc) {
        CurrentUser.idProjetColoc = idProjetColoc;
        User.setIdProjetColoc(idPersonneToAdd, idProjetColoc);
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
