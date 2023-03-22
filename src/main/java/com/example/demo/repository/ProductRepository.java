package com.example.demo.repository;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
//    List<Product> findByStoreIdOrderByCreatedDateDesc(Pageable pageable);
//    List<ProductDTO> findAll();
}