package alessia.cotini.sartoria_digitale_backend.runners;

import alessia.cotini.sartoria_digitale_backend.entities.Utente;
import alessia.cotini.sartoria_digitale_backend.enums.Ruolo;
import alessia.cotini.sartoria_digitale_backend.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SuperAdminRunner implements CommandLineRunner {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final String email;
    private final String password;

    public SuperAdminRunner(
            UtenteRepository utenteRepository,
            PasswordEncoder passwordEncoder,
            @Value("${superadmin.email}") String email,
            @Value("${superadmin.password}") String password
    ) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
        this.email = email;
        this.password = password;
    }

    @Override
    public void run(String... args) {
        boolean esisteGia = utenteRepository.findByEmail(email).isPresent();
        if (esisteGia) {
            return;
        }

        Utente superAdmin = new Utente();
        superAdmin.setNome("Super");
        superAdmin.setCognome("Admin");
        superAdmin.setEmail(email);
        superAdmin.setPassword(passwordEncoder.encode(password));
        superAdmin.setRuolo(Ruolo.SUPER_ADMIN);

        utenteRepository.save(superAdmin);
        System.out.println("Super-admin creato con email: " + email);
    }
}
