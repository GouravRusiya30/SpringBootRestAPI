# REST API: Java Spring Boot and MongoDB
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/master/LICENSE)
[![Travis Build Status](https://travis-ci.org/GouravRusiya30/SpringBootRestAPI.svg?branch=master)](https://travis-ci.org/GouravRusiya30/SpringBootRestAPI)
[![sonar](https://sonarcloud.io/api/project_badges/measure?project=GouravRusiya30_SpringBootRestAPI&metric=alert_status)](https://sonarcloud.io/dashboard?id=GouravRusiya30_SpringBootRestAPI)
[![codecov](https://codecov.io/gh/GouravRusiya30/SpringBootRestAPI/branch/master/graph/badge.svg)](https://codecov.io/gh/GouravRusiya30/SpringBootRestAPI)
[![Build status](https://ci.appveyor.com/api/projects/status/5e852fcrbt0clhej?svg=true)](https://ci.appveyor.com/project/GouravRusiya30/springbootrestapi)

[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/dwyl/esta/issues)
[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](https://github.com/ellerbrock/open-source-badges/)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)
[![first-timers-only-friendly](http://img.shields.io/badge/first--timers--only-friendly-blue.svg?style=flat-square)](https://code.publiclab.org#r=all)
[![HitCount](http://hits.dwyl.io/GouravRusiya30/SpringBootRestAPI.svg)](http://hits.dwyl.io/GouravRusiya30/SpringBootRestAPI)

## Desciption
Simple rest api using spring boot and mongodb as nosql storage. 
This project mainly focussed on the kickstart to the CI/CD using TravisCI. Also includes CodeCoverage, Sonarqube integration which can be plugged into any application.

## Task List Progress
- [X] Rest controllers and models using SpringBoot
- [X] MongoDB configuration
- [X] TravisCI build
- [X] SonarQube integration 
- [X] Jacoco Test report
- [ ] JWT authentication
- [ ] 80% and above Code Coverage (using codecov or coveralls)
- [ ] Cloud deployment

### Getting Started
* Import this project into your favourite IDE after fork and checkout of this repository.

### Pre-requisite and Installing Steps

* Get a running instance of MongoDB that you can connect to. 
For more information on getting started with MongoDB, visit their [online tutorial](https://docs.mongodb.com/manual/).
* Start by creating a test database. I will call mine "rest_tutorial" using the following command in the MongoDB shell, or through a database manager like MongoDB Compass:
```use rest_tutorial;```

* Create a sample collection that will hold data about different types of pets. Let's create the collection with the following command:
```db.createCollection("pets");```

* Once the collection is created, we need to add some data! 
We can add data to the collection with the below query, you can add any number of data like this :
```db.pets.insertMany([```
  ```{```
    ```"name" : "Spot",```
    ```"species" : "dog",```
    ```"breed" : "pitbull"```
 ``` },```
  ```{```
    ```"name" : "Daisy",```
    ```"species" : "cat",```
    ```"breed" : "calico"```
  ```},```
  ```{```
    ```"name" : "Bella",```
    ```"species" : "dog",```
    ```"breed" : "australian shepard"```
  ```}```
```]);```

* Add the mongodb authentication-database, username & password in [application.properties](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/master/src/main/resources/application.properties)
If there is no authrntication when you are running locally then you can also remove these properties from this file.


### Running the tests
Once the server starts, you are free to test your API however you choose.
Use postman for the below tests :
##### [getAllPets](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/master/docs/getAllPets.png)

##### [getPetById](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/master/docs/getPetById.png)

##### [createPet](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/master/docs/createPet.png)

##### [deletePet](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/master/docs/deletePet.png)

##### [modifyPetById](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/master/docs/modifyPetById.png)

### Code Coverage
For code coverage reports integration, I have shown example using Codecov and Coveralls as both are pretty popular and easy to integrate with the travis.

* Codecov -  Just add [this line](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/master/.travis.yml#L5) in the [.travis.yml](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/master/.travis.yml) which will send the jacoco report to the codecov console

* Coveralls - Need to add [coveralls plugin](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/coverall-integration/build.gradle#L3) and [jacoco report path](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/97df783623e5c35696451c580cc7895d17c0743a/build.gradle#L52) in the build.gradle file. Also need change in [.travis.yml](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/97df783623e5c35696451c580cc7895d17c0743a/build.gradle#L52) instead of codecov to use coveralls

### Issue Request Template
``Are you up for your first Issue Request for this project !!!``
Awesome but please first go through the [ISSUE TEMPLATE.md](https://github.com/GouravRusiya30/SpringBootRestAPI/tree/master/.github/ISSUE_TEMPLATE/feature_request.md) and use this template to submit any issue request.

### Pull Request Template
``Are you up for your first PR for this project !!!``
Awesome but please first go through the [PULL REQUEST TEMPLATE.md](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/master/PULL_REQUEST_TEMPLATE.md) and use this template to submit your PR.

### Contributing
Please read [CONTRIBUTING.md](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/master/CONTRIBUTING.md) and [CODE OF CONDUCT.md](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/master/CODE_OF_CONDUCT.md) for details on our code of conduct, and the process for submitting pull requests to us.

### Authors
* **Gourav Rusiya** 
