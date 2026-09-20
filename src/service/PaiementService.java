package service;

import dao.AbonnementDAO;
import dao.PaiementDAO;
import entity.Abonnement;
import entity.Paiement;
import entity.StatutPaiement;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Paiement> detecterImpayes(String idAbonnement) {
        return paiementDAO.findUnpaidByAbonnement(idAbonnement);
    }

    public double calculerMontantTotalImpaye(String idAbonnement) {
        return paiementDAO.findUnpaidByAbonnement(idAbonnement).stream()
                .mapToDouble(p -> abonnementDAO.findById(p.getIdAbonnement())
                        .map(Abonnement::getMontantMensuel)
                        .orElse(0.0))
                .sum();
    }

    public double calculerSommePayee(String idAbonnement) {
        return paiementDAO.findByAbonnement(idAbonnement).stream()
                .filter(p -> p.getStatut() == StatutPaiement.PAYE)
                .mapToDouble(p -> abonnementDAO.findById(p.getIdAbonnement())
                        .map(Abonnement::getMontantMensuel)
                        .orElse(0.0))
                .sum();
    }

    public List<Paiement> obtenirCinqDerniersPaiements() {
        return paiementDAO.findLastPayments(5);
    }

    /** Rapport mensuel : total payé par mois (format yyyy-MM) */
    public Map<String, Double> rapportMensuel() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
        return paiementDAO.findAll().stream()
                .filter(p -> p.getStatut() == StatutPaiement.PAYE && p.getDatePaiement() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getDatePaiement().format(fmt),
                        LinkedHashMap::new,
                        Collectors.summingDouble(p -> abonnementDAO.findById(p.getIdAbonnement())
                                .map(Abonnement::getMontantMensuel)
                                .orElse(0.0))
                ));
    }

    /** Rapport annuel : total payé par année */
    public Map<Integer, Double> rapportAnnuel() {
        return paiementDAO.findAll().stream()
                .filter(p -> p.getStatut() == StatutPaiement.PAYE && p.getDatePaiement() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getDatePaiement().getYear(),
                        LinkedHashMap::new,
                        Collectors.summingDouble(p -> abonnementDAO.findById(p.getIdAbonnement())
                                .map(Abonnement::getMontantMensuel)
                                .orElse(0.0))
                ));
    }

    /** Rapport des impayés : tous les paiements NON_PAYE ou EN_RETARD */
    public List<Paiement> rapportImpayes() {
        return paiementDAO.findAll().stream()
                .filter(p -> p.getStatut() == StatutPaiement.NON_PAYE
                        || p.getStatut() == StatutPaiement.EN_RETARD)
                .sorted(Comparator.comparing(Paiement::getDateEcheance))
                .collect(Collectors.toList());
    }

    public Optional<Paiement> trouverParId(String id) {
        return paiementDAO.findById(id);
    }

    public List<Paiement> listerParAbonnement(String idAbonnement) {
        return paiementDAO.findByAbonnement(idAbonnement);
    }
}
