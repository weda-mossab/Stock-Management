package com.pi.stockmg.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.pi.stockmg.entities.Produit;
import com.pi.stockmg.repositories.ProduitRepository;

@Controller
@RequestMapping("/produits")

public class ProduitController {
    private final ProduitRepository produitRepository;

    @Autowired
    public ProduitController(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    @GetMapping
    public String GetAllProduits(Model model) {
        List<Produit> produits = produitRepository.findAll();
        model.addAttribute("produits", produits);
        return "Produits.html";
    }

    // Créer un produit
    @PostMapping String createProduit (@ModelAttribute Produit produit){
        produitRepository.save(produit);
        return "redirect:/Produits";
    }

    // Afficher un produit par ID
    @GetMapping("/{id}")
    public String getProduitById(@PathVariable Integer id, Model model) {
        Produit produit = produitRepository.findById(id).orElse(null);
        model.addAttribute("produit", produit);
        return "Produits.html"; 
    }
    
    // Modifier un produit existant
    @PutMapping("/{id}")
    public String updateProduit(@PathVariable Integer id, @ModelAttribute Produit produitDetails) {
        Produit produit = produitRepository.findById(id).orElse(null);
        if (produit != null) {
            produit.setNom(produitDetails.getNom());
            produit.setDescription(produitDetails.getDescription());
            produit.setPrix(produitDetails.getPrix());
            produit.setQuantite(produitDetails.getQuantite());
            produitRepository.save(produit);
        }   
        return "redirect:/Produits";
    }

    // Supprimer un produit
    @DeleteMapping("/{id}")
    public String deleteProduit(@PathVariable Integer id) {
        produitRepository.deleteById(id);
        return "redirect:/Produits"; // Rediriger vers la liste des produits
    }

    @GetMapping("/search")
    public String searchProduits(@RequestParam("query") String query, Model model) {
        List<Produit> produits = produitRepository.findByNomContainingIgnoreCase(query); // Méthode de recherche dans le repository
        model.addAttribute("produits", produits);
        model.addAttribute("query", query); // Pour préremplir le champ de recherche si besoin
        return "Produits"; // Renvoyer à la vue qui affiche les produits
    }
}
