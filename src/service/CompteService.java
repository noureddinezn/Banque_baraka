package service;

import dao.CompteDAO;
import entity.Compte;

import java.util.List;

public class CompteService {
    private CompteDAO compteDAO;

    public CompteService() {
        this.compteDAO = new CompteDAO();
    }

    public void creerCompte(Compte compte) {
        compteDAO.ajouter(compte);
    }

    public Compte trouverCompteParNumero(String numero) {
        return compteDAO.trouverParNumero(numero);
    }

    public Compte trouverCompteParId(int id) {
        return compteDAO.trouverParId(id);
    }

    public List<Compte> listerComptesParClient(int idClient) {
        return compteDAO.trouverParClient(idClient);
    }

    public List<Compte> listerTousLesComptes() {
        return compteDAO.trouverTous();
    }

    public void mettreAJourSolde(int id, double nouveauSolde) {
        compteDAO.mettreAJourSolde(id, nouveauSolde);
    }
}