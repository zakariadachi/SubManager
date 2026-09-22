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
import java.time.format.DateTimeParseException;
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
                case "4": running = false; afficher("Au revoir !"); break;
                default: afficher("Choix invalide.");
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
                default: afficher("Choix invalide.");
            }
        }
    }

    private void creerAbonnement() {
        afficher("\nType : 1. Avec engagement  2. Sans engagement");
        String type = scanner.nextLine().trim();

        System.out.print("Nom du service : ");
        String nom = scanner.nextLine().trim();

        System.out.print("Montant total (DH) : ");
        double montantTotal = lireDouble();

        System.out.print("Date debut (dd/MM/yyyy) : ");
        LocalDate debut = lireDate();

        System.out.print("Date fin (dd/MM/yyyy) : ");
        LocalDate fin = lireDate();

        if (debut == null || fin == null) {
            afficher("Format de date invalide. Abonnement non cree.");
            return;
        }

        int duree = (int) java.time.temporal.ChronoUnit.MONTHS.between(debut, fin);
        if (duree <= 0) {
            afficher("Duree invalide : la date fin doit etre apres la date debut.");
            return;
        }

        double montant = montantTotal / duree;
        if (!Validator.isNonBlank(nom) || !Validator.isPositive(montantTotal) || !Validator.isValidDateRange(debut, fin)) {
            afficher("Donnees invalides. Abonnement non cree.");
            return;
        }

        String id = UUID.randomUUID().toString();

        if ("1".equals(type)) {
            abonnementService.creerAbonnement(
                    new AbonnementAvecEngagement(id, nom, montant, debut, fin, StatutAbonnement.ACTIVE, duree));
        } else {
            abonnementService.creerAbonnement(
                    new AbonnementSansEngagement(id, nom, montant, debut, fin, StatutAbonnement.ACTIVE));
        }
        abonnementService.genererEcheances(id);
        afficher("Abonnement cree avec succes. ID : " + id);
    }

    private void modifierAbonnement() {
        System.out.print("ID de l'abonnement a modifier : ");
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
            afficher("Abonnement modifie.");
        });
        if (!abonnementService.trouverParId(id).isPresent()) {
            afficher("Abonnement introuvable.");
        }
    }

    private void supprimerAbonnement() {
        System.out.print("ID de l'abonnement a supprimer : ");
        String id = scanner.nextLine().trim();
        if (abonnementService.trouverParId(id).isPresent()) {
            abonnementService.supprimerAbonnement(id);
            afficher("Abonnement supprime.");
        } else {
            afficher("Abonnement introuvable.");
        }
    }

    private void resilierAbonnement() {
        System.out.print("ID de l'abonnement a resilier : ");
        String id = scanner.nextLine().trim();
        if (abonnementService.trouverParId(id).isPresent()) {
            abonnementService.resilierAbonnement(id);
            afficher("Abonnement resilie.");
        } else {
            afficher("Abonnement introuvable.");
        }
    }

    private void listerAbonnements() {
        List<entity.Abonnement> abonnements = abonnementService.listerAbonnements();
        if (abonnements.isEmpty()) {
            afficher("Aucun abonnement.");
        } else {
            abonnements.forEach(System.out::println);
        }
    }

    private void genererEcheances() {
        System.out.print("ID de l'abonnement : ");
        String id = scanner.nextLine().trim();

        if (!abonnementService.trouverParId(id).isPresent()) {
            afficher("Abonnement introuvable.");
            return;
        }

        double montant = abonnementService.trouverParId(id)
                .map(a -> a.getMontantMensuel())
                .orElse(0.0);

        List<Paiement> generes = abonnementService.genererEcheances(id);

        if (generes.isEmpty()) {
            afficher("Aucune echeance generee (verifiez les dates de l'abonnement).");
            return;
        }

        afficher("\nEcheances generees avec succes :");
        for (Paiement p : generes) {
            System.out.printf("  - %s | %.2f DH | %s%n",
                    DateUtil.format(p.getDateEcheance()),
                    montant,
                    p.getStatut());
        }
        System.out.printf("%nNombre d'echeances generees : %d%n", generes.size());
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
                default: afficher("Choix invalide.");
            }
        }
    }

    private void afficherPaiementsAbonnement() {
        System.out.print("ID de l'abonnement : ");
        String id = scanner.nextLine().trim();
        List<Paiement> paiements = paiementService.listerParAbonnement(id);
        if (paiements.isEmpty()) {
            afficher("Aucun paiement pour cet abonnement.");
        } else {
            paiements.forEach(System.out::println);
        }
    }

    private void enregistrerPaiement() {
        System.out.print("ID de l'abonnement : ");
        String idAbonnement = scanner.nextLine().trim();

        if (!abonnementService.trouverParId(idAbonnement).isPresent()) {
            afficher("Abonnement introuvable.");
            return;
        }

        System.out.print("Date d'echeance (dd/MM/yyyy) : ");
        LocalDate echeance = lireDate();
        if (echeance == null) {
            afficher("Format de date invalide. Paiement non enregistre.");
            return;
        }

        System.out.print("Date de paiement (dd/MM/yyyy, ou laisser vide si non paye) : ");
        LocalDate datePaiement = lireDate(); // null est accepte pour un paiement non encore effectue

        afficher("Type : 1.CARTE  2.VIREMENT  3.ESPECES  4.PRELEVEMENT");
        TypePaiement type = lireTypePaiement();

        // Le statut est calculé automatiquement par le service selon la règle métier
        Paiement paiement = new Paiement(idAbonnement, echeance, datePaiement, type, StatutPaiement.NON_PAYE);
        paiementService.enregistrerPaiement(paiement);
        afficher("Paiement enregistre. ID : " + paiement.getIdPaiement());
    }

    private void modifierPaiement() {
        System.out.print("ID du paiement : ");
        String id = scanner.nextLine().trim();

        Optional<Paiement> opt = paiementService.trouverParId(id);
        if (!opt.isPresent()) { afficher("Paiement introuvable."); return; }

        Paiement p = opt.get();
        System.out.print("Nouvelle date de paiement (dd/MM/yyyy, ou laisser vide si non paye) : ");
        LocalDate date = lireDate();
        if (date != null) p.setDatePaiement(date);

        // Le statut est recalculé automatiquement par le service
        paiementService.modifierPaiement(p);
        afficher("Paiement modifie.");
    }

    private void supprimerPaiement() {
        System.out.print("ID du paiement : ");
        String id = scanner.nextLine().trim();
        if (paiementService.trouverParId(id).isPresent()) {
            paiementService.supprimerPaiement(id);
            afficher("Paiement supprime.");
        } else {
            afficher("Paiement introuvable.");
        }
    }

    private void afficherImpayes() {
        System.out.print("ID de l'abonnement : ");
        String id = scanner.nextLine().trim();
        List<Paiement> impayes = paiementService.detecterImpayes(id);
        if (impayes.isEmpty()) {
            afficher("Aucun impaye.");
        } else {
            impayes.forEach(System.out::println);
            System.out.printf("Total impaye : %.2fDH%n", paiementService.calculerMontantTotalImpaye(id));
        }
    }

    private void afficherSommePayee() {
        System.out.print("ID de l'abonnement : ");
        String id = scanner.nextLine().trim();
        System.out.printf("Somme payee : %.2f DH%n", paiementService.calculerSommePayee(id));
    }

    private void afficherCinqDerniers() {
        List<Paiement> derniers = paiementService.obtenirCinqDerniersPaiements();
        if (derniers.isEmpty()) {
            afficher("Aucun paiement.");
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
                default: afficher("Choix invalide.");
            }
        }
    }

    private void afficherRapportMensuel() {
        Map<String, Double> rapport = paiementService.rapportMensuel();
        if (rapport.isEmpty()) { afficher("Aucune donnee."); return; }
        afficher("\n--- Rapport mensuel ---");
        rapport.forEach((mois, total) -> System.out.printf("%s : %.2fDH%n", mois, total));
    }

    private void afficherRapportAnnuel() {
        Map<Integer, Double> rapport = paiementService.rapportAnnuel();
        if (rapport.isEmpty()) { afficher("Aucune donnee."); return; }
        afficher("\n--- Rapport annuel ---");
        rapport.forEach((annee, total) -> System.out.printf("%d : %.2fDH%n", annee, total));
    }

    private void afficherRapportImpayes() {
        List<Paiement> impayes = paiementService.rapportImpayes();
        if (impayes.isEmpty()) { afficher("Aucun impaye."); return; }

        afficher("\n--- Paiements NON_PAYE ---");
        boolean aucunNonPaye = true;
        for (Paiement p : impayes) {
            if (p.getStatut() == StatutPaiement.NON_PAYE) {
                afficher(p.toString());
                aucunNonPaye = false;
            }
        }
        if (aucunNonPaye) afficher("Aucun.");

        afficher("\n--- Paiements EN_RETARD ---");
        boolean aucunRetard = true;
        for (Paiement p : impayes) {
            if (p.getStatut() == StatutPaiement.EN_RETARD) {
                afficher(p.toString());
                aucunRetard = false;
            }
        }
        if (aucunRetard) afficher("Aucun.");
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
        System.out.println("1. Creer un abonnement");
        System.out.println("2. Modifier un abonnement");
        System.out.println("3. Supprimer un abonnement");
        System.out.println("4. Resilier un abonnement");
        System.out.println("5. Lister les abonnements");
        System.out.println("6. Generer les echeances");
        System.out.println("7. Retour");
        System.out.print("Votre choix : ");
    }

    public void afficherMenuPaiements() {
        System.out.println("\n--- Gestion des paiements ---");
        System.out.println("1. Afficher les paiements d'un abonnement");
        System.out.println("2. Enregistrer un paiement");
        System.out.println("3. Modifier un paiement");
        System.out.println("4. Supprimer un paiement");
        System.out.println("5. Afficher les paiements manques");
        System.out.println("6. Afficher la somme payee");
        System.out.println("7. Afficher les 5 derniers paiements");
        System.out.println("8. Retour");
        System.out.print("Votre choix : ");
    }

    public void afficherMenuRapports() {
        System.out.println("\n--- Rapports financiers ---");
        System.out.println("1. Rapport mensuel");
        System.out.println("2. Rapport annuel");
        System.out.println("3. Rapport des impayes");
        System.out.println("4. Retour");
        System.out.print("Votre choix : ");
    }

    private void afficher(String message) {
        System.out.println(message);
    }

    // ─── HELPERS SAISIE ──────────────────────────────────────────────────────

    private double lireDouble() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private LocalDate lireDate() {
        try {
            return DateUtil.parse(scanner.nextLine().trim()).orElse(null);
        } catch (DateTimeParseException e) {
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
