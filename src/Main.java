import dao.AbonnementDAO;
import dao.PaiementDAO;
import entity.AbonnementAvecEngagement;
import entity.AbonnementSansEngagement;
import entity.StatutAbonnement;
import service.AbonnementService;
import service.PaiementService;
import ui.Menu;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Main {

    public static void main(String[] args) {
        AbonnementDAO abonnementDAO = new AbonnementDAO();
        PaiementDAO paiementDAO = new PaiementDAO();
        AbonnementService abonnementService = new AbonnementService(abonnementDAO, paiementDAO);
        PaiementService paiementService = new PaiementService(paiementDAO, abonnementDAO);

        // --- Donnees de test ---

        // Abonnement 1 : Netflix avec engagement (3 mois)
        String id1 = "netflix-test-id";
        LocalDate debut1 = LocalDate.of(2024, 1, 1);
        LocalDate fin1   = LocalDate.of(2024, 4, 1);
        int duree1 = (int) ChronoUnit.MONTHS.between(debut1, fin1);
        double montant1 = 300.0 / duree1;
        abonnementService.creerAbonnement(
                new AbonnementAvecEngagement(id1, "Netflix", montant1, debut1, fin1, StatutAbonnement.ACTIVE, duree1));
        abonnementService.genererEcheances(id1);

        // Abonnement 2 : Spotify sans engagement (2 mois)
        String id2 = "spotify-test-id";
        LocalDate debut2 = LocalDate.of(2024, 1, 1);
        LocalDate fin2   = LocalDate.of(2024, 3, 1);
        int duree2 = (int) ChronoUnit.MONTHS.between(debut2, fin2);
        double montant2 = 140.0 / duree2;
        abonnementService.creerAbonnement(
                new AbonnementSansEngagement(id2, "Spotify", montant2, debut2, fin2, StatutAbonnement.ACTIVE));
        abonnementService.genererEcheances(id2);

        System.out.println("=== Donnees de test chargees ===");
        System.out.println("ID Netflix  : " + id1);
        System.out.println("ID Spotify  : " + id2);
        System.out.println();

        // --- Lancer le menu ---
        Menu menu = new Menu(abonnementService, paiementService);
        menu.demarrer();
    }
}
