package entity;

import java.time.LocalDate;

public class AbonnementAvecEngagement extends Abonnement {

    private int dureeEngagementMois;

    public AbonnementAvecEngagement(String id, String nomService, double montantMensuel,
                                    LocalDate dateDebut, LocalDate dateFin,
                                    StatutAbonnement statut, int dureeEngagementMois) {
        super(id, nomService, montantMensuel, dateDebut, dateFin, statut);
        this.dureeEngagementMois = dureeEngagementMois;
    }

    public int getDureeEngagementMois() { return dureeEngagementMois; }
    public void setDureeEngagementMois(int dureeEngagementMois) { this.dureeEngagementMois = dureeEngagementMois; }

    @Override
    public String toString() {
        return super.toString() + " | Engagement: " + dureeEngagementMois + " mois";
    }
}
