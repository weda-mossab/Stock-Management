package com.pi.stockmg.controllers;

import java.util.List;

import com.pi.stockmg.entities.Fournisseur;
import com.pi.stockmg.repositories.FournisseurRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import com.pi.stockmg.entities.Produit;
import com.pi.stockmg.repositories.ProduitRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/produits")
public class ProduitController {

    @Autowired
    private final ProduitRepository produitRepository;
    private final FournisseurRepository fournisseurRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProduitController.class);

    public ProduitController(ProduitRepository produitRepository, FournisseurRepository fournisseurRepository) {
        this.produitRepository = produitRepository;
        this.fournisseurRepository = fournisseurRepository;
    }

    // Get all products
    @GetMapping
    public String getAllProducts(Model model, HttpServletRequest request, @RequestParam(required = false) String searchTerm) {
        String authenticatedUser = (String) request.getSession().getAttribute("authenticatedUser");

        if (authenticatedUser == null) {
            return "redirect:/signin";
        }

        List<Produit> produits = produitRepository.searchProducts(searchTerm);
        List<Fournisseur> fournisseurs = fournisseurRepository.findAll();
        model.addAttribute("produits", produits); // Using "produits" consistently
        model.addAttribute("fournisseurs", fournisseurs); // Add suppliers to the model
        model.addAttribute("searchTerm", searchTerm);
        return "Produits"; // Return the view name for displaying the list of products
    }

    // Helper method to avoid code duplication
    private Produit getProductByIdOrThrow(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));
    }

    // Get product by ID
    @GetMapping("/{id}")
    @ResponseBody
    public Produit getProductById(@PathVariable Long id) {
        return getProductByIdOrThrow(id);  // Call the helper method here
    }

    // Create product
    @PostMapping
    public String createProduct(@ModelAttribute Produit produit) {
        // Add validation if necessary
        produitRepository.save(produit);
        return "redirect:/produits"; // Redirect after successful creation
    }

    // Update product /updateProduct/{id}
    @PostMapping("/updateProduct")
    public String updateProduct(@RequestParam Long id, @ModelAttribute Produit produit) {
        logger.info("Reçu une requête pour mettre à jour le produit avec l'id : " + id);

        // Vérifier si le produit existe
        Produit existingProduct = getProductByIdOrThrow(id);

        // Mettre à jour les champs du produit existant
        existingProduct.setNom(produit.getNom());
        existingProduct.setDescription(produit.getDescription());
        existingProduct.setQuantite(produit.getQuantite());
        existingProduct.setPrix(produit.getPrix());
        if (produit.getFournisseur() != null && produit.getFournisseur().getId() != null) {
            existingProduct.setFournisseur(produit.getFournisseur());
        }
        // Sauvegarder le produit mis à jour
        produitRepository.save(existingProduct);
        return "redirect:/produits";
    }

    // Delete product
    @PostMapping("/deleteProducts")
    public String deleteProducts(@RequestParam("ids") String ids) {
        // Split the comma-separated list of IDs and delete each product
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            produitRepository.deleteById(Long.parseLong(id.trim()));
        }
        return "redirect:/produits"; // Redirect after deleting the products
    }

    @GetMapping("/searchProduct")
    public String searchProducts(@RequestParam String searchTerm, Model model) {
        List<Produit> produits = produitRepository.searchProducts(searchTerm);
        model.addAttribute("produits", produits);
        model.addAttribute("searchTerm", searchTerm);
        return "Produits";
    }
}
