package org.example.patientsmvc.sec.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.patientsmvc.sec.entities.AppRole;
import org.example.patientsmvc.sec.entities.AppUser;
import org.example.patientsmvc.sec.repo.AppRoleRepository;
import org.example.patientsmvc.sec.repo.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AppRoleRepository appRoleRepository;
    private AppUserRepository appUserRepository;
    private AppRoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public AppUser addNewUser(String username, String password, String email, String confirmPassword) {

        AppUser appUser = appUserRepository.findByUsername(username);

        if (appUser != null) {
            throw new RuntimeException("User already exists");
        }

        if (!password.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

        appUser = AppUser.builder()
                .userId(UUID.randomUUID().toString())
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();
        AppUser savedAppUser =  appUserRepository.save(appUser);
        return null;
    }

    @Override
    public AppRole addNewRole(String roleName) {

        return null;
    }

    @Override
    public void addRoleToUser(String username, String role) {

    }

    @Override
    public void removeRoleFromUser(String username, String role) {

    }
}
