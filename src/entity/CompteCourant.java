package entity;

public class CompteCourant extends Compte {
    private double decouvertAutorise;

    public CompteCourant(int id, String numero, double solde, int idClient, double decouvertAutorise) {
        super(id, numero, solde, idClient);
        this.decouvertAutorise = decouvertAutorise;
    }

}
