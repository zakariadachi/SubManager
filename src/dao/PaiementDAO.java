package dao;

import entity.Paiement;
import entity.StatutPaiement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PaiementDAO {

    private final Map<String, Paiement> store = new HashMap<>();

    public void create(Paiement paiement) {
        store.put(paiement.getIdPaiement(), paiement);
    }

    public Optional<Paiement> findById(String id) { 
        return Optional.ofNullable(store.get(id)); 
    }

    public List<Paiement> findByAbonnement(String idAbonnement) { 
        return store.values().stream().filter(s -> s.getIdAbonnement().equals(idAbonnement)).collect(Collectors.toList());
    }

    public List<Paiement> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Paiement paiement) {
        store.put(paiement.getIdPaiement(), paiement);
    }

    public void delete(String id) {
        store.remove(id);
    }

    public List<Paiement> findUnpaidByAbonnement(String idAbonnement) { 
        return store.values().stream().filter(s -> s.getIdAbonnement().equals(idAbonnement) && s.getStatut() == StatutPaiement.NON_PAYE).collect(Collectors.toList());
    }

    /** Retourne les n derniers paiements triés par date */
    public List<Paiement> findLastPayments(int n) { 
        return store.values().stream().sorted((p1,p2)->p2.getDatePaiement().compareTo(p1.getDatePaiement())).limit(n).collect(Collectors.toList());
    }
}
