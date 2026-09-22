package entity;

import java.time.LocalDate;
import java.util.UUID;

public class Paiement {

    private String idPaiement;
    private String idAbonnement;
    private LocalDate dateEcheance;
    private LocalDate datePaiement;
    private TypePaiement typePaiement;
    private StatutPaiement statut;

    public Paiement(String idAbonnement, LocalDate dateEcheance,
                    LocalDate datePaiement, TypePaiement typePaiement, StatutPaiement statut) {
        this.idPaiement = UUID.randomUUID().toString();
        this.idAbonnement = idAbonnement;
        this.dateEcheance = dateEcheance;
        this.datePaiement = datePaiement;
        this.typePaiement = typePaiement;
        this.statut = statut;
    }

    public String getIdPaiement() { return idPaiement; }
    public String getIdAbonnement() { return idAbonnement; }
    public LocalDate getDateEcheance() { return dateEcheance; }
    public LocalDate getDatePaiement() { return datePaiement; }
    public TypePaiement getTypePaiement() { return typePaiement; }
    public StatutPaiement getStatut() { return statut; }

    public void setDateEcheance(LocalDate dateEcheance) { this.dateEcheance = dateEcheance; }
    public void setDatePaiement(LocalDate datePaiement) { this.datePaiement = datePaiement; }
    public void setTypePaiement(TypePaiement typePaiement) { this.typePaiement = typePaiement; }
    public void setStatut(StatutPaiement statut) { this.statut = statut; }

   
    public static StatutPaiement calculerStatut(LocalDate datePaiement, LocalDate dateEcheance) {
        if (datePaiement != null) {
            return StatutPaiement.PAYE;
        } else if (dateEcheance != null && dateEcheance.isBefore(LocalDate.now())) {
            return StatutPaiement.EN_RETARD;
        } else {
            return StatutPaiement.NON_PAYE;
        }
    }

    @Override
    public String toString() {
        return "[" + idPaiement + "] Abonnement: " + idAbonnement
                + " | Echeance: " + dateEcheance + " | " + typePaiement + " | " + statut;
    }
}
