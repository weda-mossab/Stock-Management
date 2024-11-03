package com.pi.stockmg.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pi.stockmg.entities.Produit;

public interface ProduitRepository extends JpaRepository<Produit, Integer>{
    List<Produit> findByNomContainingIgnoreCase(String nom);

}
