/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.entity.Membre;
import com.example.entity.Reservation;
import com.example.entity.TypeAbonnement;
import com.example.repository.MembreRepository;
import com.example.repository.ReservationRepository;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MembreController {

    @Autowired
    private MembreRepository membreRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/membres")
    public String listMembres(Model model) {
        model.addAttribute("listMembres", membreRepository.findAll());
        model.addAttribute("listReservations", reservationRepository.findAll());

        return "membres"; // returns the view name, ie the Thymele"af template 
    }

    @GetMapping("/membres/new")
    public String showNewMembreForm(Model model) {

        TypeAbonnement[] types = TypeAbonnement.values();
        Membre membre = new Membre();
        model.addAttribute("membre", membre);
        model.addAttribute("types", types);
        model.addAttribute("listMembres", membreRepository.findAll());
        return "new_membre";
    }

    @PostMapping("/membres/saveNew")
    public String saveNewMembre(@ModelAttribute("membre") @Valid Membre membre, Errors errors, Model model) {
        TypeAbonnement[] types = TypeAbonnement.values();
        if (errors.hasErrors()) {
            model.addAttribute("types", types);
            model.addAttribute("listMembres", membreRepository.findAll());
            return "new_membre";
        }
        List<Membre> myList = membreRepository.findByNomAndPrenom(membre.getNom(), membre.getPrenom());

        if (!myList.isEmpty()) {
            model.addAttribute("types", types);
            model.addAttribute("listMembres", membreRepository.findAll());
            model.addAttribute("message", "Membre with same nom and prenom already exists !");
            return "new_membre";
        } else {
            membreRepository.save(membre); //save membre to DB
        }

        return "redirect:/membres";
    }

    @GetMapping("/membres/update")
    public String showUpdateMembreForm(@RequestParam(name = "id") long id, Model model) {
        TypeAbonnement[] types = TypeAbonnement.values();
        Optional<Membre> optional = membreRepository.findById(id);
        Membre membre = null;
        if (optional.isPresent()) {
            membre = optional.get();
        } else {
            throw new RuntimeException("Membre not found for id:: " + id);
        }

        //set membre as a model attribute to pre-populate the form
        model.addAttribute("membre", membre);
        model.addAttribute("types", types);
        model.addAttribute("listMembres", membreRepository.findAll());
        return "update_membre";
    }

    @PostMapping("/membres/saveUpdate")
    public String saveUpdateMembre(@ModelAttribute("membre") @Valid Membre membre, Errors errors, Model model) {
        TypeAbonnement[] types = TypeAbonnement.values();
        if (errors.hasErrors()) {
            model.addAttribute("types", types);
            model.addAttribute("listMembres", membreRepository.findAll());
            return "update_membre";
        }
        List<Membre> myList = membreRepository.findByNomAndPrenom(membre.getNom(), membre.getPrenom());

        if (!myList.isEmpty()) {
            if ((myList.get(0).getId() != membre.getId())) {
                model.addAttribute("listMembres", membreRepository.findAll());
                  model.addAttribute("types", types);
                model.addAttribute("message", "Mmebre with same nom and prenom already exists !");
                return "update_membre";
            }
        }
        membreRepository.save(membre); //save membre to DB
        return "redirect:/membres";
    }

    @GetMapping("/membres/delete")
    public String deleteMembre(@RequestParam(name = "id") long id, Model model) {
        Optional<Membre> optional = membreRepository.findById(id);
        Membre membre = null;

        if (optional.isPresent()) {
            membre = optional.get();
            List<Reservation> myList = membre.getReservations();
            if (!myList.isEmpty()) {

                model.addAttribute("listMembres", membreRepository.findAll());
                model.addAttribute("listMembres", reservationRepository.findAll());
                reservationRepository.deleteAll(myList);
                membreRepository.deleteById(id);

            } else {
                membreRepository.deleteById(id);
            }
        } else {
            throw new RuntimeException("Membre not found for id: " + id);
        }

        return "redirect:/membres";
    }

}
