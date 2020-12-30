Feature: This feature tests whether the Products Service App is healthy and server is started without any issues.

  Background:
    # Set the url for all requests to 'baseUrl' which is configured in 'karate-config.js'
    * url baseUrl
    * print 'Base Url is: ', baseUrl

  Scenario: ONE - Test Application actuator heath of Products Service.
    Given path '/actuator/health'
    When method GET
    Then status 200
    And match $.status == 'UP'

  Scenario: TWO - Test Actuator non-healthcheck endpoints should expect authorization info
    Given path '/actuator/info'
    When method GET
    Then status 401
    And match $.error == 'Unauthorized'
    And match $.path == '/actuator/info'