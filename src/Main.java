import dao.AbonnementDAO;
import dao.PaiementDAO;
import service.AbonnementService;
import service.PaiementService;
import ui.Menu;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Application GestionAbonnements démarrée ===");

        AbonnementDAO abonnementDAO = new AbonnementDAO();
        PaiementDAO paiementDAO = new PaiementDAO();
        AbonnementService abonnementService = new AbonnementService(abonnementDAO, paiementDAO);
        PaiementService paiementService = new PaiementService(paiementDAO, abonnementDAO);
        Menu menu = new Menu(abonnementService, paiementService);

        menu.demarrer();
    }
}
