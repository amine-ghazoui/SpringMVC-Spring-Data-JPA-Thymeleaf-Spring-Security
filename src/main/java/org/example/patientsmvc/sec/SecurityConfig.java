package org.example.patientsmvc.sec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {



    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        //(Spécifier le data source)dans quelle DB ou se trouve les utilisateurs et les roles
        //dite a springSecurity quand un utilisteur va saisir son nom et le mot de passe, tu vas chercher l'utilisateur dans ce dataSource
        // il doit créer deux table (stocker les utilisateurs et les roles)
        return new JdbcUserDetailsManager(dataSource);
    }




    /*
    Déclaration d'un objet PasswordEncoder pour chiffrer les mots de passe.
    Cet objet est utilisé pour encoder les mots de passe avant stockage et
    pour vérifier la correspondance entre un mot de passe saisi et un mot de passe haché.
     */
    private PasswordEncoder passwordEncoder;

    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // définir les utilisateurs qui on le droit d'accéder a l'application (il faut définir ou est ce que spring security va chercher les utilisateur )
    //@Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode("1234");
        System.out.println(encodedPassword);
        return new InMemoryUserDetailsManager(
                User.withUsername("user1").password(encodedPassword).roles("USER").build(),
                User.withUsername("user2").password(encodedPassword).roles("USER").build(),
                User.withUsername("admin").password(encodedPassword).roles("USER", "ADMIN").build()
        );
    }


    // contient Bean : c-à-d c'est une méthode qui s'execute au démarage
    /** Configure l'authentification et l'autorisation des utilisateurs selon leurs rôles. */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // Configure l'authentification par formulaire avec une page de connexion personnalisée accessible à tous.
        httpSecurity.formLogin(form -> form
                .loginPage("/login")
                // Redirige vers la page d'accueil après une connexion réussie
                .defaultSuccessUrl("/")
                .permitAll());
        httpSecurity.rememberMe(remember -> remember
                .key("votreCleSecrete") // Clé secrète pour signer le cookie
                .tokenValiditySeconds(86400) // Durée de validité du cookie (ici 1 jour)
        );
        // Autorise l'accès public aux ressources WebJars (CSS, JS, icônes, etc.)
        httpSecurity.authorizeHttpRequests(ar -> ar.requestMatchers("/webjars/**", "/h2-console/**").permitAll());

        /*
        on peux uriliser une autre solution, c'est d'utilisée les annotations
        (@EnableMethodSecurity) (et dans controller on ajoute @PreAuthorize()) (car si nous avons
        pas faire ca -> nous avons protéger les resources coté client mais coté serveur non !!)
         */
        /*
        // Autorise uniquement les "ADMIN" à accéder à "/admin/**"
            httpSecurity.authorizeHttpRequests(ar -> ar.requestMatchers("/admin/**").hasRole("ADMIN"));
        // Autorise uniquement les "USER" à accéder à "/user/**"
            httpSecurity.authorizeHttpRequests(ar -> ar.requestMatchers("/user/**").hasRole("USER"));
        */

        // Toutes les autres requêtes nécessitent une authentification
        httpSecurity.authorizeHttpRequests(ar -> ar.anyRequest().authenticated());
        // Gestion des exceptions
        httpSecurity.exceptionHandling(eh -> eh
                .accessDeniedPage("/notAuthorized") // Redirige vers une page d'erreur 403
        );
        // Construit la configuration de sécurité
        return httpSecurity.build();
    }

}





















/*
authorizeHttpRequests() est une méthode utilisée dans la configuration de Spring Security
pour définir les règles d'autorisation des requêtes HTTP entrantes.
- requestMatchers() : Une méthode utilisée pour définir les chemins d'URL à sécuriser.
- hasRole("ROLE_NAME") : Exige un rôle spécifique pour accéder à une ressource.
 */


/*
Le rôle de rememberMe() est de permettre aux utilisateurs de rester authentifiée entre les sessions
 en utilisant un cookie persistant, évitant ainsi de devoir se reconnecter à chaque visite.
 */
