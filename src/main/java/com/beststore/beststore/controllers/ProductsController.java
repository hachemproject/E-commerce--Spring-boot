package com.beststore.beststore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.beststore.beststore.models.Product;
import com.beststore.beststore.models.ProductDto;
import com.beststore.beststore.service.ProductsRepository;

@Controller
@RequestMapping("/a")//le chemin dans URL

public class ProductsController {

	@Autowired
	private ProductsRepository repo;
	
	@GetMapping({"","/"})
	public String showProductList(Model model) {
	    List<Product> c = repo.findAll(); // Récupération de tous les produits
	    model.addAttribute("a", c); // Ajout de la liste des produits au modèle
	    return "products/index"; // Chemin vers la vue
	}
	
	

	
	@GetMapping({"/create"})
	public String showCreatePage(Model model) {
	    ProductDto productDto = new ProductDto();
	    model.addAttribute("productDto", productDto); // Assurez-vous que le nom est 'productDto'
	    return "products/CreateProduct"; 
	}
	
	@PostMapping("/create")//que cette méthode doit être appelée lorsque l'application reçoit URL /a/create
    public String addProduct(@ModelAttribute("productDto") ProductDto productDto) {
		Product product = new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setCreatedAt(productDto.getCreatedAt());
		
        repo.save(product); // Sauvegarde le produit dans la base de données
        return "redirect:/a/"; // Redirige vers la liste des produits après l'ajout
    }
	
	
	
	
	//@GetMapping({"/edit"})<a th:href="@{/a/edit(id=${prod.id})}" class="btn btn-primary">Edit</a> utilison @RequestParam soit passé comme un paramètre 
	
	@GetMapping({"/edit/{id}"})//soit passé comme variable
	public String EditPage(Model model, @PathVariable int id) {  // Utilise @PathVariable ici
	    Product product = repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
	    model.addAttribute("product", product);
	    return "products/edit";  // Assure-toi que "products/edit" correspond bien à la vue Thymeleaf
	}
	
	@PostMapping({"/edit/{id}"})//Cela lie les données du formulaire soumis à un objet Product
	public String updateProduct(@PathVariable int id, @ModelAttribute Product product) {
	    Product existingProduct = repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

	    existingProduct.setName(product.getName());
	    existingProduct.setPrice(product.getPrice());
	    existingProduct.setBrand(product.getBrand());
	    existingProduct.setCategory(product.getCategory());
	    existingProduct.setDescription(product.getDescription());
	    existingProduct.setCreatedAt(product.getCreatedAt());


	    repo.save(existingProduct);
	    return "redirect:/a/";  // Change l'URL si nécessaire
	}
	
	@GetMapping({"/delete"})
	public String Delete(@RequestParam int id) {
		Product product=repo.findById(id).get();
		repo.delete(product);
		return "redirect:/a/";
	}
	
	
	
}
