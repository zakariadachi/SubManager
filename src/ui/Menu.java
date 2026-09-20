package ui;

import entity.Abonnement;
import entity.AbonnementAvecEngagement;
import entity.AbonnementSansEngagement;
import entity.Paiement;
import entity.StatutAbonnement;
import entity.StatutPaiement;
import entity.TypePaiement;
import service.AbonnementService;
import service.PaiementService;
import util.DateUtil;
import util.Validator;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class Menu {

    private final AbonnementService abonnementService;
    private final PaiementService paiementService;
    private final Scanner scanner;

    public Menu(AbonnementService abonnementService, PaiementService paiementService) {
        this.abonnementService = abonnementService;
        this.paiementService = paiementService;
        this.scanner = new Scanner(System.in);
    }

    public void demarrer() {
        boolean running = true;
        while (running) {
            afficherMenuPrincipal();
            String choix = scanner.nextLine().trim();
            switch (choix) {
                case "1": gererAbonnements(); break;
                case "2": gererPaiements(); break;
                case "3": gererRapports(); break;
                case "4": running = false; System.out.println("Au revoir !"); break;
                default: System.out.println("Choix invalide.");
            }
        }
    }

    // ─── MENU ABONNEMENTS ────────────────────────────────────────────────────

    private void gererAbonnements() {
        boolean back = false;
        while (!back) {
            afficherMenuAbonnements();
            String choix = scanner.nextLine().trim();
            switch (choix) {
                case "1": creerAbonnement(); break;
                case "2": modifierAbonnement(); break;
                case "3": supprimerAbonnement(); break;
                case "4": resilierAbonnement(); break;
                case "5": listerAbonnements(); break;
                case "6": genererEcheances(); break;
                case "7": back = true; break;
                default: System.out.println("Choix invalide.");
            }
        }
    }

    private void creerAbonnement() {
        System.out.println("\nType : 1. Avec engagement  2. Sans engagement");
        String type = scanner.nextLine().trim();

        System.out.print("Nom du service : ");
        String nom = scanner.nextLine().trim();

        System.out.print("Montant mensuel (€) : ");
        double montant = lireDouble();

        System.out.print("Date début (dd/MM/yyyy) : ");
        LocalDate debut = lireDate();

        System.out.print("Date fin (dd/MM/yyyy) : ");
        LocalDate fin = lireDate();

        if (debut == null || fin == null) {
            System.out.println("Format de date invalide. Abonnement non créé.");
            return;
        }

        if (!Validator.isNonBlank(nom) || !Validator.isPositive(montant) || !Validator.isValidDateRange(debut, fin)) {
            System.out.println("Données invalides. Abonnement non créé.");
            return;
        }

        String id = UUID.randomUUID().toString();

        if ("1".equals(type)) {
            System.out.print("Durée d'engagement (mois) : ");
            int duree = lireInt();
            if (!Validator.isPositive(duree)) { System.out.println("Durée invalide."); return; }
            abonnementService.creerAbonnement(
                    new AbonnementAvecEngagement(id, nom, montant, debut, fin, StatutAbonnement.ACTIVE, duree));
        } else {
            abonnementService.creerAbonnement(
                    new AbonnementSansEngagement(id, nom, montant, debut, fin, StatutAbonnement.ACTIVE));
        }
        abonnementService.genererEcheances(id);
        System.out.println("Abonnement créé avec succès. ID : " + id);
    }

    private void modifierAbonnement() {
        System.out.print("ID de l'abonnement à modifier : ");
        String id = scanner.nextLine().trim();

        abonnementService.trouverParId(id).ifPresent(a -> {
            System.out.print("Nouveau nom (" + a.getNomService() + ") [Laisser vide pour ignorer] : ");
            String nom = scanner.nextLine().trim();
            System.out.print("Nouveau montant (" + a.getMontantMensuel() + ") [Laisser vide pour ignorer] : ");
            double montant = lireDouble();
            System.out.print("Nouvelle date fin (" + DateUtil.format(a.getDateFin()) + ") [Laisser vide pour ignorer] : ");
            LocalDate fin = lireDate();

            if (Validator.isNonBlank(nom)) a.setNomService(nom);
            if (Validator.isPositive(montant)) a.setMontantMensuel(montant);
            if (fin != null && !fin.isBefore(a.getDateDebut())) a.setDateFin(fin);

            abonnementService.modifierAbonnement(a);
            System.out.println("Abonnement modifié.");
        });
        if (!abonnementService.trouverParId(id).isPresent()) {
            System.out.println("Abonnement introuvable.");
        }
    }

    private void supprimerAbonnement() {
        System.out.print("ID de l'abonnement à supprimer : ");
        String id = scanner.nextLine().trim();
        if (abonnementService.trouverParId(id).isPresent()) {
            abonnementService.supprimerAbonnement(id);
            System.out.println("Abonnement supprimé.");
        } else {
            System.out.println("Abonnement introuvable.");
        }
    }

    private void resilierAbonnement() {
        System.out.print("ID de l'abonnement à résilier : ");
        String id = scanner.nextLine().trim();
        if (abonnementService.trouverParId(id).isPresent()) {
            abonnementService.resilierAbonnement(id);
            System.out.println("Abonnement résilié.");
        } else {
            System.out.println("Abonnement introuvable.");
        }
    }

    private void listerAbonnements() {
        List<entity.Abonnement> abonnements = abonnementService.listerAbonnements();
        if (abonnements.isEmpty()) {
            System.out.println("Aucun abonnement.");
        } else {
            abonnements.forEach(System.out::println);
        }
    }

    private void genererEcheances() {
        System.out.print("ID de l'abonnement : ");
        String id = scanner.nextLine().trim();
        if (abonnementService.trouverParId(id).isPresent()) {
            abonnementService.genererEcheances(id);
            System.out.println("Échéances générées.");
        } else {
            System.out.println("Abonnement introuvable.");
        }
    }

    // ─── MENU PAIEMENTS ──────────────────────────────────────────────────────

    private void gererPaiements() {
        boolean back = false;
        while (!back) {
            afficherMenuPaiements();
            String choix = scanner.nextLine().trim();
            switch (choix) {
                case "1": afficherPaiementsAbonnement(); break;
                case "2": enregistrerPaiement(); break;
                case "3": modifierPaiement(); break;
                case "4": supprimerPaiement(); break;
                case "5": afficherImpayes(); break;
                case "6": afficherSommePayee(); break;
                case "7": afficherCinqDerniers(); break;
                case "8": back = true; break;
                default: System.out.println("Choix invalide.");
            }
        }
    }

    private void afficherPaiementsAbonnement() {
        System.out.print("ID de l'abonnement : ");
        String id = scanner.nextLine().trim();
        List<Paiement> paiements = paiementService.listerParAbonnement(id);
        if (paiements.isEmpty()) {
            System.out.println("Aucun paiement pour cet abonnement.");
        } else {
            paiements.forEach(System.out::println);
        }
    }

    private void enregistrerPaiement() {
        System.out.print("ID de l'abonnement : ");
        String idAbonnement = scanner.nextLine().trim();

        if (!abonnementService.trouverParId(idAbonnement).isPresent()) {
            System.out.println("Abonnement introuvable.");
            return;
        }

        System.out.print("Date d'échéance (dd/MM/yyyy) : ");
        LocalDate echeance = lireDate();

        System.out.print("Date de paiement (dd/MM/yyyy) : ");
        LocalDate datePaiement = lireDate();

        System.out.println("Type : 1.CARTE  2.VIREMENT  3.ESPECES  4.PRELEVEMENT");
        TypePaiement type = lireTypePaiement();

        System.out.println("Statut : 1.PAYE  2.NON_PAYE  3.EN_RETARD");
        StatutPaiement statut = lireStatutPaiement();

        Paiement paiement = new Paiement(idAbonnement, echeance, datePaiement, type, statut);
        paiementService.enregistrerPaiement(paiement);
        System.out.println("Paiement enregistré. ID : " + paiement.getIdPaiement());
    }

    private void modifierPaiement() {
        System.out.print("ID du paiement : ");
        String id = scanner.nextLine().trim();

        Optional<Paiement> opt = paiementService.trouverParId(id);
        if (!opt.isPresent()) { System.out.println("Paiement introuvable."); return; }

        Paiement p = opt.get();
        System.out.print("Nouvelle date de paiement (dd/MM/yyyy) : ");
        LocalDate date = lireDate();
        if (date != null) p.setDatePaiement(date);

        System.out.println("Nouveau statut : 1.PAYE  2.NON_PAYE  3.EN_RETARD");
        StatutPaiement statut = lireStatutPaiement();
        if (statut != null) p.setStatut(statut);

        paiementService.modifierPaiement(p);
        System.out.println("Paiement modifié.");
    }

    private void supprimerPaiement() {
        System.out.print("ID du paiement : ");
        String id = scanner.nextLine().trim();
        if (paiementService.trouverParId(id).isPresent()) {
            paiementService.supprimerPaiement(id);
            System.out.println("Paiement supprimé.");
        } else {
            System.out.println("Paiement introuvable.");
        }
    }

    private void afficherImpayes() {
        System.out.print("ID de l'abonnement : ");
        String id = scanner.nextLine().trim();
        List<Paiement> impayes = paiementService.detecterImpayes(id);
        if (impayes.isEmpty()) {
            System.out.println("Aucun impayé.");
        } else {
            impayes.forEach(System.out::println);
            System.out.printf("Total impayé : %.2f€%n", paiementService.calculerMontantTotalImpaye(id));
        }
    }

    private void afficherSommePayee() {
        System.out.print("ID de l'abonnement : ");
        String id = scanner.nextLine().trim();
        System.out.printf("Somme payée : %.2f€%n", paiementService.calculerSommePayee(id));
    }

    private void afficherCinqDerniers() {
        List<Paiement> derniers = paiementService.obtenirCinqDerniersPaiements();
        if (derniers.isEmpty()) {
            System.out.println("Aucun paiement.");
        } else {
            derniers.forEach(System.out::println);
        }
    }

    // ─── MENU RAPPORTS ───────────────────────────────────────────────────────

    private void gererRapports() {
        boolean back = false;
        while (!back) {
            afficherMenuRapports();
            String choix = scanner.nextLine().trim();
            switch (choix) {
                case "1": afficherRapportMensuel(); break;
                case "2": afficherRapportAnnuel(); break;
                case "3": afficherRapportImpayes(); break;
                case "4": back = true; break;
                default: System.out.println("Choix invalide.");
            }
        }
    }

    private void afficherRapportMensuel() {
        Map<String, Double> rapport = paiementService.rapportMensuel();
        if (rapport.isEmpty()) { System.out.println("Aucune donnée."); return; }
        System.out.println("\n--- Rapport mensuel ---");
        rapport.forEach((mois, total) -> System.out.printf("%s : %.2f€%n", mois, total));
    }

    private void afficherRapportAnnuel() {
        Map<Integer, Double> rapport = paiementService.rapportAnnuel();
        if (rapport.isEmpty()) { System.out.println("Aucune donnée."); return; }
        System.out.println("\n--- Rapport annuel ---");
        rapport.forEach((annee, total) -> System.out.printf("%d : %.2f€%n", annee, total));
    }

    private void afficherRapportImpayes() {
        List<Paiement> impayes = paiementService.rapportImpayes();
        if (impayes.isEmpty()) { System.out.println("Aucun impayé."); return; }
        System.out.println("\n--- Rapport des impayés ---");
        impayes.forEach(System.out::println);
    }

    // ─── AFFICHAGE DES MENUS ─────────────────────────────────────────────────

    public void afficherMenuPrincipal() {
        System.out.println("\n=== GestionAbonnements ===");
        System.out.println("1. Gestion des abonnements");
        System.out.println("2. Gestion des paiements");
        System.out.println("3. Rapports financiers");
        System.out.println("4. Quitter");
        System.out.print("Votre choix : ");
    }

    public void afficherMenuAbonnements() {
        System.out.println("\n--- Gestion des abonnements ---");
        System.out.println("1. Créer un abonnement");
        System.out.println("2. Modifier un abonnement");
        System.out.println("3. Supprimer un abonnement");
        System.out.println("4. Résilier un abonnement");
        System.out.println("5. Lister les abonnements");
        System.out.println("6. Générer les échéances");
        System.out.println("7. Retour");
        System.out.print("Votre choix : ");
    }

    public void afficherMenuPaiements() {
        System.out.println("\n--- Gestion des paiements ---");
        System.out.println("1. Afficher les paiements d'un abonnement");
        System.out.println("2. Enregistrer un paiement");
        System.out.println("3. Modifier un paiement");
        System.out.println("4. Supprimer un paiement");
        System.out.println("5. Afficher les paiements manqués");
        System.out.println("6. Afficher la somme payée");
        System.out.println("7. Afficher les 5 derniers paiements");
        System.out.println("8. Retour");
        System.out.print("Votre choix : ");
    }

    public void afficherMenuRapports() {
        System.out.println("\n--- Rapports financiers ---");
        System.out.println("1. Rapport mensuel");
        System.out.println("2. Rapport annuel");
        System.out.println("3. Rapport des impayés");
        System.out.println("4. Retour");
        System.out.print("Votre choix : ");
    }

    // ─── HELPERS SAISIE ──────────────────────────────────────────────────────

    private double lireDouble() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private int lireInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private LocalDate lireDate() {
        try {
            return DateUtil.parse(scanner.nextLine().trim()).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    private TypePaiement lireTypePaiement() {
        while (true) {
            switch (scanner.nextLine().trim()) {
                case "1": return TypePaiement.CARTE;
                case "2": return TypePaiement.VIREMENT;
                case "3": return TypePaiement.ESPECES;
                case "4": return TypePaiement.PRELEVEMENT;
                default: System.out.print("Choix invalide. Entrez 1, 2, 3 ou 4 : ");
            }
        }
    }

    private StatutPaiement lireStatutPaiement() {
        while (true) {
            switch (scanner.nextLine().trim()) {
                case "1": return StatutPaiement.PAYE;
                case "2": return StatutPaiement.NON_PAYE;
                case "3": return StatutPaiement.EN_RETARD;
                default: System.out.print("Choix invalide. Entrez 1, 2 ou 3 : ");
            }
        }
    }
}
