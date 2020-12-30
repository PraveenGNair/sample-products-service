@ignore
Feature: This feature file holds Common Util functions.
#  This holds common functions see https://github.com/intuit/karate#multiple-functions-in-one-file

  Scenario:
    * def uuid = function(){ return java.util.UUID.randomUUID() + '' }
    * def basicAuth =
    """
    function(username,password) {
      var temp = username + ':' + password;
      var Base64 = Java.type('java.util.Base64');
      var encoded = Base64.getEncoder().encodeToString(temp.bytes);
      return 'Basic ' + encoded;
    }
    """