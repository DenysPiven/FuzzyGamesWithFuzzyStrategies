package com.game.fuzzyGames.service;

import com.game.fuzzyGames.model.GameData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;

@Service
public class SolverService {

    private final DataService dataService;

    @Autowired
    public SolverService(DataService dataService) {
        this.dataService = dataService;
    }

    public String solveGame() {

        // Load game data
        GameData gameData = dataService.loadGameData();

        // Check if data is loaded successfully
        if (gameData == null) {
            return "Failed to load game data";
        }

        // Calculate game result based on loaded data
        return calculateGameResult(gameData);
    }

    private String calculateGameResult(GameData gameData) {
        StringBuilder result = new StringBuilder();

        // Adding initial information about strategies and trust levels
        result.append("M = ")
                .append(gameData.getM_degrees().toString())
                .append(" and N = ")
                .append(gameData.getN_degrees().toString())
                .append("<br><br>");

        // Adding the matrix to the result
        result.append("Matrix:<br>");
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        for (List<Double> row : gameData.getMatrix()) {
            for (Double value : row) {
                result.append(decimalFormat.format(value)).append("\t");
            }
            result.append("<br>");
        }

        result.append("<br>");

        // Convert lists of trust degrees to sets to remove duplicates
        Set<Double> M_degreesSet = new HashSet<>(gameData.getM_degrees());
        Set<Double> N_degreesSet = new HashSet<>(gameData.getN_degrees());

        // Create a list to store the pairs of trust levels
        List<SimpleEntry<Double, Double>> trustLevelPairs = new ArrayList<>();

        // Generate all pair combinations of trust levels from the two sets
        for (Double mDegree : M_degreesSet) {
            for (Double nDegree : N_degreesSet) {
                trustLevelPairs.add(new SimpleEntry<>(mDegree, nDegree));
            }
        }

        // Sort the trust level pairs by ascending distance (a^2 + b^2)
        trustLevelPairs.sort(Comparator.comparing(pair ->
                Math.pow(pair.getKey(), 2) + Math.pow(pair.getValue(), 2)));

        Map<SimpleEntry<Double, Double> ,Double> solutions = new HashMap<>();
        for (SimpleEntry<Double, Double> pair : trustLevelPairs) {
            // Extract the trust levels for M and N
            double mTrust = pair.getKey();
            double nTrust = pair.getValue();

            // Now, implement the logic to solve the fuzzy minimax problem for this pair.
            double solution = solveForPair(mTrust, nTrust, gameData);
            solutions.put(pair, solution);

            // Format the trust levels and solution to remove trailing zeros
            String formattedSolution = String.valueOf(solution).replaceAll("\\.?0*$", "");

            result.append("For trust levels (M: ")
                    .append(mTrust)
                    .append(", N: ")
                    .append(nTrust)
                    .append("), the solution is: ")
                    .append(formattedSolution)
                    .append("<br>");
        }

        // Append the solutions in the specified format
        result.append("<br>W = { ");

        // Append each solution with its trust levels
        for (SimpleEntry<Double, Double> pair : trustLevelPairs) {
            double mTrust = pair.getKey();
            double nTrust = pair.getValue();
            double minTrust = Math.min(mTrust, nTrust);
            double solution = solutions.get(pair);
            String formattedSolution = String.valueOf(solution).replaceAll("\\.?0*$", "");

            result.append("((").append(formattedSolution)
                    .append(solution != 0.0 ? ", 1), " : ", 0), ")
                    .append(minTrust).append("), ");
        }

        result.append("<br>((r, 0), 1.0) |");
        for (SimpleEntry<Double, Double> pair : trustLevelPairs) {
            double solution = solutions.get(pair);
            String formattedSolution = String.valueOf(solution).replaceAll("\\.?0*$", "");
            result.append(" r != ").append(formattedSolution).append(",");
        }
        result.deleteCharAt(result.length() - 1);
        result.append("}.");

        // After building the result string, write it to a file
        try (FileWriter writer = new FileWriter("src/main/resources/data/gameResult.txt")) {
            // Replacing '<br>' with newline for plain text formatting
            String resultText = result.toString().replace("<br>", "\n");
            writer.write(resultText);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private double solveForPair(double mTrust, double nTrust, GameData gameData) {
        List<Double> M_degrees = gameData.getM_degrees();
        List<Double> N_degrees = gameData.getN_degrees();
        List<List<Double>> matrix = gameData.getMatrix();

        // Lists to store indices of strategies that match trust levels
        List<Integer> validMIndexes = new ArrayList<>();
        List<Integer> validNIndexes = new ArrayList<>();

        // Determine valid strategies based on trust levels
        for (int i = 0; i < M_degrees.size(); i++) {
            if (M_degrees.get(i) >= mTrust) {
                validMIndexes.add(i);
            }
        }
        for (int j = 0; j < N_degrees.size(); j++) {
            if (N_degrees.get(j) >= nTrust) {
                validNIndexes.add(j);
            }
        }

        // Exclude strategies from the matrix that do not match trust levels
        List<List<Double>> reducedMatrix = reduceMatrix(matrix, validMIndexes, validNIndexes);

        // Compute the minimax solution based on the updated matrix
        return minimaxSolution(reducedMatrix);
    }

    private List<List<Double>> reduceMatrix(List<List<Double>> matrix, List<Integer> validMIndexes, List<Integer> validNIndexes) {
        // Create a new matrix with valid strategies
        List<List<Double>> reducedMatrix = new ArrayList<>();
        for (Integer mIndex : validMIndexes) {
            List<Double> reducedRow = new ArrayList<>();
            for (Integer nIndex : validNIndexes) {
                reducedRow.add(matrix.get(mIndex).get(nIndex));
            }
            reducedMatrix.add(reducedRow);
        }
        return reducedMatrix;
    }

    private double minimaxSolution(List<List<Double>> matrix) {
        double maxMinValue = Double.NEGATIVE_INFINITY;

        // Iterate through each row of the matrix
        for (List<Double> row : matrix) {
            // Find the minimum value in the current row
            double minValueInRow = Collections.min(row);

            // Find the maximum of the minimum values
            if (minValueInRow > maxMinValue) {
                maxMinValue = minValueInRow;
            }
        }

        // Return the maximum of the minimum values, which is the optimal gain for the first player
        return maxMinValue;
    }
}
