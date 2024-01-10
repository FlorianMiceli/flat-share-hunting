# Flat share hunting 🏠 - WIP
A third year school project for the Java module.

# Table of contents

- [Project Overview and Objective](#readme-wip)
- [Technical details](#technical-details)
  - [General informations](#general-informations)
  - [Details about project structure](#details-about-project-structure)
- [Insallation and usage](#insallation-and-usage)

# Project Overview and Objective

Using data from ["Ma connexion internet"](https://maconnexioninternet.arcep.fr/), the goal was to build an app to help people find a flat share. The main criteria is the internet speed. User should be able to create a flat-sharing project, add criterias (minimum internet speed and city location) and his friends. Then the group can add places to a shared list, vote for the best ones with stars, plan visits, and finally accept a place as their new flat share.

> **Skip to [Installation and usage](#installation-and-usage) to try it**. 

# Technical details

## General informations

Data comes from [data.gouv.fr](https://www.data.gouv.fr/en/datasets/ma-connexion-internet/). The project uses only the data of the [Essonne department (91)](https://en.wikipedia.org/wiki/Essonne) for query speed purposes. Data is stored in a SQLite database. Table "baseImmeuble91" contains informations about the type of building, the number of appartments, their adress, etc. Table "baseEligibilite91" contains informations about the internet speed, the type of internet connection, the provider, etc. More information the [official documentation](https://www.data.gouv.fr/fr/datasets/r/8c21e6a5-ebcb-4eaf-b835-a687abce248d) Database in `./data` needs to be decompressed before use, and here is its model :

The project is built with Maven. Dependencies are listed in `./pom.xml`. This project uses : 
-  [SQLite JDBC driver](https://github.com/xerial/sqlite-jdbc) to handle the database. 
- [Consoleui](https://github.com/awegmann/consoleui) to have a basic but pleasant interface in the terminal.


Even though I restricted the data to the Essonne department, the database is still quite big. It contains 300k+ rows. So querying it can take some time. I used [DBVisualizer](https://www.dbvis.com/) to help me handle the database. 

Javadoc of all methods available in `./java/target/site/apidocs/index.html`.

## Details about project structure

The project is divided in 2 packages at `./java/src/main/java/com/flatsharehunting` :

- `com.flatsharehunting.handleDatabase` contains all the classes to handle the database : 
    - `Database`
    - `CreateDatabase` - only for development purposes
    - `FillDatabase` - only for development purposes
    - `FillDatabaseWithCSVs` - deprecated because used DBVisualizer instead

- `com.flatsharehunting` contains all the other classes : 
    - `Main` - the main class, ***everything starts here***
    - `Event` - group actions and "menus"
    - `Display` - print methods
    - `Logement` - interaction with logements elements in db
    - `Project` - interaction with project elements in db
    - `User` - interaction with user elements in db

# notes
- maven
- sqlite-jbdc 
    - https://github.com/xerial/sqlite-jdbc
    - https://github.com/xerial/sqlite-jdbc/blob/master/USAGE.md
- consoleUI
- db to decompress 
- db structure sur drawsql
- .jar usage
- emojis [tutorial](https://akr.am/blog/posts/using-utf-8-in-the-windows-terminal)
- javadoc

- handleDatabase is built by following [this tutorial](https://www.sqlitetutorial.net/sqlite-java/)
- console ui usage is descriped [here](https://github.com/awegmann/consoleui/blob/master/doc/howto.md)

# Insallation and usage

- todo

- handleDatabase is built by following [this tutorial](https://www.sqlitetutorial.net/sqlite-java/)
- console ui usage is descriped [here](https://github.com/awegmann/consoleui/blob/master/doc/howto.md)

# Demo


# Login
<img width="958" alt="image" src="https://github.com/FlorianMiceli/flat-share-hunting/assets/103659071/252c8486-dfbe-4601-bc56-45fb2637093d">


# voir les logements
<img width="958" alt="image" src="https://github.com/FlorianMiceli/flat-share-hunting/assets/103659071/f4316a85-e21b-4d67-b2aa-04baf7474768">

#options
<img width="958" alt="image" src="https://github.com/FlorianMiceli/flat-share-hunting/assets/103659071/fe88f4b7-80b2-43a7-865c-66ffeaaa41ae">

#ajouter un logmenet
<img width="958" alt="image" src="https://github.com/FlorianMiceli/flat-share-hunting/assets/103659071/73ddff0a-4faa-45a1-96c0-0841bfe1a6ef">

# Créer proj critere
<img width="958" alt="image" src="https://github.com/FlorianMiceli/flat-share-hunting/assets/103659071/5c4da76e-1e31-4e6b-a984-d01c68ac3f21">

# logmenet accepte
<img width="959" alt="image" src="https://github.com/FlorianMiceli/flat-share-hunting/assets/103659071/67c64168-3b98-4a4f-bff9-7667636861c1">


# javadoc