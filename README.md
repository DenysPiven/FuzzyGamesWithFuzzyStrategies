# FuzzyGamesWithFuzzyStrategies

This project, "FuzzyGamesWithFuzzyStrategies," is designed to demonstrate fuzzy logic strategies in game theory. It includes components for managing game data, solving fuzzy minimax problems, and providing a web interface for viewing game results.

### Project Structure

- **src/fuzzyGames/controller/GameController.java**
- **src/fuzzyGames/model/GameData.java**
- **src/fuzzyGames/service/DataService.java**
- **src/fuzzyGames/service/SolverService.java**
- **src/fuzzyGames/FuzzyGamesApplication.java**

### Resources

- **src/resources/data/gameData.json**
- **src/resources/static/favicon.ico**
- **src/resources/templates/index.html**
- **src/resources/application.properties**

### Additional Files

- **docker-compose.yaml**
- **Dockerfile**
- **HELP.md**
- **mvnw, mvnw.cmd**
- **pom.xml**
- **README.md**

### Installation

1. Clone the repository to your local machine.
2. Ensure you have Docker and Maven installed.
3. Navigate to the project directory.
4. Run `mvn clean package` to build the project.
5. Run `docker-compose up` to start the application.
6. Access the application at `http://localhost:8080` in your web browser.

### Usage

To use this project effectively, follow these steps:

1. Modify the game data in the `gameData.json` file located in `src/resources/data/`.
2. Update the matrix values, M degrees, and N degrees according to your game scenario.
3. Ensure that the matrix dimensions (M x N) match the number of degrees specified.
4. Run the application using Docker and Maven as described in the installation steps.
5. Access the web interface at `http://localhost:8080` to view the game results.

The application will create all possible pairs of fuzzy strategies based on the provided data. It will then solve the fuzzy minimax problem for each pair, find the results, and combine them into a summary.