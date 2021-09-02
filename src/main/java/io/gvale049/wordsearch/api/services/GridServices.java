package io.gvale049.wordsearch.api.services;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class GridServices {

        private enum Direction {
            HORIZONTAL,
            VERTICAL,
            DIAGONAL,
            HORIZONTAL_INVERSE,
            VERTICAL_INVERSE,
            DIAGONAL_INVERSE
        }

        private class Coordinate{
            private int x;
            private int y;

            public Coordinate (int x, int y) {
                this.x = x;
                this.y = y;
            }
        }

        public void displayGrid (char[][] content) {
            int GRID_SIZE = content[0].length;
            for(int i = 0; i < GRID_SIZE; i++) {
                for(int j = 0; j < GRID_SIZE; j++) {
                    System.out.print(content[i][j] + " ");
                }
                System.out.println(" ");
            }
        }

        public char[][] generateGrid(int GRID_SIZE, List<String> words) {

            List<Coordinate> coordinates = new ArrayList();
            char[][] content = new char[GRID_SIZE][GRID_SIZE];

            for(int i = 0; i < GRID_SIZE; i++) {
                for(int j = 0; j < GRID_SIZE; j++) {
                    coordinates .add(new Coordinate(i, j));
                    content[i][j] = '_';
                }
            }
            Collections.shuffle(coordinates);
            for (String word: words) {
                for (Coordinate coordinate : coordinates) {
                    int x = coordinate.x;
                    int y = coordinate.y;
                    Direction selectedDirection = getDirectionForFit(content, coordinate, word);
                    if(selectedDirection != null) {
                        switch (selectedDirection) {
                            case VERTICAL:
                                for (char c: word.toCharArray()) {
                                    content[x++][y] = c;
                                }
                                break;
                            case HORIZONTAL:
                                for (char c: word.toCharArray()) {
                                    content[x][y++] = c;
                                }
                                break;
                            case DIAGONAL:
                                for (char c: word.toCharArray()) {
                                    content[x++][y++] = c;
                                }
                                break;
                            case VERTICAL_INVERSE:
                                for (char c: word.toCharArray()) {
                                    content[x--][y] = c;
                                }
                                break;
                            case HORIZONTAL_INVERSE:
                                for (char c: word.toCharArray()) {
                                    content[x][y--] = c;
                                }
                                break;
                            case DIAGONAL_INVERSE:
                                for (char c: word.toCharArray()) {
                                    content[x--][y--] = c;
                                }
                                break;
                        }
                        break;
                    }
                }
            }

            randomFillGrid(content);
            return content;
        }

        public Direction getDirectionForFit (char[][] content, Coordinate coordinate, String word) {
            List<Direction> directions = Arrays.asList(Direction.values());
            Collections.shuffle(directions);
            for (Direction direction : directions) {
                if (doesFit(content, word, coordinate, direction)) {
                    return direction;
                }
            }
            return null;
        }

        private boolean doesFit (char[][] content, String word, Coordinate coordinate, Direction direction) {
            int GRID_SIZE = content[0].length;
            switch (direction) {
                case VERTICAL -> {
                    if (coordinate.x + word.length() > GRID_SIZE)
                        return false;
                    for (int i = 0; i < word.length(); i++) {
                        if (content[coordinate.x + i][coordinate.y] != '_')
                            return false;
                    }
                }
                case HORIZONTAL -> {
                    if (coordinate.y + word.length() > GRID_SIZE)
                        return false;
                    for (int i = 0; i < word.length(); i++) {
                        if (content[coordinate.x][coordinate.y + i] != '_')
                            return false;
                    }
                }
                case DIAGONAL -> {
                    if ((coordinate.x + word.length() > GRID_SIZE) || (coordinate.y + word.length() > GRID_SIZE))
                        return false;
                    for (int i = 0; i < word.length(); i++) {
                        if (content[coordinate.x + i][coordinate.y + i] != '_')
                            return false;
                    }
                }
                case VERTICAL_INVERSE -> {
                    if (coordinate.x < word.length())
                        return false;
                    for (int i = 0; i < word.length(); i++) {
                        if (content[coordinate.x - i][coordinate.y] != '_')
                            return false;
                    }
                }
                case HORIZONTAL_INVERSE -> {
                    if (coordinate.y < word.length())
                        return false;
                    for (int i = 0; i < word.length(); i++) {
                        if (content[coordinate.x][coordinate.y - i] != '_')
                            return false;
                    }
                }
                case DIAGONAL_INVERSE -> {
                    if ((coordinate.x < word.length()) || (coordinate.y < word.length()))
                        return false;
                    for (int i = 0; i < word.length(); i++) {
                        if (content[coordinate.x - i][coordinate.y - i] != '_')
                            return false;
                    }
                }
            }
            return true;
        }

        private void randomFillGrid(char[][] content) {
            int GRID_SIZE = content[0].length;
            String allLeters = "abcdefghijklmnopqstuvxywz";

            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    if (content[i][j] == '_') {
                        int randomIndex = ThreadLocalRandom.current().nextInt(0, allLeters.length());
                        content[i][j] = allLeters.charAt(randomIndex);
                    }
                }
            }
        }
}
