# 📋 SubManager — Application de gestion d'abonnements

> Application console Java 8 permettant de centraliser, suivre et analyser vos abonnements personnels ou professionnels.

---

## 🎯 Présentation

SubManager est une application console développée en **Java 8** qui permet de :
- Gérer des abonnements avec ou sans engagement
- Générer automatiquement les échéances de paiement
- Détecter les paiements manqués ou en retard
- Produire des rapports financiers mensuels, annuels et d'impayés

---

## 🏗️ Architecture

L'application respecte une **architecture en couches** stricte :

```
src/
├── Main.java                          ← Point d'entrée + données de test
├── entity/                            ← Couche entité (objets métier)
│   ├── Abonnement.java                ← Classe abstraite
│   ├── AbonnementAvecEngagement.java  ← Abonnement avec durée d'engagement
│   ├── AbonnementSansEngagement.java  ← Abonnement sans engagement
│   ├── Paiement.java                  ← Entité paiement
│   ├── StatutAbonnement.java          ← Enum : ACTIVE, SUSPENDU, RESILIE
│   ├── StatutPaiement.java            ← Enum : PAYE, NON_PAYE, EN_RETARD
│   └── TypePaiement.java              ← Enum : CARTE, VIREMENT, ESPECES, PRELEVEMENT
├── dao/                               ← Couche persistance (en mémoire)
│   ├── AbonnementDAO.java
│   └── PaiementDAO.java
├── service/                           ← Couche métier
│   ├── AbonnementService.java
│   └── PaiementService.java
├── ui/                                ← Couche présentation
│   └── Menu.java
└── util/                              ← Utilitaires
    ├── DateUtil.java
    └── Validator.java
```

---

## ✨ Fonctionnalités

### 📦 Gestion des abonnements
| Fonctionnalité | Description |
|---|---|
| Créer un abonnement | Avec ou sans engagement, génération automatique des échéances |
| Modifier un abonnement | Mise à jour partielle (champ vide = inchangé) |
| Supprimer un abonnement | Suppression définitive |
| Résilier un abonnement | Passage au statut `RESILIE` |
| Lister les abonnements | Affichage de tous les abonnements |
| Générer les échéances | Re-génération des échéances (sans doublons) |

### 💳 Gestion des paiements
| Fonctionnalité | Description |
|---|---|
| Afficher les paiements d'un abonnement | Liste complète |
| Enregistrer un paiement | Avec type et statut |
| Modifier un paiement | Date et statut modifiables |
| Supprimer un paiement | Suppression définitive |
| Paiements manqués | Liste des `NON_PAYE` et `EN_RETARD` + montant total |
| Somme payée | Total des paiements `PAYE` pour un abonnement |
| 5 derniers paiements | Triés par date décroissante |

### 📊 Rapports financiers
| Rapport | Description |
|---|---|
| Rapport mensuel | Total payé par mois (`yyyy-MM`) |
| Rapport annuel | Total payé par année |
| Rapport des impayés | Tous les paiements `NON_PAYE` ou `EN_RETARD`, triés par date d'échéance |

---

## 🚀 Lancer l'application

### Prérequis
- **Java 8** ou supérieur
- IntelliJ IDEA (recommandé) ou tout autre IDE Java

### Depuis IntelliJ IDEA
1. Ouvrir le projet `SubManager`
2. Vérifier que le dossier `src/` est marqué comme **Sources Root**
3. Lancer `Main.java`

### Depuis le terminal
```bash
# Compiler
javac -d out/production/SubManager src/**/*.java src/Main.java

# Exécuter
java -cp out/production/SubManager Main
```

---

## 🧪 Données de test

Au démarrage, l'application charge automatiquement **deux abonnements de test** avec leurs échéances :

| ID | Service | Montant | Période | Type |
|---|---|---|---|---|
| `netflix-test-id` | Netflix | 100 DH/mois | Jan–Avr 2024 | Avec engagement (3 mois) |
| `spotify-test-id` | Spotify | 70 DH/mois | Jan–Mar 2024 | Sans engagement |

Ces IDs sont affichés au démarrage pour faciliter les tests.

---

## 🧰 Technologies utilisées

| Technologie | Usage |
|---|---|
| **Java 8** | Langage principal |
| **Stream API** | Filtrage, tri, agrégation des données |
| **Lambda** | Callbacks et expressions fonctionnelles |
| **Optional** | Gestion sûre des valeurs nullables |
| **Collectors** | Groupement pour les rapports financiers |
| **UUID** | Génération d'identifiants uniques |
| **LocalDate** | Gestion des dates (Java Time API) |
| **Collections Java** | Persistance en mémoire (`HashMap`) |

---

## 📐 Modèle de données

```
Abonnement (1) ──────────────── (n) Paiement
   id                                idPaiement
   nomService                        idAbonnement (FK)
   montantMensuel                    dateEcheance
   dateDebut                         datePaiement
   dateFin                           typePaiement
   statut                            statut
   [dureeEngagementMois]
```

---

## 📁 Diagramme de classes (simplifié)

```
           ┌─────────────────┐
           │  <<abstract>>   │
           │   Abonnement    │
           └────────┬────────┘
                    │
          ┌─────────┴──────────┐
          │                    │
┌─────────────────┐  ┌──────────────────────┐
│ AbonnementSans  │  │  AbonnementAvec       │
│ Engagement      │  │  Engagement           │
│                 │  │  + dureeEngagement    │
└─────────────────┘  └──────────────────────┘

┌──────────────────────────────────┐
│            Paiement              │
│  idPaiement, idAbonnement        │
│  dateEcheance, datePaiement      │
│  typePaiement, statut            │
└──────────────────────────────────┘
```

---

## 👤 Auteur

- **Nom :** Zakaria Dachi
- **Formation :** Développement Java — Brief Sprint 1
- **Période :** 14/09/2026 – 18/09/2026

