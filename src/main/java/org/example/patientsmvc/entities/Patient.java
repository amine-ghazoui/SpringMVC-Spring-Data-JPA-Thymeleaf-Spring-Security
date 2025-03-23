package org.example.patientsmvc.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor  @AllArgsConstructor

public class Patient {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // ces annotations pour la validation dans la saisie de nouveaux patient (formulaire) (la méme chose pour l'annotation dans score)
    @NotEmpty
    @Size(min = 2, max = 40)
    private String nom;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateNaissance;
    private Boolean malade;
    @DecimalMin("10")
    private int score;


}

/*
pour faire la validation on fait trois chose :
- ajouter la dépendance spring boot validation
- les annotations de validation
- au niveau de controller, il faut utiliser l'annotaion @Valid et BindingResult= si il yas des erreurs
- au niveaux de la page HTML il faut utiliser th:Errors
 */
