-- Base Immeuble 91
CREATE TABLE "baseImmeuble91" (
    "idImmeuble" INTEGER PRIMARY KEY NOT NULL,
    "typeImmeuble" TEXT NOT NULL,
    "numeroAdresse" INTEGER,
    "repetitionAdresse" TEXT,
    "nomVoieAdresse" TEXT NOT NULL,
    "codePostalAdresse" TEXT NOT NULL,
    "nomCommuneAdresse" TEXT NOT NULL
);

-- EligibiliteActuel91
CREATE TABLE "eligibiliteActuel91" (
    "idImmeuble" INTEGER NOT NULL,
    "classeDebitMontant" TEXT NOT NULL,
    "classeDebitDescendant" TEXT NOT NULL,
    "saturation" INTEGER NOT NULL,
    FOREIGN KEY ("idImmeuble") REFERENCES "baseImmeuble91" ("idImmeuble"),
    FOREIGN KEY ("classeDebitMontant") REFERENCES "ClasseDebit" ("codeEligibilite"),
    FOREIGN KEY ("classeDebitDescendant") REFERENCES "ClasseDebit" ("codeEligibilite")
);

-- Personne
CREATE TABLE "Personne" (
    "idPersonne" INTEGER PRIMARY KEY NOT NULL,
    "nom" TEXT NOT NULL,
    "prenom" TEXT NOT NULL,
    "idProjetColoc" INTEGER,
    FOREIGN KEY ("idProjetColoc") REFERENCES "ProjetColoc" ("idProjetColoc")
);

-- LogementColoc
CREATE TABLE "LogementColoc" (
    "idLogementColoc" INTEGER PRIMARY KEY NOT NULL,
    "idImmeuble" INTEGER NOT NULL,
    "idProjetColoc" TEXT NOT NULL,
    "idPersonneAjout" TEXT NOT NULL,
    "dateVisite" TEXT NOT NULL,
    "offreAcceptee" BOOLEAN NOT NULL,
    "abandon" BOOLEAN NOT NULL,
    FOREIGN KEY ("idPersonneAjout") REFERENCES "Personne" ("idPersonne"),
    FOREIGN KEY ("idProjetColoc") REFERENCES "ProjetColoc" ("idProjetColoc"),
    FOREIGN KEY ("idImmeuble") REFERENCES "baseImmeuble91" ("idImmeuble")
);

-- ClasseDebit
CREATE TABLE "ClasseDebit" (
    "codeEligibilite" TEXT PRIMARY KEY NOT NULL,
    "debitMax" REAL NOT NULL,
    "debitMin" REAL NOT NULL
);

-- ProjetColoc
CREATE TABLE "ProjetColoc" (
    "idProjetColoc" TEXT PRIMARY KEY NOT NULL,
    "critereDebitMin" REAL NOT NULL,
    "critereVille" TEXT NOT NULL
);

-- VoteLogement
CREATE TABLE "VoteLogement"(
        "idImmeuble" INTEGER NOT NULL,
        "idPersonne" INTEGER NOT NULL,
        "note" INTEGER NOT NULL,
        FOREIGN KEY ("idImmeuble") REFERENCES "LogementColoc" ("idImmeuble")
);