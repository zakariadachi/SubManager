package service;

import dao.AbonnementDAO;
import dao.PaiementDAO;
import entity.Paiement;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PaiementService {

    private final PaiementDAO paiementDAO;
    private final AbonnementDAO abonnementDAO;

    public PaiementService(PaiementDAO paiementDAO, AbonnementDAO abonnementDAO) {
        this.paiementDAO = paiementDAO;
        this.abonnementDAO = abonnementDAO;
    }

    public void enregistrerPaiement(Paiement paiement) {
        paiementDAO.create(paiement);
    }

    public void modifierPaiement(Paiement paiement) {
        paiementDAO.update(paiement);
    }

    public void supprimerPaiement(String id) {
        paiementDAO.delete(id);
    }

    public List<Paiement> detecterImpayes(String idAbonnement) { return null; }

    public double calculerMontantTotalImpaye(String idAbonnement) { return 0; }

    public double calculerSommePayee(String idAbonnement) { return 0; }

    public List<Paiement> obtenirCinqDerniersPaiements() { return null; }

    /** Rapport mensuel : total payé par mois (mois au format yyyy-MM) */
    public Map<String, Double> rapportMensuel() { return null; }

    /** Rapport annuel : total payé par année */
    public Map<Integer, Double> rapportAnnuel() { return null; }

    /** Rapport des impayés : liste de tous les paiements non réglés */
    public List<Paiement> rapportImpayes() { return null; }

    public Optional<Paiement> trouverParId(String id) { return Optional.empty(); }
}
