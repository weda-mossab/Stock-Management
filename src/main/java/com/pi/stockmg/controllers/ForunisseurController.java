package com.pi.stockmg.controllers;

import com.pi.stockmg.entities.Fournisseur;
import com.pi.stockmg.entities.Produit;
import com.pi.stockmg.repositories.FournisseurRepository;
import com.pi.stockmg.repositories.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/fournisseurs")
public class ForunisseurController {

    @Autowired
    private final FournisseurRepository fournisseurRepository;

    public ForunisseurController(FournisseurRepository fournisseurRepository) {
        this.fournisseurRepository = fournisseurRepository;
    }

    @GetMapping
    public String getAllFrounisseurs(Model model) {
        List<Fournisseur> fournisseurs = fournisseurRepository.findAll();
        model.addAttribute("fournisseurs", fournisseurs);
        return "Fournisseurs";
    }


}
