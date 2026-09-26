package dao;

import entity.Compte;
import entity.CompteCourant;
import entity.CompteEpargne;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CompteDAO {

    private Connection connection;

    public CompteDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public void ajouter(Compte compte) {
        String sql = "INSERT INTO compte (numero, solde, id_client, type_compte, decouvert_autorise, taux_interet) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, compte.getNumero());
            stmt.setDouble(2, compte.getSolde());
            stmt.setInt(3, compte.getIdClient());

            if (compte instanceof CompteCourant cc) {
                stmt.setString(4, "COURANT");
                stmt.setDouble(5, cc.getDecouvertAutorise());
                stmt.setNull(6, Types.DOUBLE);
            } else if (compte instanceof CompteEpargne ce) {
                stmt.setString(4, "EPARGNE");
                stmt.setNull(5, Types.DOUBLE);
                stmt.setDouble(6, ce.getTauxInteret());
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur d'ajout : " + e.getMessage());
        }
    }

    public Compte trouverParId(int id) {
        String sql = "SELECT * FROM compte WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToCompte(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur de recherche : " + e.getMessage());
        }
        return null;
    }

    public Compte trouverParNumero(String numero) {
        String sql = "SELECT * FROM compte WHERE numero = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numero);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToCompte(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur de recherche : " + e.getMessage());
        }
        return null;
    }

    public List<Compte> trouverTous() {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT * FROM compte";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                comptes.add(mapResultSetToCompte(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur de récupération : " + e.getMessage());
        }
        return comptes;
    }

    public List<Compte> trouverParClient(int idClient) {
        List<Compte> comptes = new ArrayList<>();
        String sql = "SELECT * FROM compte WHERE id_client = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comptes.add(mapResultSetToCompte(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur de récupération : " + e.getMessage());
        }
        return comptes;
    }

    public void mettreAJourSolde(int id, double nouveauSolde) {
        String sql = "UPDATE compte SET solde = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, nouveauSolde);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur de mise à jour du solde : " + e.getMessage());
        }
    }

    public void supprimer(int id) {
        String sql = "DELETE FROM compte WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur de suppression : " + e.getMessage());
        }
    }

    private Compte mapResultSetToCompte(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String numero = rs.getString("numero");
        double solde = rs.getDouble("solde");
        int idClient = rs.getInt("id_client");
        String type = rs.getString("type_compte");

        if ("COURANT".equalsIgnoreCase(type)) {
            double decouvert = rs.getDouble("decouvert_autorise");
            return new CompteCourant(id, numero, solde, idClient, decouvert);
        } else if ("EPARGNE".equalsIgnoreCase(type)) {
            double taux = rs.getDouble("taux_interet");
            return new CompteEpargne(id, numero, solde, idClient, taux);
        }
        return null;
    }
}