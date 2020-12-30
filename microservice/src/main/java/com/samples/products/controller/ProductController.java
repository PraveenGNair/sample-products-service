package com.samples.products.controller;

import static org.springframework.util.ObjectUtils.isEmpty;

import com.samples.products.exception.MicroserviceError;
import com.samples.products.model.ProductOptions;
import com.samples.products.model.Products;
import com.samples.products.persistence.entity.Product;
import com.samples.products.persistence.entity.ProductOption;
import com.samples.products.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping("/api/products")
@Api(tags = "Product-Operations", description = "All Product and Product Options"
    + " CRUD operations goes here.")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  /**
   * Retrieves a collection of Products.
   *
   * @param name Product name or substring.
   * @return {@link Products}
   */
  @ApiOperation(value = "Retrieve a collection of Products.", response = Products.class,
      notes = "Retrieves a list of all products from the source.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Success. Product collections retrieved successfully.",
          response = Products.class)})
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Products> getAllProducts(
      @ApiParam(value = "Product name or its substring.")
      @RequestParam(required = false) String name,
      @RequestHeader final HttpHeaders httpHeaders,
      @RequestHeader("myName") final String  myName) {
    System.out.println(myName);
    System.out.println(httpHeaders.get("myName").get(0));
    Products products =
        isEmpty(name) ? productService.findAllProducts() : productService.findProductsByName(name);
    return ResponseEntity.ok(products);
  }

  /**
   * Creates and saves a new product.
   *
   * @param product Request product body.
   * @return {@link Product}.
   */
  @ApiOperation(value = "Create a new product.", response = Product.class,
      notes = "Creates a new product and saves in products database.")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Success. Product created successfully.",
          response = Product.class)})
  @ResponseStatus(value = HttpStatus.CREATED)
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Product> saveProduct(
      @ApiParam(value = "Request Product body.", required = true)
      @RequestBody Product product) {
    Product savedProduct = productService.saveProduct(product);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(savedProduct.getId()).toUri();
    return ResponseEntity.created(location).body(savedProduct);
  }

  /**
   * Find product using Id.
   *
   * @param id Unique Product Id.
   * @return {@link Product}.
   */
  @ApiOperation(value = "Get product using productId.", response = Product.class,
      notes = "Find product by productId.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Success. Product retrieved successfully",
          response = Product.class)})
  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Product> getProductById(
      @ApiParam(value = "Unique ProductId", required = true)
      @PathVariable UUID id) {
    productService.verifyProductExists(id);
    return ResponseEntity.ok(productService.findProductById(id).get());
  }

  /**
   * Update a product usinf ProductId.
   *
   * @param id product Id.
   * @param product Product body with updates.
   * @return Empty.
   */
  @ApiOperation(value = "Update product using productId.", notes = "Update the product by "
      + "productId and supplied request body.", response = void.class)
  @ApiResponses(value = {
      @ApiResponse(code = 204, message = "Success. Product has been updated successfully",
          response = void.class)})
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  @PutMapping("/{id}")
  public ResponseEntity updateProduct(
      @ApiParam(value = "Unique ProductId", required = true)
      @PathVariable UUID id,
      @ApiParam(value = "Product Request body with updates", required = true)
      @RequestBody Product product) {
    productService.verifyProductExists(id);
    product.setId(id);
    productService.saveProduct(product);
    return ResponseEntity.noContent().build();
  }

  /**
   * Deletes a product and its option by using the productId.
   *
   * @param id productId.
   * @return Empty.
   */
  @ApiOperation(value = "Delete the product and its options using the productId.", notes =
      "Deletes and removes the product and its option.")
  @ApiResponses(value = {
      @ApiResponse(code = 204, message = "Success. Product has been hard deleted successfully")})
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  @DeleteMapping("/{id}")
  public ResponseEntity deleteProduct(
      @ApiParam(value = "Unique ProductId", required = true) @PathVariable UUID id) {
    productService.verifyProductExists(id);
    productService.deleteProductById(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Retrive all options for a productId.
   *
   * @param id productId.
   * @return {@link ProductOptions}.
   */
  @ApiOperation(value = "Get product options for productId.", notes = "Retrieves all product "
      + "options for a product.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Success. ProductOption collections has been retreived "
          + "successfully", response = ProductOptions.class),
      @ApiResponse(code = 404, message = "No Product found.", response = MicroserviceError.class)})
  @GetMapping(path = "/{id}/options", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ProductOptions> getAllProductOptionsByProductId(
      @ApiParam(value = "Unique ProductId", required = true) @PathVariable UUID id) {
    productService.verifyProductExists(id);
    return ResponseEntity.ok(productService.findAllOptionsByProductId(id));
  }

  /**
   * Create and saves a productOption for a product.
   *
   * @param id productId.
   * @param productOption productOptionId.
   * @return {@link ProductOption}
   */
  @ApiOperation(value = "Create product option for a product.", notes = "Creates and saves a "
      + "product option for a product.")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Success. ProductOption created successfully",
          response = ProductOption.class)})
  @PostMapping(path = "/{id}/options", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.CREATED)
  public ResponseEntity<ProductOption> saveProductOption(
      @ApiParam(value = "Unique ProductId", required = true)
      @PathVariable UUID id,
      @ApiParam(value = "Request ProductOption Body", required = true)
      @RequestBody ProductOption productOption) {
    productService.verifyProductExists(id);
    productOption.setProductId(id);
    ProductOption savedProductOption = productService.saveProductOption(productOption);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(savedProductOption.getId()).toUri();
    return ResponseEntity.created(location).body(savedProductOption);
  }

  /**
   * Updates a productOption for a product.
   *
   * @param id productId.
   * @param optionId optionId.
   * @param productOption ProductOption body with updates.
   * @return Empty.
   */
  @ApiOperation(value = "Updates a product option for a product.", notes = "Update a "
      + "product option for a product with the supplied body.")
  @ApiResponses(value = {
      @ApiResponse(code = 204, message = "Success. ProductOption updated successfully",
          response = void.class)})
  @PutMapping("/{id}/options/{optionId}")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public ResponseEntity updateProductOption(
      @ApiParam(value = "Unique ProductId", required = true) @PathVariable UUID id,
      @ApiParam(value = "Unique OptionId", required = true) @PathVariable UUID optionId,
      @ApiParam(value = "ProductOption Request body with updates", required = true)
      @RequestBody ProductOption productOption) {
    productService.verifyProductOptionExists(id, optionId);
    productOption.setId(optionId);
    productOption.setProductId(id);

    productService.saveProductOption(productOption);
    return ResponseEntity.noContent().build();
  }

  /**
   * Retrive a productOption for productId and OptionId.
   *
   * @param id productId.
   * @param optionId optionId.
   * @return {@link ProductOption}
   */
  @ApiOperation(value = "Get product option for productId and optionId.",
      notes = "Retrieves product option for supplied productId and productoptionId.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Success. ProductOption has been retreived successfully",
          response = ProductOption.class)})
  @GetMapping(path = "/{id}/options/{optionId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ProductOption> getProductOptionById(
      @ApiParam(value = "Unique ProductId", required = true) @PathVariable UUID id,
      @ApiParam(value = "Unique OptionId", required = true) @PathVariable UUID optionId) {

    productService.verifyProductOptionExists(id, optionId);
    return ResponseEntity.ok(productService
        .findProductOptionByIdAndProductId(optionId, id).get());
  }

  /**
   * Deletes a productOption for a product and optionId.
   *
   * @param id productId.
   * @param optionId optionId.
   * @return Empty.
   */
  @ApiOperation(value = "Deletes a productoption using productId and optionId.",
      notes = "Deletes and removes the productoption from database.")
  @ApiResponses(value = {
      @ApiResponse(code = 204, message = "Success. ProductOption has been hard"
          + " deleted successfully", response = void.class)})
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  @DeleteMapping("/{id}/options/{optionId}")
  public ResponseEntity deleteProductOption(
      @ApiParam(value = "Unique ProductId", required = true) @PathVariable UUID id,
      @ApiParam(value = "unique OptionId", required = true) @PathVariable UUID optionId) {
    productService.verifyProductOptionExists(id, optionId);
    productService.deleteProductOptionById(optionId, id);
    return ResponseEntity.noContent().build();
  }

}
