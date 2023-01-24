package com.example.entity;


/*import com.fasterxml.jackson.annotation.JsonFormat.Shape;*/
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "membre")
public class Membre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    @Pattern(regexp = "^[a-zA-Z\\-\\s]{2,18}$",
            message = "first name must be of 2 to 18 length with no special characters and numbers")
    @NotNull(message = "Must not be empty")
    private String nom;

    @Column(name = "prenom")
    @Pattern(regexp = "^[a-zA-Z\\-\\s]{2,18}$",
            message = "last name must be of 2 to 18 length with no special characters and numbers")
    @NotNull(message = "Must not be empty")
    private String prenom;

    @Column(name = "type_Abonnement")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Must not be empty")
    private TypeAbonnement typeAbonnement;

    @Column(name = "birthDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Must not be empty")
    @Past
    private Date dateNaiss;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "membre")
    List<Reservation> reservations = new ArrayList<>();

    public Membre(String nom, String prenom, Date dateNaiss, TypeAbonnement typeAbonnement) {

        this.nom = nom;
        this.prenom = prenom;
        this.dateNaiss = dateNaiss;
        this.typeAbonnement = typeAbonnement;

    }

    public Membre() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public TypeAbonnement getTypeAbonnement() {
        return typeAbonnement;
    }

    public void setTypeAbonnement(TypeAbonnement typeAbonnement) {
        this.typeAbonnement = typeAbonnement;
    }

    public Date getDateNaiss() {
        return dateNaiss;
    }

    public void setDateNaiss(Date dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

}
