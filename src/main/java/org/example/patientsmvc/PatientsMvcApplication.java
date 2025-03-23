package org.example.patientsmvc;

import org.example.patientsmvc.entities.Patient;
import org.example.patientsmvc.repositories.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.Date;

@SpringBootApplication
public class PatientsMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatientsMvcApplication.class, args);
    }

    //@Bean
    CommandLineRunner commandLineRunner(PatientRepository patientRepository) {
        return args -> {

            patientRepository.save(new Patient(null, "hssayn", new Date(), false, 12));
            patientRepository.save(new Patient(null, "khdija", new Date(), true, 120));
            patientRepository.save(new Patient(null, "fadma", new Date(), true, 67));
            patientRepository.save(new Patient(null, "hmad", new Date(), false, 87));

            patientRepository.findAll().forEach(patient ->
                    System.out.println(patient.getNom()));
        };
    }

    @Bean
    CommandLineRunner commandLineRunner(JdbcUserDetailsManager jdbcUserDetailsManager) {

        PasswordEncoder passwordEncoder = passwordEncoder();
        return args -> {

            UserDetails u1 = jdbcUserDetailsManager.loadUserByUsername("user11");
            if (u1 == null) {
                jdbcUserDetailsManager.createUser(
                        User.withUsername("user11").password(passwordEncoder.encode("1234")).roles("USER").build()
                );
            }

            UserDetails u2 = jdbcUserDetailsManager.loadUserByUsername("user22");
            if (u2 == null) {
                jdbcUserDetailsManager.createUser(
                        User.withUsername("user22").password(passwordEncoder.encode("1234")).roles("USER").build()
                );
            }

            UserDetails ad = jdbcUserDetailsManager.loadUserByUsername("admin2");
            if (ad == null) {
                jdbcUserDetailsManager.createUser(
                        User.withUsername("admin2").password(passwordEncoder.encode("1234")).roles("USER", "ADMIN").build()
                );
            }
        };
    }

    // Cette méthode crée et retourne un bean de type PasswordEncoder utilisant l'algorithme BCrypt pour chiffrer les mots de passe.
    @Bean
    PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }
}
