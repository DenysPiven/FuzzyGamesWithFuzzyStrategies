package com.game.fuzzyGames.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GameData {
    @JsonProperty("M")
    private int M;
    @JsonProperty("N")
    private int N;

    @JsonProperty("M_degrees")
    private List<Double> M_degrees;
    @JsonProperty("N_degrees")
    private List<Double> N_degrees;
    @JsonProperty("Matrix")
    private List<List<Double>> Matrix;
}

