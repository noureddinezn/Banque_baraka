package dao;

import entity.Client;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    
    private Connection connection;

    public ClientDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public void ajouter(Client client) {
        String sql = "INSERT INTO client (nom, email) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.nom());
            stmt.setString(2, client.email());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur d'ajout : " + e.getMessage());
        }
    }

    public Client trouverParId(int id) {
        String sql = "SELECT * FROM client WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Client(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("email")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur de recherche : " + e.getMessage());
        }
        return null;
    }

    public List<Client> trouverTous() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                clients.add(new Client(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur de récupération : " + e.getMessage());
        }
        return clients;
    }

    public void supprimer(int id) {
        String sql = "DELETE FROM client WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur de suppression : " + e.getMessage());
        }
    }
}