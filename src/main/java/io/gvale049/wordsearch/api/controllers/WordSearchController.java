package io.gvale049.wordsearch.api.controllers;

import io.gvale049.wordsearch.api.services.GridServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController("/")
public class WordSearchController {

        @Autowired
        GridServices wordGridService;

        @GetMapping("/wordgrid")
        public String CreateWordGrid(@RequestParam int gridSize, @RequestParam String wordList){
            List<String> words = Arrays.asList(wordList.split(","));
            char[][] grid = wordGridService.generateGrid(gridSize, words);
            String gridToString = "";
            int GRID_SIZE = grid[0].length;
            for(int i = 0; i < GRID_SIZE; i++) {
                for(int j = 0; j < GRID_SIZE; j++) {
                    gridToString += grid[i][j] + " ";
                }
                gridToString += "\r\n";
            }
            return gridToString;
        }
}
