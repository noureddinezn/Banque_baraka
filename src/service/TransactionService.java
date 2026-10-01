package service;

import dao.TransactionDAO;
import entity.Compte;
import entity.CompteCourant;
import entity.Transaction;
import entity.TypeTransaction;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TransactionService {
    private TransactionDAO transactionDAO;
    private CompteService compteService;

    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
        this.compteService = new CompteService();
    }

    public boolean effectuerVersement(String numeroCompte, double montant, String lieu) {
        return Optional.of(montant)
                .filter(m -> m > 0)
                .flatMap(m -> Optional.ofNullable(compteService.trouverCompteParNumero(numeroCompte)))
                .map(compte -> {
                    compteService.mettreAJourSolde(compte.getId(), compte.getSolde() + montant);
                    transactionDAO.ajouter(new Transaction(0, LocalDateTime.now(), montant, TypeTransaction.VERSEMENT, lieu, compte.getId()));
                    return true;
                })
                .orElse(false);
    }

    public boolean effectuerRetrait(String numeroCompte, double montant, String lieu) {
        return Optional.of(montant)
                .filter(m -> m > 0)
                .flatMap(m -> Optional.ofNullable(compteService.trouverCompteParNumero(numeroCompte)))
                .filter(compte -> peutRetirer(compte, montant))
                .map(compte -> {
                    compteService.mettreAJourSolde(compte.getId(), compte.getSolde() - montant);
                    transactionDAO.ajouter(new Transaction(0, LocalDateTime.now(), montant, TypeTransaction.RETRAIT, lieu, compte.getId()));
                    return true;
                })
                .orElse(false);
    }

    public boolean effectuerVirement(String numeroSource, String numeroDest, double montant, String lieu) {
        return Optional.of(montant)
                .filter(m -> m > 0)
                .flatMap(m -> Optional.ofNullable(compteService.trouverCompteParNumero(numeroSource)))
                .filter(source -> peutRetirer(source, montant))
                .flatMap(source -> Optional.ofNullable(compteService.trouverCompteParNumero(numeroDest))
                        .map(dest -> {
                            compteService.mettreAJourSolde(source.getId(), source.getSolde() - montant);
                            compteService.mettreAJourSolde(dest.getId(), dest.getSolde() + montant);
                            transactionDAO.ajouter(new Transaction(0, LocalDateTime.now(), montant, TypeTransaction.VIREMENT, lieu, source.getId()));
                            transactionDAO.ajouter(new Transaction(0, LocalDateTime.now(), montant, TypeTransaction.VIREMENT, lieu, dest.getId()));
                            return true;
                        }))
                .orElse(false);
    }

    public List<Transaction> listerToutesLesTransactions() {
        return transactionDAO.trouverTous();
    }

    public List<Transaction> listerTransactionsParCompte(int idCompte) {
        return listerToutesLesTransactions().stream()
                .filter(t -> t.idCompte() == idCompte)
                .toList();
    }

    private boolean peutRetirer(Compte compte, double montant) {
        if (compte instanceof CompteCourant cc) {
            return (cc.getSolde() + cc.getDecouvertAutorise()) >= montant;
        }
        return compte.getSolde() >= montant;
    }

    

}