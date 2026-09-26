package service;

import entity.Client;
import entity.Compte;
import entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RapportService {
    
    private ClientService clientService;
    private CompteService compteService;
    private TransactionService transactionService;

    public RapportService() {
        this.clientService = new ClientService();
        this.compteService = new CompteService();
        this.transactionService = new TransactionService();
    }

    public List<Client> getTop5ClientsParSolde() {
        Map<Integer, Double> soldesParClient = compteService.listerTousLesComptes().stream()
                .collect(Collectors.groupingBy(Compte::getIdClient, Collectors.summingDouble(Compte::getSolde)));

        return clientService.listerTousLesClients().stream()
                .sorted((c1, c2) -> Double.compare(
                        soldesParClient.getOrDefault(c2.id(), 0.0),
                        soldesParClient.getOrDefault(c1.id(), 0.0)
                ))
                .limit(5)
                .toList();
    }

    public double calculerTotalTransactionsParMois(int annee, int mois) {
        return transactionService.listerToutesLesTransactions().stream()
                .filter(t -> t.dateTransaction().getYear() == annee)
                .filter(t -> t.dateTransaction().getMonthValue() == mois)
                .mapToDouble(Transaction::montant)
                .sum();
    }

    public long compterTransactionsParMois(int annee, int mois) {
        return transactionService.listerToutesLesTransactions().stream()
                .filter(t -> t.dateTransaction().getYear() == annee)
                .filter(t -> t.dateTransaction().getMonthValue() == mois)
                .count();
    }

    public List<Transaction> detecterTransactionsSuspectes(double seuilMax) {
        return transactionService.listerToutesLesTransactions().stream()
                .filter(t -> t.montant() > seuilMax)
                .toList();
    }

    public List<Compte> detecterComptesInactifs(int joursInactivite) {
        return compteService.listerTousLesComptes().stream()
                .filter(c -> transactionService.listerToutesLesTransactions().stream()
                        .filter(t -> t.idCompte() == c.getId())
                        .noneMatch(t -> t.dateTransaction().isAfter(LocalDateTime.now().minusDays(joursInactivite))))
                .toList();
    }
}