# Instructions:


## Building the project

### Method 1
#### Requirements:
1. Java (version 11)
2. mysql installed (version 5.3.71)
#### Steps
1. clone the project
2. rename application.properties.copy to application.properties
3. provide username and password you are using for mySql in file application.properties
4. Run the application, if everything goes fine, tomcat server should be running on port 8080
5. you can refer to api documentation after running the project here [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)


### Method 2
#### Requirements:
1. Java (version 11)
2. mysql installed (version 5.3.71)
#### Steps
2. make sure the mysql is running on port 3306.
3. execute the .jar file by running the command `java -jar /path/to/snapshot/file.jar --spring.datasource.username={your-mysql-username} --spring.datasource.password={your-mysql-password}`
4. Run the application, if everything goes fine, tomcat server should be running on port 8080
5. you can refer to api documentation after running the project here [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)


