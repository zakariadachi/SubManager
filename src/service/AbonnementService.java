package service;

import dao.AbonnementDAO;
import dao.PaiementDAO;
import entity.Abonnement;
import entity.Paiement;
import entity.StatutAbonnement;
import entity.StatutPaiement;
import entity.TypePaiement;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import util.Validator;

public class AbonnementService {

    private final AbonnementDAO abonnementDAO;
    private final PaiementDAO paiementDAO;

    public AbonnementService(AbonnementDAO abonnementDAO, PaiementDAO paiementDAO) {
        this.abonnementDAO = abonnementDAO;
        this.paiementDAO = paiementDAO;
    }

    public void creerAbonnement(Abonnement abonnement) {
        if (Validator.isNonBlank(abonnement.getNomService())
                && Validator.isPositive(abonnement.getMontantMensuel())
                && Validator.isValidDateRange(abonnement.getDateDebut(),abonnement.getDateFin())) {

            abonnementDAO.create(abonnement);
        }
    }

    public void modifierAbonnement(Abonnement abonnement) {
        Optional<Abonnement> existingAbonnement = abonnementDAO.findById(abonnement.getId());

        if (existingAbonnement.isPresent()
                && Validator.isNonBlank(abonnement.getNomService())
                && Validator.isPositive(abonnement.getMontantMensuel())
                && Validator.isValidDateRange(abonnement.getDateDebut(),abonnement.getDateFin())) {

            abonnementDAO.update(abonnement);
        }
    }

    public void supprimerAbonnement(String id) {
        if (abonnementDAO.findById(id).isPresent()) {
            abonnementDAO.delete(id);
        }
    }

    public void resilierAbonnement(String id) {
        abonnementDAO.findById(id).ifPresent(abonnement -> {
            abonnement.setStatut(StatutAbonnement.RESILIE);
            abonnementDAO.update(abonnement);
        });
    }

    public List<Paiement> genererEcheances(String idAbonnement) {

        Optional<Abonnement> optionalAbonnement =
                abonnementDAO.findById(idAbonnement);

        if (!optionalAbonnement.isPresent()) {
            return java.util.Collections.emptyList();
        }

        Abonnement abonnement = optionalAbonnement.get();

        // Supprimer les échéances existantes pour éviter les doublons
        List<Paiement> existing = paiementDAO.findByAbonnement(idAbonnement);
        existing.forEach(p -> paiementDAO.delete(p.getIdPaiement()));

        List<Paiement> generes = new java.util.ArrayList<>();
        LocalDate date = abonnement.getDateDebut();

        while (date.isBefore(abonnement.getDateFin())) {

            Paiement paiement = new Paiement(
                    abonnement.getId(),
                    date,
                    null,
                    TypePaiement.CARTE,
                    Paiement.calculerStatut(null, date)
            );

            paiementDAO.create(paiement);
            generes.add(paiement);

            date = date.plusMonths(1);
        }

        return generes;
    }

    public Optional<Abonnement> trouverParId(String id) {
        return abonnementDAO.findById(id);
    }

    public List<Abonnement> listerAbonnements() {
        return abonnementDAO.findAll();
    }
}
