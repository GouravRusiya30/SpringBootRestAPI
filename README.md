# REST API: Java Spring Boot and MongoDB
[![Travis Build Status](https://travis-ci.org/GouravRusiya30/SpringBootRestAPI.svg?branch=master)](https://travis-ci.org/GouravRusiya30/SpringBootRestAPI)
[![sonar](https://sonarcloud.io/api/project_badges/measure?project=GouravRusiya30_SpringBootRestAPI&metric=alert_status)](https://sonarcloud.io/dashboard?id=GouravRusiya30_SpringBootRestAPI)

```Tech Stack Used in this project :```
* Java
* Spring Boot
* Gradle
* MongoDB
* Postman

## Task List Progress
- [X] Rest controllers and models using SpringBoot
- [X] MongoDB configuration
- [X] TravisCI build
- [X] SonarQube integration 
- [X] Jacoco Test report
- [ ] 80% and above Code Coverage
- [ ] Cloud deployment script addition to travis

## ```Setup Steps ```

### Step 1: Creating a Database
Get a running instance of MongoDB that you can connect to. 
For more information on getting started with MongoDB, visit their [online tutorial](https://docs.mongodb.com/manual/).

Start by creating a test database. I will call mine "rest_tutorial" using the following command in the MongoDB shell, or through a database manager like MongoDB Compass:
```use rest_tutorial;```
This will create a new database in MongoDB that we can use for our project.

### Step 2: Adding a MongoDB Collection and Data
Create a sample collection that will hold data about different types of pets. Let's create the collection with the following command:

```db.createCollection("pets");```
Once the collection is created, we need to add some data! 
We can add data to the collection with the following command:

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

### Step 3: Adding mongodb credentials
Add the mongodb authentication-database, username & password in [application.properties](https://github.com/GouravRusiya30/SpringBootRestAPI/blob/master/src/main/resources/application.properties)
If there is no authrntication when you are running locally then you can also remove these properties from this file.

### Step 4: Testing Your API
Once the server starts, you are free to test your API however you choose.
For example :
* POST 'http://localhost:8080/pets'
With body : ```{"name" : "Liam", "species" : "cat", "breed" : "tabby"}```
and header : ```'Content-Type: application/json'```
