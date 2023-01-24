/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.entity.Membre;
import com.example.entity.Reservation;
import com.example.repository.MembreRepository;
import com.example.repository.ReservationRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
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
public class ReservationController {

    @Autowired
    private MembreRepository membreRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/reservations")
    public String listReservations(Model model) {
        model.addAttribute("listMembres", membreRepository.findAll());
        model.addAttribute("listReservations", reservationRepository.findAll());

        return "reservations"; // returns the view name, ie the Thymele"af template 
    }

    @GetMapping("/reservations/filter1")
    public String viewHomePageFilter1(Model model, @RequestParam(name = "number", defaultValue = "-1") int num) {

        if (num > 0 && num < 25) {

            model.addAttribute("listReservations", reservationRepository.findByNumTerrain(num));
            model.addAttribute("filter1", num);

        } else {
            model.addAttribute("listReservations", reservationRepository.findAll());
        }
        return "reservations";
    }

    @GetMapping("/reservations/filter2")
    public String viewHomePageFilter2(Model model, @RequestParam(name = "dateRes", defaultValue = "") String dn) throws ParseException {
        if (!dn.equals("")) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            model.addAttribute("listReservations", reservationRepository.findByDateRes(sdf.parse(dn)));
        } else {
            model.addAttribute("listReservations", reservationRepository.findAll());
        }

        return "reservations";
    }

    @GetMapping("/reservations/new")
    public String showNewReservationForm(Model model) {

        Reservation reservation = new Reservation();

        model.addAttribute("reservation", reservation);
        model.addAttribute("listMembres", membreRepository.findAll());
        model.addAttribute("listReservations", reservationRepository.findAll());
        return "new_reservation";
    }

    @PostMapping("/reservations/saveNew")
    public String saveNewReservation(@ModelAttribute("reservation") @Valid Reservation reservation, Errors errors, Model model) {
       
        // Vérifiez manuellement si l'heure de début est inférieure à l'heure de fin
        if (reservation.getHeureDeb().isAfter(reservation.getHeureFin())) {
            errors.rejectValue("heureDeb", "heure.debut.invalide", "L'heure de début doit être inférieure à l'heure de fin");
        }

        //vérifiez manuellement si la réservation dure au moins une heure
        if (Duration.between(reservation.getHeureDeb(), reservation.getHeureFin()).toMinutes() < 60) {
            errors.rejectValue("heureFin", "heure.fin.invalide", "La réservation doit durer au moins une heure");
        }
        if (errors.hasErrors()) {
            model.addAttribute("listReservations", reservationRepository.findAll());
               model.addAttribute("listMembres", membreRepository.findAll());
            return "new_reservation";
        }
        List<Reservation> myList1 = reservationRepository.findByDateResAndNumTerrain(reservation.getDateRes(), reservation.getNumTerrain());
        List<Reservation> myList2 = reservationRepository.findByHeureDebAndHeureFin(reservation.getHeureDeb(), reservation.getHeureFin());
        if (!myList1.isEmpty() && !myList2.isEmpty()) {
            model.addAttribute("listMembres", membreRepository.findAll());
            model.addAttribute("listReservations", reservationRepository.findAll());
            model.addAttribute("message", "reservation  complet dans cette date à cette heure essayer à nouveau !");
            return "new_reservation";
        } else {
            reservationRepository.save(reservation);
        }

        return "redirect:/reservations";
    }

    @GetMapping("/reservations/update")
    public String showUpdateReservationForm(@RequestParam(name = "id") long id, Model model) {
        Optional<Reservation> optional = reservationRepository.findById(id);
        Reservation reservation = null;
        if (optional.isPresent()) {
            reservation = optional.get();
        } else {
            throw new RuntimeException("Reservation not found for id:: " + id);
        }

        //set membre as a model attribute to pre-populate the form
        model.addAttribute("reservation", reservation);
        model.addAttribute("listReservations", reservationRepository.findAll());
        model.addAttribute("listMembres", membreRepository.findAll());
        return "update_reservation";
    }

    @PostMapping("/reservations/saveUpdate")
    public String saveUpdateReservation(@ModelAttribute("reservation") @Valid Reservation reservation, Errors errors, Model model) {
        
         if (reservation.getHeureDeb().isAfter(reservation.getHeureFin())) {
            errors.rejectValue("heureDeb", "heure.debut.invalide", "L'heure de début doit être inférieure à l'heure de fin");
        }

        //vérifiez manuellement si la réservation dure au moins une heure
        if (Duration.between(reservation.getHeureDeb(), reservation.getHeureFin()).toMinutes() < 60) {
            errors.rejectValue("heureFin", "heure.fin.invalide", "La réservation doit durer au moins une heure");
        }
        
        if (errors.hasErrors()) {
            model.addAttribute("listReservations", reservationRepository.findAll());
            model.addAttribute("listMembres", membreRepository.findAll());
            
            return "update_reservation";
        }
        
        
        List<Reservation> myList1 = reservationRepository.findByDateResAndNumTerrain(reservation.getDateRes(), reservation.getNumTerrain());
        List<Reservation> myList2 = reservationRepository.findByHeureDebAndHeureFin(reservation.getHeureDeb(), reservation.getHeureFin());
       // List<Reservation> myList3 = reservationRepository.findByNumTerrain(reservation.getNumTerrain());
       // List<Reservation> myList4 =reservationRepository.findByHeureFin(reservation.getHeureFin());
        if (!myList1.isEmpty() && !myList2.isEmpty() ) {
            if ((myList1.get(0).getId()!= reservation.getId()) && (myList2.get(0).getId()!= reservation.getId()) ) {
                
                 model.addAttribute("listMembres", membreRepository.findAll());
                 model.addAttribute("listReservations", reservationRepository.findAll());
                model.addAttribute("message", "reservation  complet dans cette date à cette heure essayer à nouveau !!");
                return "update_reservation";
            }
        }
        reservationRepository.save(reservation);
        return "redirect:/reservations";
    }

    @GetMapping("/reservations/delete")
    public String deleteReservation(@RequestParam(name = "id") long id) {

        reservationRepository.deleteById(id);
        return "redirect:/reservations";
    }

}
