# Banque Baraka

Application bancaire Java permettant de gérer des clients, des comptes, des transactions et des rapports d'analyse dans un système bancaire simple.

## Description du projet

Le projet `Banque Baraka` est une application console développée en Java avec une architecture orientée objet. Elle permet de :

- ajouter des clients
- créer des comptes courants et comptes épargne
- effectuer des opérations bancaires (versement, retrait, virement)
- consulter l'historique d'un compte
- analyser les transactions
- détecter les comptes inactifs et les transactions suspectes
- afficher des alertes sur les soldes bas

## Stack technique

- Java 17+
- MySQL
- JDBC
- Maven/Gradle non utilisé (structure manuelle du projet)

## Structure du projet

```text
Banque_baraka/
├── lib/
│   └── mysql-connector-j-26.7.0.jar
├── src/
│   ├── dao/
│   ├── entity/
│   ├── service/
│   ├── ui/
│   └── util/
├── .gitignore
├── .vscode/
├── Readme.md
└── ...
```

## Prérequis

Avant de lancer le projet, assurez-vous d'avoir installé :

- Java JDK 17 ou plus
- MySQL Server
- Un client MySQL (Workbench, MySQL CLI, ou phpMyAdmin)

## Base de données

Le projet utilise une base MySQL nommée :

```sql
bank_baraka
```

Le fichier de connexion JDBC est configuré dans :

```java
src/util/DatabaseConnection.java
```

avec les paramètres suivants :

```java
private static final String URL = "jdbc:mysql://localhost:3306/bank_baraka";
private static final String USER = "root";
private static final String PASSWORD = "";
```

### Créer la base

Dans MySQL, exécutez :

```sql
CREATE DATABASE IF NOT EXISTS bank_baraka;
USE bank_baraka;
```

Si votre mot de passe MySQL est différent, modifiez la valeur `PASSWORD` dans `DatabaseConnection.java`.

## Compilation

Depuis le dossier racine du projet, exécutez les commandes suivantes dans un terminal Windows :

```powershell
mkdir out
Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName } | Out-File -Encoding UTF8 sources.txt
javac -cp "lib\mysql-connector-j-26.7.0.jar" -d out (Get-Content .\sources.txt)
```

## Exécution

```powershell
java -cp "out;lib\mysql-connector-j-26.7.0.jar" ui.Main
```

Le menu principal s'affichera alors avec les options suivantes :

1. Ajouter un client
2. Créer un compte
3. Effectuer une transaction
4. Consulter l'historique d'un compte
5. Lancer une analyse
6. Afficher les alertes des comptes
7. Afficher toutes les transactions
0. Quitter

## Fonctionnalités principales

### Gestion des clients

- ajout d'un client avec nom et email
- stockage des informations client

### Gestion des comptes

- comptes courants avec découvert autorisé
- comptes épargne avec taux d'intérêt
- recherche d'un compte par numéro

### Transactions

- versement
- retrait
- virement
- validation du solde disponible

### Rapports et analyses

- top 5 clients les plus riches
- transactions par type et par mois
- comptes inactifs
- transactions suspectes
- alertes de solde faible ou de comptes inactifs

## Remarques importantes

- La classe `Main` se trouve dans `src/ui/Main.java`.
- Le point d'entrée de l'application est :

```java
public static void main(String[] args) {
    MenuController menu = new MenuController();
    menu.demarrer();
}
```

- Le projet dépend de la bibliothèque JDBC MySQL présente dans `lib/`.

## Développeur

Projet développé dans le cadre d'une application bancaire Java orientée gestion et analyse.

## Licence

Ce projet est fourni à des fins de démonstration et de développement interne.
