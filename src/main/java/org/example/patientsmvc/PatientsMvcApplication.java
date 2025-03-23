package org.example.patientsmvc;

import org.example.patientsmvc.entities.Patient;
import org.example.patientsmvc.repositories.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootApplication
public class PatientsMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatientsMvcApplication.class, args);
    }

    //@Bean
    CommandLineRunner commandLineRunner(PatientRepository patientRepository) {
        return args -> {

            patientRepository.save(new Patient(null, "hassane", new Date(), false, 12));
            patientRepository.save(new Patient(null, "karim", new Date(), true, 120));
            patientRepository.save(new Patient(null, "jaouad", new Date(), true, 67));
            patientRepository.save(new Patient(null, "hmad", new Date(), false, 87));

            patientRepository.findAll().forEach(patient ->
                    System.out.println(patient.getNom()));
        };
    }

    // Cette méthode crée et retourne un bean de type PasswordEncoder utilisant l'algorithme BCrypt pour chiffrer les mots de passe.
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
