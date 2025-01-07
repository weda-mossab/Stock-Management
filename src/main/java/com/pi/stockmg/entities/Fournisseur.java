package com.pi.stockmg.entities;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "fournisseurs")
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nom;
    private String adresse; 
    private String email;
    private String Tel;

    @OneToMany(mappedBy = "fournisseur", cascade = CascadeType.ALL)
    private List<Produit> produits;

}
