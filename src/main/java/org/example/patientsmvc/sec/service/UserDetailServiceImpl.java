package org.example.patientsmvc.sec.service;

import lombok.AllArgsConstructor;
import org.example.patientsmvc.sec.entities.AppUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
// this class is used to load user details from the database
public class UserDetailServiceImpl implements UserDetailsService {

    private AccountService accountService;

    // this method is used to load user details by username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // load user from the database
        AppUser appUser = accountService.loadUserByUsername(username);
        // if user is not found, throw exception
        if (appUser != null) {
            throw new UsernameNotFoundException(String.format("Username %s not found", username));
        }
        // if user is found, create UserDetails object (Transforme la liste des rôles de l'utilisateur en un tableau de chaînes de caractères)
        String[] roles = appUser.getRoles().stream().map(r -> r.getRole()).toArray(String[]::new);

        // create UserDetails object
        UserDetails userDetails = User
                .withUsername(appUser.getUsername())
                .password(appUser.getPassword())
                .roles(roles).build();

        return userDetails;
    }
}
