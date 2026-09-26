package dao;

import entity.Transaction;
import entity.TypeTransaction;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    private Connection connection;

    public TransactionDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public void ajouter(Transaction transaction) {
        String sql = "INSERT INTO transaction (date_transaction, montant, type, lieu, id_compte) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, transaction.dateTransaction());
            stmt.setDouble(2, transaction.montant());
            stmt.setString(3, transaction.type().name());
            stmt.setString(4, transaction.lieu());
            stmt.setInt(5, transaction.idCompte());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur d'ajout : " + e.getMessage());
        }
    }

    public List<Transaction> trouverParCompte(int idCompte) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transaction WHERE id_compte = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCompte);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur de récupération : " + e.getMessage());
        }
        return transactions;
    }

    public List<Transaction> trouverTous() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transaction";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur de récupération : " + e.getMessage());
        }
        return transactions;
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        return new Transaction(
            rs.getInt("id"),
            rs.getObject("date_transaction", LocalDateTime.class),
            rs.getDouble("montant"),
            TypeTransaction.valueOf(rs.getString("type")),
            rs.getString("lieu"),
            rs.getInt("id_compte")
        );
    }
}