# Application Overview

This application has a single REST controller that provides basic CRUD operations for the `Company` entity and all its related entities, such as `Department`, `Project`, `Team`, and `Manager`.

## Dependencies

If you're curious about the dependencies used, feel free to check out the `pom.xml` file. The main dependencies include:

1. `spring-boot-starter-data-jpa`
2. `spring-boot-starter-web`
3. `spring-boot-docker-compose`
4. `spring-boot-starter-validation`
5. `spring-boot-starter-test`
6. `spring-boot-testcontainers`
7. `postgresql`

## Running the Project

It's time to run the project and test it!

To do so, make sure the following are installed on your device:

1. **JDK 21**
2. **Maven**
3. **Docker Desktop** (for Windows) or **Docker Driver** (for Linux)

### Next Steps

1. Download the project.
2. Open the project using a code editor or an IDE.
3. Start Docker.
4. In the project's root directory, run the following command in the terminal:
   ```bash
   mvn spring-boot:run
5. Try to send POST request to http://localhost:8080/api/company with body from BodyExample.

## Running Tests

To run the tests, follow these steps:

1. **Download the Project**

   Ensure you have the project files downloaded to your local machine.

2. **Open the Project**

   Open the project using a code editor or an Integrated Development Environment (IDE) of your choice.

3. **Start Docker**

   Make sure Docker is running on your machine.

4. **Run the Tests**

   Navigate to the root directory of the project and execute the following command in the terminal:

   ```bash
   mvn test

5. **Enjoy!!!**

   Enjoy the fact that you didn't have to write these tests yourself!

## Technical Task Implementation

The technical task was followed carefully. If something seems missing, it might not have been specified in the task or misunderstood. Additional features were added to improve functionality or simplify the project.

## Testing Approach

Tests are written at the controller level because of the controller’s input validation.

## Final Note

I hope the coding style and approach are to your liking. Please provide any feedback or request adjustments if needed.

contact: tvijasplacadem@gmail.com