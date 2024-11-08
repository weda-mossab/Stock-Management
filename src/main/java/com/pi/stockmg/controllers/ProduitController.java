package com.pi.stockmg.controllers;

import java.util.List;

import com.pi.stockmg.entities.Fournisseur;
import com.pi.stockmg.repositories.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.pi.stockmg.entities.Produit;
import com.pi.stockmg.repositories.ProduitRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/produits")
public class ProduitController {

    @Autowired
    private final ProduitRepository produitRepository;
    private final FournisseurRepository fournisseurRepository;

    public ProduitController(ProduitRepository produitRepository, FournisseurRepository fournisseurRepository) {
        this.produitRepository = produitRepository;
        this.fournisseurRepository = fournisseurRepository;
    }

    // Get all products
    @GetMapping
    public String getAllProducts(Model model) {
        List<Produit> produits = produitRepository.findAll();
        List<Fournisseur> fournisseurs = fournisseurRepository.findAll();
        model.addAttribute("produits", produits); // Using "produits" consistently
        model.addAttribute("fournisseurs", fournisseurs); // Add suppliers to the model
        return "Produits"; // Return the view name for displaying the list of products
    }

    // Get product by ID
    @GetMapping("/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found")); // Could be a custom exception
        model.addAttribute("produit", produit); // Using "produit" for consistency
        return "Produits"; // Return the view for displaying the single product
    }

    // Create product
    @PostMapping
    public String createProduct(@ModelAttribute Produit produit) {
        // Add validation if necessary
        produitRepository.save(produit);
        return "redirect:/produits"; // Redirect after successful creation
    }

    // Update product
    @PutMapping("{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Produit produit) {
        Produit existingProduit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Set only the fields that can be updated
        existingProduit.setNom(produit.getNom());
        existingProduit.setDescription(produit.getDescription());
        existingProduit.setPrix(produit.getPrix());
        existingProduit.setQuantite(produit.getQuantite());

        produitRepository.save(existingProduit); // Save the updated product
        return "redirect:/produits"; // Redirect after successful update
    }

    // Delete product
    @GetMapping("/deleteProduct")
    public String deleteProduct(@RequestParam("id") Long id) {
        produitRepository.deleteById(id);
        return "redirect:/produits"; // Redirect after deleting the product
    }

    // Search products
    @GetMapping("/searchProducts")
    public String searchProducts(@RequestParam(required = false) String name, Model model) {
        List<Produit> produits;
        if (name == null || name.isEmpty()) {
            produits = produitRepository.findAll(); // Fetch all if no name provided
        } else {
            produits = produitRepository.findByNomStartingWith(name); // Search by name prefix
        }
        model.addAttribute("produits", produits);
        return "produits"; // Return the view for displaying search results
    }
}
