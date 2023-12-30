package com.flatsharehunting.handleDatabase;

import java.io.FileReader;
import com.opencsv.CSVReader;

// exceptions handling
import java.io.IOException;
import com.opencsv.exceptions.CsvValidationException;

// code that fills the database with data from CSV files
// executing was too long, so I used DBVisualizer to import data from CSV files to database.db
public class FillDatabaseWithCSVs {
    
    // csv to eligibiliteActuel91
    public static void insertEligibilteActuel() throws IOException, CsvValidationException {
        String csvFilePath = "data/eligibilite_actuel_91.csv";
        
        CSVReader reader = new CSVReader(new FileReader(csvFilePath));
        reader.readNext();

        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            String[] colomns = nextLine[0].split(";");
            
            String idImmeuble = colomns[0];
            String classeDebitMontant = "'"+colomns[5]+"'";
            String classeDebitDescendant = "'"+colomns[6]+"'";
            String saturation = (colomns.length < 9 || colomns[8]=="f") ? "false" : "true";

            Database.insert(
                "eligibiliteActuel91", "idImmeuble, classeDebitMontant, classeDebitDescendant, saturation", 
                String.join(", ", idImmeuble, classeDebitMontant, classeDebitDescendant, saturation)
            );
        }

        reader.close();
    }

    // csv to baseImmeuble91
    public static void insertBaseImmeuble() throws CsvValidationException, IOException{
        String csvFilePath = "data/base_imb_91.csv";

        CSVReader reader = new CSVReader(new FileReader(csvFilePath));
        reader.readNext();

        String[] nextLine;
        while((nextLine = reader.readNext()) != null){
            String[] colomns = nextLine[0].split(";");

            String idImmeuble = colomns[0];
            String typeImmeuble = "'"+colomns[7]+"'";
            String numeroAdresse = colomns[10];
            String repetitionAdresse = "'"+colomns[11]+"'";
            String nomVoieAdresse = "'"+colomns[12]+"'";
            String codePostalAdresse = colomns[14];
            String nomCommuneAdresse = "'"+colomns[15]+"'";

            Database.insert(
                "baseImmeuble91", "idImmeuble, typeImmeuble, numeroAdresse, repetitionAdresse, nomVoieAdresse, codePostalAdresse, nomCommuneAdresse",
                String.join(", ", idImmeuble, typeImmeuble, numeroAdresse, repetitionAdresse, nomVoieAdresse, codePostalAdresse, nomCommuneAdresse)
            );
        }

        reader.close();
    } 

    public static void main(String[] args) throws CsvValidationException, IOException {
        insertEligibilteActuel();
        insertBaseImmeuble();
    }
}