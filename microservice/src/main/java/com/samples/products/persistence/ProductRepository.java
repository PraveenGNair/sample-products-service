package com.samples.products.persistence;

import com.samples.products.persistence.entity.Product;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {

  List<Product> findByNameIgnoreCaseContaining(String name);
}
