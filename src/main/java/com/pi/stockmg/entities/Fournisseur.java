package com.pi.stockmg.entities;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nom;
    private String adresse; 
    private String email;
    private String Tel;

    @OneToMany(mappedBy = "fournisseur", cascade = CascadeType.ALL)
    @ToString.Exclude // Évite la récursion infinie dans les méthodes toString
    private List<Produit> produits; // Liste des produits fournis par ce fournisseur

}
