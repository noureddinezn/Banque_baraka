package service;

import dao.ClientDAO;
import entity.Client;

import java.util.List;

public class ClientService {
    private ClientDAO clientDAO;

    public ClientService() {
        this.clientDAO = new ClientDAO();
    }

    public void ajouterClient(Client client) {
        clientDAO.ajouter(client);
    }

    public Client trouverClientParId(int id) {
        return clientDAO.trouverParId(id);
    }

    public List<Client> listerTousLesClients() {
        return clientDAO.trouverTous();
    }

    public void supprimerClient(int id) {
        clientDAO.supprimer(id);
    }
}