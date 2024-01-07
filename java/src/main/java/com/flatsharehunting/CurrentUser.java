package com.flatsharehunting;

import java.util.List;
import java.util.Map;

import com.flatsharehunting.handleDatabase.Database;

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

    /**
     * Rate a logement of project
     * @param idLogementColoc
     * @param note
     */
    public static void rateLogementColoc(String idLogementColoc, Integer note){
        if(getNoteForLogement(idLogementColoc) != null){
            throw new Error("Vous avez déjà noté ce logement");
        }
        Database.insert(
            "NoteLogement",
            "idPersonne, idLogementColoc, note",
            String.format("%s, %s, %s", CurrentUser.getIdPersonne(), idLogementColoc, note)
        );
    }

    public static String getNoteForLogement(String idLogementColoc){
        List<Map<String, Object>> result = Database.select(
            "note",
            "NoteLogement",
            String.format("idPersonne=%s AND idLogementColoc=%s", CurrentUser.getIdPersonne(), idLogementColoc)
        );
        if(result.size() == 0){
            return null;
        }
        return result.get(0).get("note").toString();
    }

    // tests
    public static void main(String[] args) {
        rateLogementColoc("689396812", 5);
    }

}
