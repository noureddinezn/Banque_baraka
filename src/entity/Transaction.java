package entity;

import java.time.LocalDateTime;

public record Transaction(
    int id,
    LocalDateTime dateTransaction,
    double montant,
    TypeTransaction type,
    String lieu,
    int idCompte
) {}