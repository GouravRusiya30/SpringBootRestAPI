# REST API: Java Spring Boot and MongoDB
[![Travis Build Status](https://travis-ci.org/GouravRusiya30/SpringBootRestAPI.svg?branch=master)](https://travis-ci.org/GouravRusiya30/SpringBootRestAPI)
[![sonar](https://sonarcloud.io/api/project_badges/measure?project=GouravRusiya30_SpringBootRestAPI&metric=alert_status)](https://sonarcloud.io/dashboard?id=GouravRusiya30_SpringBootRestAPI)
[![codecov](https://codecov.io/gh/GouravRusiya30/SpringBootRestAPI/branch/master/graph/badge.svg)](https://codecov.io/gh/GouravRusiya30/SpringBootRestAPI)

## Desciption
Simple rest api using spring boot and mongodb as nosql storage. 
This project mainly focussed on the kickstart to the CI/CD using TravisCI. Also includes CodeCoverage, Sonarqube integration with can be plugged into any application.

## Task List Progress
- [X] Rest controllers and models using SpringBoot
- [X] MongoDB configuration
- [X] TravisCI build
- [X] SonarQube integration 
- [X] Jacoco Test report
- [ ] JWT authentication
- [ ] 80% and above Code Coverage
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

### Contributing
Please read [CONTRIBUTING.md](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/master/docs/CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

### Authors
* **Gourav Rusiya** 