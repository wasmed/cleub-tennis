/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.example.repository;


import com.example.entity.Reservation;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
      public List<Reservation> findByNumTerrain(int numTerrain);
       public List<Reservation> findByDateRes( Date date);
        /*public List<Reservation> findByHeureDeb( LocalTime time);
        public List<Reservation> findByHeureFin( LocalTime time);*/
       public List<Reservation> findByDateResAndNumTerrain(Date date, int  numTerrain);
        public List<Reservation> findByHeureDebAndHeureFin(LocalTime time1, LocalTime time2);
        
        
     // @Query("select r from reservation r where r.membre_id = ?1")
    //  public List<Reservation> chercherReservationParMembre(Long id);
      
     
}
