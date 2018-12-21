package org.practise.algorithm.geekforgeek.interesting;

import java.util.ArrayList;
import java.util.List;

public class Encircular {
    public List<String> doesCircleExist(List<String> commands) {
        List<String> resultList = new ArrayList<>();
        for (String command : commands) {
            resultList.add(isCircle(command)? "Yes" : "No");
        }
        return resultList;
    }

    public boolean isCircle(String command) {
        int x = 0, y = 0, TOTAL_DIRECTIONS = Directions.values().length;
        char[] actions = command.toCharArray();
        Directions direction = Directions.East;
        for (char action : actions) {
            if (action == 'L') {
                direction = Directions.values()[(TOTAL_DIRECTIONS + direction.getValue() - 1) % TOTAL_DIRECTIONS];
            } else if (action == 'R') {
                direction = Directions.values()[(direction.getValue() + 1) % TOTAL_DIRECTIONS];
            } else {
                switch (direction) {
                    case East: {
                        x++;
                        break;
                    }
                    case West: {
                        x--;
                        break;
                    }
                    case South: {
                        y--;
                        break;
                    }
                    case North: {
                        y++;
                        break;
                    }
                }
            }
        }
        return x == 0 && y == 0;
    }

    enum Directions {
        East(0), South(1), West(2), North(3);

        private int value;
        Directions(int val) {
            this.value = val;
        }

        public int getValue() {
            return this.value;
        }
    }
}
