package com.beststore.beststore.controllers;

import java.io.File;
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
import org.springframework.web.multipart.MultipartFile;

import com.beststore.beststore.models.Category;
import com.beststore.beststore.models.CategoryDTO;
import com.beststore.beststore.models.Product;
import com.beststore.beststore.models.ProductDto;
import com.beststore.beststore.service.CategoryRepository;
import com.beststore.beststore.service.ProductsRepository;

@Controller
@RequestMapping("/dashbord")
public class DashbordController {
	
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
	    return "dashbord/index"; 
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
	
	
	
	
	@GetMapping({"/create"})
	public String showCreatePage(Model model) {
	    ProductDto productDto = new ProductDto();
	    model.addAttribute("productDto", productDto); 
	    return "dashbord/create"; 

	}
	
	@PostMapping("/create")
	public String addProduct(@ModelAttribute("productDto") ProductDto productDto) {
	    Product product = new Product();

	    //recuperir de la catégorie
	    Integer categoryId = productDto.getCategory().getId();
	    Category category = cate.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("Catégorie non trouvée"));
	    product.setCategory(category); 

	    //remplir des détails du produit
	    product.setName(productDto.getName());
	    product.setBrand(productDto.getBrand());
	    product.setPrice(productDto.getPrice());
	    product.setDescription(productDto.getDescription());
	    product.setCreatedAt(productDto.getCreatedAt());

	    MultipartFile imageFile = productDto.getImage();

	    if (imageFile != null && !imageFile.isEmpty()) {
	        try {
	            //le chemin de l'image
	        	 String imagePath = "C:/path/to/your/project/src/main/resources/static/images/" + imageFile.getOriginalFilename();
	            File file = new File(imagePath);
                //enregistrer l'image dans le dossier
	            imageFile.transferTo(file);

	            //enregistrer le nom de l'image dans l'entité
	            product.setImageFileName(imageFile.getOriginalFilename());

	            System.out.println("Image enregistrée avec succès : " + imagePath);
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("Erreur lors de l'enregistrement de l'image.");
	        }
	    } else {
	        System.out.println("Aucune image fournie.");
	    }
	    repo.save(product);

	    return "redirect:/dashbord"; 
	

	}
	
	
	
	
	@GetMapping({"/edit/{id}"})//soit passé comme variable
	public String EditPage(Model model, @PathVariable int id) {  // Utilise @PathVariable ici
	    Product product = repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
	    model.addAttribute("product", product);
	    return "dashbord/edit";  // Assure-toi que "products/edit" correspond bien à la vue Thymeleaf
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
	    return "redirect:/dashbord/";  // Change l'URL si nécessaire
	}
	
	
	@GetMapping({"/delete"})
	public String Delete(@RequestParam int id) {
		Product product=repo.findById(id).get();
		repo.delete(product);
		return "redirect:/dashbord/";
	}
	
	
	


}
