package com.game.fuzzyGames.controller;

import com.game.fuzzyGames.service.SolverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GameController {

    @Autowired
    private SolverService solverService;

    @GetMapping("/")
    public String getGameResult(Model model) {
        String gameResult = solverService.solveGame();
        model.addAttribute("result", gameResult);
        return "index";
    }
}
