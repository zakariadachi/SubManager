import dao.AbonnementDAO;
import dao.PaiementDAO;
import entity.AbonnementAvecEngagement;
import entity.AbonnementSansEngagement;
import entity.Paiement;
import entity.StatutAbonnement;
import entity.StatutPaiement;
import entity.TypePaiement;
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

        // --- Abonnement 1 : Netflix avec engagement (3 mois) ---
        String id1 = "netflix-test-id";
        LocalDate debut1 = LocalDate.of(2026, 9, 1);
        LocalDate fin1   = LocalDate.of(2026, 12, 1);
        int duree1 = (int) ChronoUnit.MONTHS.between(debut1, fin1);
        abonnementService.creerAbonnement(
                new AbonnementAvecEngagement(id1, "Netflix", 300.0 / duree1, debut1, fin1, StatutAbonnement.ACTIVE, duree1));
        abonnementService.genererEcheances(id1);
        // Paiements Netflix
        paiementService.enregistrerPaiement(new Paiement(id1, LocalDate.of(2026, 9, 1),  LocalDate.of(2026, 9, 2),  TypePaiement.CARTE, StatutPaiement.PAYE));
        paiementService.enregistrerPaiement(new Paiement(id1, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 15),TypePaiement.CARTE, StatutPaiement.EN_RETARD));
        paiementService.enregistrerPaiement(new Paiement(id1, LocalDate.of(2026, 11, 1), null,                      TypePaiement.CARTE, StatutPaiement.NON_PAYE));

        // --- Abonnement 2 : Spotify sans engagement (2 mois) ---
        String id2 = "spotify-test-id";
        LocalDate debut2 = LocalDate.of(2026, 9, 1);
        LocalDate fin2   = LocalDate.of(2026, 11, 1);
        int duree2 = (int) ChronoUnit.MONTHS.between(debut2, fin2);
        abonnementService.creerAbonnement(
                new AbonnementSansEngagement(id2, "Spotify", 140.0 / duree2, debut2, fin2, StatutAbonnement.ACTIVE));
        abonnementService.genererEcheances(id2);
        // Paiements Spotify
        paiementService.enregistrerPaiement(new Paiement(id2, LocalDate.of(2026, 9, 1),  LocalDate.of(2026, 9, 1), TypePaiement.PRELEVEMENT, StatutPaiement.PAYE));
        paiementService.enregistrerPaiement(new Paiement(id2, LocalDate.of(2026, 10, 1), null,                     TypePaiement.PRELEVEMENT, StatutPaiement.NON_PAYE));

        // --- Abonnement 3 : YouTube Premium avec engagement (6 mois) ---
        String id3 = "youtube-test-id";
        LocalDate debut3 = LocalDate.of(2024, 1, 1);
        LocalDate fin3   = LocalDate.of(2024, 7, 1);
        int duree3 = (int) ChronoUnit.MONTHS.between(debut3, fin3);
        abonnementService.creerAbonnement(
                new AbonnementAvecEngagement(id3, "YouTube Premium", 600.0 / duree3, debut3, fin3, StatutAbonnement.ACTIVE, duree3));
        abonnementService.genererEcheances(id3);
        // Paiements YouTube
        paiementService.enregistrerPaiement(new Paiement(id3, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 3),  TypePaiement.VIREMENT,   StatutPaiement.PAYE));
        paiementService.enregistrerPaiement(new Paiement(id3, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 1),  TypePaiement.VIREMENT,   StatutPaiement.PAYE));
        paiementService.enregistrerPaiement(new Paiement(id3, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 20), TypePaiement.VIREMENT,   StatutPaiement.EN_RETARD));
        paiementService.enregistrerPaiement(new Paiement(id3, LocalDate.of(2024, 4, 1), null,                      TypePaiement.VIREMENT,   StatutPaiement.NON_PAYE));
        paiementService.enregistrerPaiement(new Paiement(id3, LocalDate.of(2024, 5, 1), null,                      TypePaiement.VIREMENT,   StatutPaiement.NON_PAYE));

        // --- Abonnement 4 : Canal+ resilie ---
        String id4 = "canal-test-id";
        LocalDate debut4 = LocalDate.of(2024, 1, 1);
        LocalDate fin4   = LocalDate.of(2024, 4, 1);
        int duree4 = (int) ChronoUnit.MONTHS.between(debut4, fin4);
        abonnementService.creerAbonnement(
                new AbonnementAvecEngagement(id4, "Canal+", 450.0 / duree4, debut4, fin4, StatutAbonnement.ACTIVE, duree4));
        abonnementService.genererEcheances(id4);
        abonnementService.resilierAbonnement(id4);
        paiementService.enregistrerPaiement(new Paiement(id4, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 5), TypePaiement.ESPECES, StatutPaiement.PAYE));
        paiementService.enregistrerPaiement(new Paiement(id4, LocalDate.of(2024, 2, 1), null,                     TypePaiement.ESPECES, StatutPaiement.NON_PAYE));

        // --- Abonnement 5 : Amazon Prime sans engagement (4 mois) ---
        String id5 = "amazon-test-id";
        LocalDate debut5 = LocalDate.of(2024, 2, 1);
        LocalDate fin5   = LocalDate.of(2024, 6, 1);
        int duree5 = (int) ChronoUnit.MONTHS.between(debut5, fin5);
        abonnementService.creerAbonnement(
                new AbonnementSansEngagement(id5, "Amazon Prime", 400.0 / duree5, debut5, fin5, StatutAbonnement.ACTIVE));
        abonnementService.genererEcheances(id5);
        paiementService.enregistrerPaiement(new Paiement(id5, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 1),  TypePaiement.CARTE, StatutPaiement.PAYE));
        paiementService.enregistrerPaiement(new Paiement(id5, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 3, 1),  TypePaiement.CARTE, StatutPaiement.PAYE));
        paiementService.enregistrerPaiement(new Paiement(id5, LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 10), TypePaiement.CARTE, StatutPaiement.EN_RETARD));
        paiementService.enregistrerPaiement(new Paiement(id5, LocalDate.of(2024, 5, 1), null,                      TypePaiement.CARTE, StatutPaiement.NON_PAYE));

        System.out.println("=== Donnees de test chargees ===");
        System.out.println("ID Netflix       : " + id1);
        System.out.println("ID Spotify       : " + id2);
        System.out.println("ID YouTube       : " + id3);
        System.out.println("ID Canal+ (resilie) : " + id4);
        System.out.println("ID Amazon Prime  : " + id5);
        System.out.println();

        Menu menu = new Menu(abonnementService, paiementService);
        menu.demarrer();
    }
}
