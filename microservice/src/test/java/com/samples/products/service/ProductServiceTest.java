package com.samples.products.service;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.samples.products.model.ProductOptions;
import com.samples.products.model.Products;
import com.samples.products.exception.ProductNotFoundException;
import com.samples.products.persistence.ProductOptionRepository;
import com.samples.products.persistence.ProductRepository;
import com.samples.products.persistence.entity.Product;
import com.samples.products.persistence.entity.ProductOption;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

  @Mock
  private ProductRepository productRepository;

  @Mock
  private ProductOptionRepository productOptionRepository;

  private ProductService productService;

  @Before
  public void setup() {
    productService = new ProductService(productRepository, productOptionRepository);
    reset();
  }

  @Test
  public void shouldSaveNewProduct() {
    // given
    UUID productId = UUID.randomUUID();
    Product product = new Product("product 1", "product description 1", BigDecimal.ONE,
        BigDecimal.TEN);
    product.setId(productId);
    when(productRepository.save(any(Product.class))).thenReturn(product);

    // when
    Product savedProduct = productService.saveProduct(product);

    // then
    assertNotNull(savedProduct);
    verify(productRepository).save(product);
  }

  @Test
  public void shouldFindAllProducts() {
    // given
    Product product1 = new Product("product 1",
        "product description 1", BigDecimal.ONE,
        BigDecimal.TEN);
    product1.setId(UUID.randomUUID());
    Product product2 = new Product("product 2",
        "product description two", BigDecimal.ONE,
        BigDecimal.ONE);
    product2.setId(UUID.randomUUID());
    Products products = new Products();
    products.setItems(asList(product1, product2));
    when(productRepository.findAll()).thenReturn(products.getItems());

    //when
    Products productList = productService.findAllProducts();
    //then
    assertThat(productList.getItems(), hasSize(2));
    verify(productRepository).findAll();
  }

  @Test
  public void shouldFindProductsByName() {
    // given
    Product product = new Product("product 1",
        "product description 1", BigDecimal.ONE,
        BigDecimal.TEN);
    product.setId(UUID.randomUUID());
    Products products = new Products();
    products.setItems(singletonList(product));
    when(productRepository.findByNameIgnoreCaseContaining(anyString()))
        .thenReturn(products.getItems());
    //when
    Products productList = productService.findProductsByName("prod");
    //then
    assertThat(productList.getItems(), hasSize(1));
    verify(productRepository).findByNameIgnoreCaseContaining("prod");
  }

  @Test
  public void shouldFindProductById() {
    // given
    final UUID productId = UUID.randomUUID();
    Product product = new Product("product ",
        "product description ", BigDecimal.ONE,
        BigDecimal.TEN);
    product.setId(productId);
    when(productRepository.findById(any())).thenReturn(Optional.of(product));

    //when
    Optional<Product> productResponse = productService.findProductById(productId);
    assertTrue(productResponse.isPresent());
    assertThat(productResponse.get().getId(), is(product.getId()));
    verify(productRepository).findById(eq(productId));
  }

  @Test
  public void shouldDeleteById() {
    // given
    final UUID productId = UUID.randomUUID();
    //when
    productService.deleteProductById(productId);
    //then
    verify(productRepository).deleteById(productId);
  }

  @Test
  public void shouldFindAllProductOptions() {
    // given
    UUID productId = UUID.randomUUID();
    ProductOption productOption1 = new ProductOption("product option 1",
        "product description 1",
        productId);
    productOption1.setId(UUID.randomUUID());
    ProductOption productOption2 = new ProductOption("product option 2",
        "product description two",
        productId);
    productOption2.setId(UUID.randomUUID());
    ProductOptions productOptions = new ProductOptions();
    productOptions.setItems(asList(productOption1, productOption2));
    when(productOptionRepository.findAllByProductId(productId))
        .thenReturn(productOptions.getItems());

    //when
    ProductOptions productOptionsList = productService.findAllOptionsByProductId(productId);
    //then
    assertThat(productOptionsList.getItems(), hasSize(2));
    verify(productOptionRepository).findAllByProductId(productId);
  }

  @Test
  public void shouldFindProductOptionByIdAndProductId() {
    // given
    final UUID productId = UUID.randomUUID();
    final UUID productOptionId = UUID.randomUUID();
    ProductOption productOption = new ProductOption("product ",
        "product description ", productId);
    productOption.setId(productOptionId);
    when(productOptionRepository.findProductOptionByIdAndProductId(productOptionId, productId))
        .thenReturn(Optional.of(productOption));

    //when
    Optional<ProductOption> productOptionResponse = productService
        .findProductOptionByIdAndProductId(productOptionId, productId);
    //then
    assertTrue(productOptionResponse.isPresent());
    assertThat(productOptionResponse.get().getId(), is(productOption.getId()));
    verify(productOptionRepository).findProductOptionByIdAndProductId(productOptionId, productId);
  }

  @Test
  public void shouldSaveNewProductOption() {
    // given
    final UUID productId = UUID.randomUUID();
    final UUID productOptionId = UUID.randomUUID();
    ProductOption productOption = new ProductOption("product ",
        "product description ", productId);
    productOption.setId(productOptionId);
    when(productOptionRepository.save(any(ProductOption.class))).thenReturn(productOption);

    // when
    ProductOption savedProductOption = productService.saveProductOption(productOption);

    // then
    assertNotNull(savedProductOption);
    verify(productOptionRepository).save(savedProductOption);
  }

  @Test
  public void shouldDeleteProductOptionByIdAndProductId() {
    // given
    final UUID productId = UUID.randomUUID();
    final UUID productOptionId = UUID.randomUUID();
    //when
    productService.deleteProductOptionById(productOptionId, productId);
    //then
    verify(productOptionRepository).deleteByIdAndProductId(productOptionId, productId);
  }

  @Test(expected = ProductNotFoundException.class)
  public void shouldThrowExceptionForInvalidId() {
    // given
    final UUID productId = UUID.randomUUID();
    //when
    productService.verifyProductExists(productId);
  }

  @Test(expected = ProductNotFoundException.class)
  public void shouldThrowExceptionForInvalidOptionId() {
    // given
    final UUID productId = UUID.randomUUID();
    final UUID productOptionId = UUID.randomUUID();
    //when
    productService.verifyProductOptionExists(productId, productOptionId);
  }
}
