package org.example.patientsmvc.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.patientsmvc.entities.Patient;
import org.example.patientsmvc.repositories.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@AllArgsConstructor

public class PatientController {

    private PatientRepository patientRepository;

    @GetMapping(path = "/user/index")
    public String patients(Model model,
                           @RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "size", defaultValue = "5") int size,
                           @RequestParam(name = "keyword", defaultValue = "") String keyword
                            ) {

        Page<Patient> pagePatients = patientRepository.findByNomContains(keyword, PageRequest.of(page, size));
        model.addAttribute("ListePatients", pagePatients.getContent());
        model.addAttribute("pages", new int[pagePatients.getTotalPages()]);
        model.addAttribute("pageCurrent", page);
        model.addAttribute("keyword", keyword);

        return "patients";
    }

    @GetMapping("/admin/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(Long id, String keyword, int page){

        patientRepository.deleteById(id);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }


    @GetMapping("/admin/formPatients")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String formPatient(Model model){
        model.addAttribute("patient", new Patient());
        return "formPatients";
    }

    @PostMapping("/admin/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword){
        if(bindingResult.hasErrors()){
            return "formPatients";
        }
        patientRepository.save(patient);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }

    // renvoie la liste des patient au format JSON
    @GetMapping("/patients")
    @ResponseBody
    public List<Patient>listPatients(){

        return patientRepository.findAll();
    }

    @GetMapping("/admin/editPatient")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editPatient(Model model, Long id, String keyword, int page){

        Patient patient = patientRepository.findById(id).orElse(null);

        if(patient == null) throw new RuntimeException("Patient not found");

        model.addAttribute("patient", patient);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);

        return "editPatient";
    }

    // si je met rien, il vas rediriger vers index
    @GetMapping("/")
    public String home(){
        return "redirect:/user/index";
    }


}


















// getContent() : pour récupérer  "? la liste ?" des éléments contenus dans une page de résultats paginée.

/*
model.addAttribute("ListePatients", pagePatients.getContent()); ajoute cette liste au modèle
avec le nom "ListePatients". Ce modèle sera ensuite utilisé par la vue (par exemple, une page
Thymeleaf ou JSP) pour afficher la liste des patients.
 */

/*
model.addAttribute("pages", new int[pagePatients.getTotalPages()]); : Cette ligne crée un tableau
d'entiers de taille égale au nombre total de pages,puis l'ajoute au modèle pour
permettre à la vue de générer des liens ou indicateurs de pagination.
 */