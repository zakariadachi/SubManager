package entity;

import java.time.LocalDate;

public class AbonnementSansEngagement extends Abonnement {

    public AbonnementSansEngagement(String id, String nomService, double montantMensuel,
                                    LocalDate dateDebut, LocalDate dateFin, StatutAbonnement statut) {
        super(id, nomService, montantMensuel, dateDebut, dateFin, statut);
    }

    @Override
    public String toString() {
        return super.toString() + " | Sans engagement";
    }
}
