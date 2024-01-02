package com.flatsharehunting;

import java.util.UUID;

import com.flatsharehunting.handleDatabase.Database;

public class Project {

    /**
     * Create a project in the database
     * @return Integer idProjetColoc
     */
    public static Integer createProject() {
        Integer idProjetColoc = UUID.randomUUID().hashCode();
        Database.insert(
            "ProjetColoc", 
            "idProjetColoc", 
            idProjetColoc.toString()
        );
        return idProjetColoc;
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

    // tests
    public static void main(String[] args) {
        createProject();
    }

}
