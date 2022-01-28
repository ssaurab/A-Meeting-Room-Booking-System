# To Run the Backend

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




# To Run the Frontend

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app), using the [Redux](https://redux.js.org/) and [Redux Toolkit](https://redux-toolkit.js.org/) template.

## Available Scripts

In the project directory, you can run:

### `npm start`

Runs the app in the development mode.<br />
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.<br />
You will also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.<br />
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run build`

Builds the app for production to the `build` folder.<br />
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.<br />
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.

### `npm run eject`

**Note: this is a one-way operation. Once you `eject`, you can’t go back!**

If you aren’t satisfied with the build tool and configuration choices, you can `eject` at any time. This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (webpack, Babel, ESLint, etc) right into your project so you have full control over them. All of the commands except `eject` will still work, but they will point to the copied scripts so you can tweak them. At this point you’re on your own.

You don’t have to ever use `eject`. The curated feature set is suitable for small and middle deployments, and you shouldn’t feel obligated to use this feature. However we understand that this tool wouldn’t be useful if you couldn’t customize it when you are ready for it.

## Learn More

You can learn more in the [Create React App documentation](https://facebook.github.io/create-react-app/docs/getting-started).

To learn React, check out the [React documentation](https://reactjs.org/).
