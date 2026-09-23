package entity;

public abstract class Compte {
    private int id;
    private String numero;
    private double solde;
    private int idClient;

    public Compte(int id, String numero, double solde, int idClient) {
        this.id = id;
        this.numero = numero;
        this.solde = solde;
        this.idClient = idClient;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public double getSolde() { return solde; }
    public void setSolde(double solde) { this.solde = solde; }

    public int getIdClient() { return idClient; }
    public void setIdClient(int idClient) { this.idClient = idClient; }
}
