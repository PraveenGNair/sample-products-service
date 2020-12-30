Feature: Validate find,update and delete operations for Products.

  Background:
    # Set the url for all requests to 'baseUrl' which is configured in 'karate-config.js'
    * url baseUrl
    * def utils = callonce read('classpath:functionaltests/common.feature')
    * def authHeader = utils.basicAuth('karate_user', 'karate_password')
    * def product_response1 = callonce read('classpath:functionaltests/save-products.feature@Product1')
    * def product_response2 = callonce read('classpath:functionaltests/save-products.feature@Product2')

  Scenario: one - find All products - success
    Given path '/api/products'
    And header Authorization = authHeader
    When method GET
    Then status 200
    And assert response.items.size() >= 1

  Scenario Outline: two - find product by Id - Bad Input
    Given path '/api/products/','<id>'
    And header Authorization = authHeader
    When method GET
    Then status 400
    And match response.error == 'Bad Request'
    And match response.message contains 'Invalid UUID string'

    Examples:
      | id      |
      | 122     |
      | #!@$    |
      | invalid |

  Scenario Outline: three - find product by Id - success
    Given path '/api/products/',<id>
    And header Authorization = authHeader
    When method GET
    Then status 200
    And match response contains { id: #notnull, name: #notnull, description: #notnull, price: #notnull, deliveryPrice: #notnull }
    And match response.id == <productResponse>.response.id
    And match response.description == <productResponse>.response.description
    And match response.price == <productResponse>.response.price * 1
    And match response.deliveryPrice == <productResponse>.response.deliveryPrice * 1

    Examples:
      | id                            | productResponse   |
      | product_response1.response.id | product_response1 |
      | product_response2.response.id | product_response2 |

  Scenario Outline: four - Update product by Id - success
    * def requestBody = { "name": "<name>", "description": "<description>", "price": "<price>", "deliveryPrice": "<deliveryPrice>" }
    Given path '/api/products/',<id>
    And header Authorization = authHeader
    And request requestBody
    When method PUT
    Then status 204

    Given path '/api/products/',<id>
    And header Authorization = authHeader
    When method GET
    Then status 200
    And match response.id == <productResponse>.response.id
    And match response.name == '<name>'
    And match response.description == '<description>'
    And match response.price == <price>
    And match response.deliveryPrice == <deliveryPrice>

    Examples:
      | id                            | productResponse   | description                   | price | deliveryPrice | name                   |
      | product_response1.response.id | product_response1 | Apple Updated Description 1   | 200   | 100           | Apple Updated Name 1   |
      | product_response2.response.id | product_response2 | Samsung Updated Description 1 | 300   | 500           | Samsung updated name 2 |

  Scenario Outline: five - Update product by Id - Invalid Products
    * def requestBody = { }
    Given path '/api/products/',<id>
    And header Authorization = authHeader
    And request requestBody
    When method PUT
    Then status 404
    And match response.error == 'Not Found'
    And match response.message == "Product with id "+<id>+" was not found"

    Examples:
      | id                                     |
      | 'd4c17665-b3da-4983-b786-fa843a278910' |
      | 'fb27b285-1ea4-4323-88fe-6a79d5821b4f' |

  Scenario Outline: six - Delete product by Id - success
    Given path '/api/products/',<id>
    And header Authorization = authHeader
    When method DELETE
    Then status 204

    Given path '/api/products/',<id>
    And header Authorization = authHeader
    When method GET
    Then status 404
    And match response.error == 'Not Found'
    And match response.message == "Product with id "+<id>+" was not found"

    Examples:
      | id                            |
      | product_response1.response.id |
      | product_response2.response.id |
