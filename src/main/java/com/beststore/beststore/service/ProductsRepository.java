package com.beststore.beststore.service;

import org.springframework.data.jpa.repository.JpaRepository;


import com.beststore.beststore.models.Product;

public interface ProductsRepository extends JpaRepository<Product, Integer>  {

}
