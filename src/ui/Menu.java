package ui;

import service.AbonnementService;
import service.PaiementService;

import java.util.Scanner;

public class Menu {

    private final AbonnementService abonnementService;
    private final PaiementService paiementService;
    private final Scanner scanner;

    public Menu(AbonnementService abonnementService, PaiementService paiementService) {
        this.abonnementService = abonnementService;
        this.paiementService = paiementService;
        this.scanner = new Scanner(System.in);
    }

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

    public void demarrer() {
        // sera implémenté lors du sprint suivant
    }
}
