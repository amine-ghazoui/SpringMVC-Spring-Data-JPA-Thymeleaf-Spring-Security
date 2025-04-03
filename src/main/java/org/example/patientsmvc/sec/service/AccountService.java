package org.example.patientsmvc.sec.service;

import org.example.patientsmvc.sec.entities.AppRole;
import org.example.patientsmvc.sec.entities.AppUser;

public interface AccountService{

    AppUser addNewUser(String username, String password, String email, String confirmPassword);
    AppRole addNewRole(String role);
    void addRoleToUser(String username, String role);
    void removeRoleFromUser(String username, String role);
    AppUser loadUserByUsername(String username);
}
