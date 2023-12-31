package com.flatsharehunting.handleDatabase;

public class FillDatabase {

    public static void insertClasseDebit(){
        Database.insert("classeDebit", "codeEligibilite, debitMax, debitMin","'INEL'  , 0.512, 0    ");
        Database.insert("classeDebit", "codeEligibilite, debitMax, debitMin","'HD05'  , 4    , 0.512");
        Database.insert("classeDebit", "codeEligibilite, debitMax, debitMin","'HD3'   , 10   , 2    ");
        Database.insert("classeDebit", "codeEligibilite, debitMax, debitMin","'BHD8'  , 25   , 6    ");
        Database.insert("classeDebit", "codeEligibilite, debitMax, debitMin","'THD30' , 100  , 20   ");
        Database.insert("classeDebit", "codeEligibilite, debitMax, debitMin","'THD100', 1000 , 100  ");
        Database.insert("classeDebit", "codeEligibilite, debitMax, debitMin","'THD1G' , 10000, 1000 ");
    }

    public static void main(String[] args) {
        insertClasseDebit();
    }
}
