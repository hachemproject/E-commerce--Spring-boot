package com.beststore.beststore.controllers;

import java.io.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.beststore.beststore.models.Product;
import com.beststore.beststore.models.Category;
import com.beststore.beststore.models.CategoryDTO;
import com.beststore.beststore.models.ProductDto;
import com.beststore.beststore.service.CategoryRepository;
import com.beststore.beststore.service.ProductsRepository;

@Controller
@RequestMapping("/accueil")//le chemin dans URL

public class ProductsController {

	@Autowired
	private ProductsRepository repo;
	
	@Autowired
	private CategoryRepository cate;

	
	
	@GetMapping({"", "/"})
	public String showProductList(Model model) {
	    List<Product> products = repo.findAll();
	    List<ProductDto> productDTOs = products.stream().map(this::convertToDTO).collect(Collectors.toList());
	   List<Category> categories = cate.findAll();
	    model.addAttribute("categories", categories);
	    model.addAttribute("products", productDTOs);
	    return "client/accueil"; 
	}


	private ProductDto convertToDTO(Product product) {
	    ProductDto dto = new ProductDto();
	    dto.setId(product.getId());
	    dto.setName(product.getName());
	    dto.setBrand(product.getBrand());
	    dto.setPrice(product.getPrice());
	    dto.setDescription(product.getDescription());
	    dto.setCreatedAt(product.getCreatedAt());
	    dto.setImageFileName(product.getImageFileName());

	        CategoryDTO categoryDto = new CategoryDTO();
	        categoryDto.setId(product.getCategory().getId());
	        categoryDto.setName(product.getCategory().getName());
	        dto.setCategory(categoryDto); 
	    
       return dto;
	}
	
	
	/*
	@GetMapping({"/category"})
	public String showCategoryList(Model model) {
	    List<Category> category = cate.findAll();
	    model.addAttribute("category", category);
	    return "client/accueil"; 
	}
	
	*/
	
	@GetMapping({"/create"})
	public String showCreatePage(Model model) {
	    ProductDto productDto = new ProductDto();
	    model.addAttribute("productDto", productDto); 
	    return "products/CreateProduct"; 
	}

	@PostMapping("/create")
	public String addProduct(@ModelAttribute("productDto") ProductDto productDto) {
	    Product product = new Product();

	    // Récupération de la catégorie
	    Integer categoryId = productDto.getCategory().getId();
	    Category category = cate.findById(categoryId)
	        .orElseThrow(() -> new IllegalArgumentException("Catégorie non trouvée"));
	    product.setCategory(category); 

	    // Remplissage des détails du produit
	    product.setName(productDto.getName());
	    product.setBrand(productDto.getBrand());
	    product.setPrice(productDto.getPrice());
	    product.setDescription(productDto.getDescription());
	    product.setCreatedAt(productDto.getCreatedAt());

	    MultipartFile imageFile = productDto.getImage();
	    
	    
	            String imagePath = "C:/path/to/your/project/src/main/resources/static/images/" + imageFile.getOriginalFilename();
	            File file = new File(imagePath);
	            product.setImageFileName(imageFile.getOriginalFilename());
	            System.out.println("Image enregistrée avec succès : " + imagePath);
	    repo.save(product);
	    return "redirect:/accueil"; 
	}
	
	

}
