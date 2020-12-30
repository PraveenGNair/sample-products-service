package com.samples.products.persistence;

import com.samples.products.persistence.entity.ProductOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOption, UUID> {

  List<ProductOption> findAllByProductId(UUID productId);

  Optional<ProductOption> findProductOptionByIdAndProductId(UUID id, UUID productId);

  void deleteByIdAndProductId(UUID id, UUID productId);

  boolean existsByIdAndProductId(UUID id, UUID productId);

}
