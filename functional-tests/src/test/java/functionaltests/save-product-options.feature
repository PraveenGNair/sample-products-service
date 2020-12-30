Feature: Validate creation of new product options by rest operation.

  Background:
    # Set the url for all requests to 'baseUrl' which is configured in 'karate-config.js'
    * url baseUrl
    * path '/api/products/'
    * def utils = callonce read('classpath:functionaltests/common.feature')
    * def authHeader = utils.basicAuth('karate_user', 'karate_password')
    * def productRes1 =  callonce read('classpath:functionaltests/save-products.feature@Product1')
    * def productRes2 =  callonce read('classpath:functionaltests/save-products.feature@Product2')
    * def productId1 = productRes1.response.id
    * def productId2 = productRes2.response.id


  Scenario Outline: save productOption for a Product - success
    Given path <productId>,'options'
    * def requestBody = { "name": "<name>", "description": "<description>" }
    And header Authorization = authHeader
    And request requestBody
    When method POST
    Then status 201
    And match response.id == '#notnull'
    And match response.name == '<name>'
    And match response.description == '<description>'
    And match response.productId == '#notpresent'

  @ProductOption1
    Examples:
      | name  | description              | productId  |
      | White | White Samsung Galaxy S7. | productId1 |
  @ProductOption2
    Examples:
      | name      | description          | productId  |
      | Rose Gold | Gold Apple iPhone 6S | productId2 |