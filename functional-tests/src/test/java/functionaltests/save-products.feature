Feature: Validate creation of new products by rest operation.

  Background:
    # Set the url for all requests to 'baseUrl' which is configured in 'karate-config.js'
    * url baseUrl
    * path '/api/products'
    * def utils = callonce read('classpath:functionaltests/common.feature')
    * def authHeader = utils.basicAuth('karate_user', 'karate_password')

  Scenario Outline: save products - success
    Given path ''
    * def requestBody = { "name": "<name>", "description": "<description>", "price": "<price>", "deliveryPrice": "<deliveryPrice>" }
    And header Authorization = authHeader
    And request requestBody
    When method POST
    Then status 201
    And match response.id == '#notnull'
    And match response.name == '<name>'
    And match response.description == '<description>'
    And match response.price == <price>
    And match response.deliveryPrice == <deliveryPrice>

  @Product1
    Examples:
      | name              | description                         | price   | deliveryPrice |
      | Samsung Galaxy S7 | Newest mobile product from Samsung. | 1024.99 | 16.99         |

  @Product2
    Examples:
      | name            | description                       | price   | deliveryPrice |
      | Apple iPhone 6S | Newest mobile product from Apple. | 1299.99 | 15.99         |