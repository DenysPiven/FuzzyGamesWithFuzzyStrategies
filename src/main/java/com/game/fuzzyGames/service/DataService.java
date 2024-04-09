package com.game.fuzzyGames.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.fuzzyGames.model.GameData;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DataService {

    public DataService() {
    }

    public GameData loadGameData() {
        String resourcePath = "data/gameData.json";
        Resource resource = new ClassPathResource(resourcePath);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(resource.getInputStream(), GameData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}