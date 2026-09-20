package dao;

import entity.Abonnement;
import entity.StatutAbonnement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AbonnementDAO {

    private final Map<String, Abonnement> store = new HashMap<>();

    public void create(Abonnement abonnement) {
        store.put(abonnement.getId(), abonnement);
    }

    public Optional<Abonnement> findById(String id) { 
        return Optional.ofNullable(store.get(id));
    }

    public List<Abonnement> findAll() {
         return new ArrayList<>(store.values());
    }

    public void update(Abonnement abonnement) {
        store.put(abonnement.getId(), abonnement);
    }

    public void delete(String id) {
        store.remove(id);
    }

    public List<Abonnement> findActiveSubscriptions() {
         
        return store.values().stream().filter(s->s.getStatut()==StatutAbonnement.ACTIVE).collect(Collectors.toList());
    }

    /** Recherche par type : AbonnementAvecEngagement ou AbonnementSansEngagement */
    public List<Abonnement> findByType(Class<? extends Abonnement> type) {
        return store.values().stream().filter(a->type.isInstance(a)).collect(Collectors.toList());
    }
}
