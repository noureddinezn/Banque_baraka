package ui;

import entity.Client;
import entity.CompteCourant;
import entity.CompteEpargne;
import service.ClientService;
import service.CompteService;
import service.RapportService;
import service.TransactionService;

import java.util.Scanner;

public class MenuController {
    private Scanner scanner;
    private ClientService clientService;
    private CompteService compteService;
    private TransactionService transactionService;
    private RapportService rapportService;

    public MenuController() {
        this.scanner = new Scanner(System.in);
        this.clientService = new ClientService();
        this.compteService = new CompteService();
        this.transactionService = new TransactionService();
        this.rapportService = new RapportService();
    }

    public void demarrer() {
        int choix;
        do {
            System.out.println("\n=== Systeme Bancaire - Banque Baraka ===");
            System.out.println("1. Ajouter un client");
            System.out.println("2. Creer un compte");
            System.out.println("3. Effectuer une transaction");
            System.out.println("4. Afficher le Top 5 des clients");
            System.out.println("5. Afficher toutes les transactions");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");
            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1 -> ajouterClient();
                case 2 -> creerCompte();
                case 3 -> gererTransactions();
                case 4 -> afficherTopClients();
                case 5 -> afficherTransactions();
                case 0 -> System.out.println("Fermeture de l'application...");
                default -> System.out.println("Choix invalide.");
            }
        } while (choix != 0);
    }

    private void ajouterClient() {
        System.out.print("Nom du client : ");
        String nom = scanner.nextLine();
        System.out.print("Email du client : ");
        String email = scanner.nextLine();
        clientService.ajouterClient(new Client(0, nom, email));
        System.out.println("Client ajoute avec succes.");
    }

    private void creerCompte() {
        System.out.print("ID du client : ");
        int idClient = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Numero de compte (ex: RIB123) : ");
        String numero = scanner.nextLine();
        System.out.print("Solde initial : ");
        double solde = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.print("Type de compte (1. Courant, 2. Epargne) : ");
        int type = scanner.nextInt();
        scanner.nextLine();

        if (type == 1) {
            System.out.print("Decouvert autorise : ");
            double decouvert = scanner.nextDouble();
            compteService.creerCompte(new CompteCourant(0, numero, solde, idClient, decouvert));
            System.out.println("Compte courant cree avec succes.");
        } else if (type == 2) {
            System.out.print("Taux d'interet : ");
            double taux = scanner.nextDouble();
            compteService.creerCompte(new CompteEpargne(0, numero, solde, idClient, taux));
            System.out.println("Compte epargne cree avec succes.");
        }
    }

    private void gererTransactions() {
        System.out.println("1. Versement | 2. Retrait | 3. Virement");
        System.out.print("Choix de l'operation : ");
        int choix = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Numero du compte (Source) : ");
        String numero = scanner.nextLine();
        System.out.print("Montant : ");
        double montant = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Lieu de l'operation : ");
        String lieu = scanner.nextLine();

        boolean succes = false;
        if (choix == 1) {
            succes = transactionService.effectuerVersement(numero, montant, lieu);
        } else if (choix == 2) {
            succes = transactionService.effectuerRetrait(numero, montant, lieu);
        } else if (choix == 3) {
            System.out.print("Numero du compte (Destination) : ");
            String numeroDest = scanner.nextLine();
            succes = transactionService.effectuerVirement(numero, numeroDest, montant, lieu);
        }

        if (succes) {
            System.out.println("Operation effectuee avec succes.");
        } else {
            System.out.println("Echec de l'operation (Verifiez le numero de compte ou le solde).");
        }
    }

    private void afficherTopClients() {
        System.out.println("\n--- Top 5 des clients les plus riches ---");
        rapportService.getTop5ClientsParSolde().forEach(c -> 
            System.out.println("- " + c.nom() + " (" + c.email() + ")")
        );
    }

    private void afficherTransactions() {
        System.out.println("\n--- Toutes les transactions ---");
        transactionService.listerToutesLesTransactions().forEach(t ->
            System.out.printf("- ID: %d | Compte: %d | Type: %s | Montant: %.2f | Lieu: %s | Date: %s%n",
                t.id(), t.idCompte(), t.type(), t.montant(), t.lieu(), t.dateTransaction())
        );
    }
}