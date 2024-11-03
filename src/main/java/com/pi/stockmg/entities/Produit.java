package com.pi.stockmg.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity 
@Data 
@NoArgsConstructor
@AllArgsConstructor

public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String nom ;
    private String description;
    private Double prix;
    private Integer quantite;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    @ToString.Exclude // Évite la récursion infinie si Fournisseur a une relation bidirectionnelle
    private Fournisseur fournisseur;
}
