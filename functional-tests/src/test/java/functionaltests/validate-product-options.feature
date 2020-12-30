Feature: Validate find,update and delete operations for Product Options.

  Background:
    # Set the url for all requests to 'baseUrl' which is configured in 'karate-config.js'
    * url baseUrl
    * def utils = callonce read('classpath:functionaltests/common.feature')
    * def authHeader = utils.basicAuth('karate_user', 'karate_password')
    * def productOptionRes1 = callonce read('classpath:functionaltests/save-product-options.feature@ProductOption1')
    * def productOptionRes2 = callonce read('classpath:functionaltests/save-product-options.feature@ProductOption2')

  Scenario: one - find All Product Options by ProductId - success
    Given path '/api/products/', productOptionRes1.productId1,'/options'
    And header Authorization = authHeader
    When method GET
    Then status 200
    And assert response.items.size() >= 1

  Scenario Outline: two - find productOptions by OptionId and ProductId - Bad Input
    Given path '/api/products/', productOptionRes1.productId1,'/options/','<id>'
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

  Scenario Outline: three - find productOptions by OptionId and ProductId  - success
    Given path '/api/products/',<id>,'/options/',<optionId>
    And header Authorization = authHeader
    When method GET
    Then status 200
    And match response contains { id: #notnull, name: #notnull, description: #notnull, productId : '#notpresent' }
    And match response.id == <productResponse>.response.id
    And match response.description == <productResponse>.response.description
    And match response.name == <productResponse>.response.name

    Examples:
      | id                           | optionId                      | productResponse   |
      | productOptionRes1.productId1 | productOptionRes1.response.id | productOptionRes1 |
      | productOptionRes2.productId2 | productOptionRes2.response.id | productOptionRes2 |


  Scenario Outline: four - Update productOption by ProductId and OptionId - success
    * def requestBody = { "name": "<name>", "description": "<description>" }
    Given path '/api/products/',<id>,'/options/',<optionId>
    And header Authorization = authHeader
    And request requestBody
    When method PUT
    Then status 204

    Given path '/api/products/', <id>,'/options/',<optionId>
    And header Authorization = authHeader
    When method GET
    Then status 200
    And match response contains { id: #notnull, name: #notnull, description: #notnull, productId : '#notpresent' }
    And match response.id == <productResponse>.response.id
    And match response.description == '<description>'
    And match response.name == '<name>'

    Examples:
      | id                           | optionId                      | productResponse   | description                          | name                          |
      | productOptionRes1.productId1 | productOptionRes1.response.id | productOptionRes1 | Apple Updated Option Description 1   | Apple Updated Option Name 1   |
      | productOptionRes2.productId2 | productOptionRes2.response.id | productOptionRes2 | Samsung Updated Option Description 1 | Samsung Updated Option Name 2 |

  Scenario Outline: five - Update productOption by ProductId and OptionId - Invalid Product and ProductOption
    * def requestBody = { }
    Given path '/api/products/',<id>, '/options/',<optionId>
    And header Authorization = authHeader
    And request requestBody
    When method PUT
    Then status 404
    And match response.error == 'Not Found'
    And match response.message == "Product with id "+<id>+" and productOption id "+<optionId>+" was not found"

    Examples:
      | id                                     | optionId                               |
      | 'd4c17665-b3da-4983-b786-fa843a278910' | '28c09485-f848-4f98-bd10-99dd4b3b5391' |
      | 'fb27b285-1ea4-4323-88fe-6a79d5821b4f' | '8126e51d-0e71-40e6-87e9-32a1db5940af' |

  Scenario Outline: six - Delete productOption by ProductId and OptionId - success
    Given path '/api/products/',<id>, '/options/',<optionId>
    And header Authorization = authHeader
    When method DELETE
    Then status 204

    Given path '/api/products/',<id>,'/options/',<optionId>
    And header Authorization = authHeader
    When method GET
    Then status 404
    And match response.error == 'Not Found'
    And match response.message == "Product with id "+<id>+" and productOption id "+<optionId>+" was not found"

    Examples:
      | id                           | optionId                      |
      | productOptionRes1.productId1 | productOptionRes1.response.id |
      | productOptionRes2.productId2 | productOptionRes2.response.id |