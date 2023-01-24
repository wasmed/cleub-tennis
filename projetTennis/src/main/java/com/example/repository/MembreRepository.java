/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.repository;

import com.example.entity.Membre;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


 @Repository
public interface MembreRepository extends JpaRepository<Membre,Long>{
  
    public List<Membre> findByNomAndPrenom(String nom, String prenom);
 //  public List<Membre> findByBirthDateIs(Date dateNaiss);
   /* public List<Membre> findByBirthDateBetween(Date date1, Date date2);
   
    public List<Membre> findBytypeAbonnement(TypeAbonnement type);*/
    
    
    
}
