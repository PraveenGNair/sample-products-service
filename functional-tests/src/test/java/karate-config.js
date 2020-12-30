/**
 * Base configuration for Karate Tests.
 */
function config() {
  karate.configure('connectTimeout', 15000);
  karate.configure('readTimeout', 15000);

  var protocol = 'http';
  var host = 'localhost';

  var microservicePort = karate.properties['microservice.port'];
  if (!microservicePort) {
    microservicePort = '8080'
  }

  var config = {};
  // url
  config.baseUrl = protocol + '://' + host + ':' + microservicePort;

  return config;
}
