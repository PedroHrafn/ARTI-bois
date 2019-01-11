import java.security.Principal;
import java.util.Collection;
import java.util.Random;

public class AlexAgent implements Agent {
    private boolean hasStarted = false;
    private int xMax;
    private int xMin;
    private int yMax;
    private int yMin;
    private int visited; // Tiles visited
    private int numOfTiles = 0; // Represents number of tiles on the grid
    private boolean setVisited = false; // Tells us if we have found all 4 walls, so we can calculate number of tiles
    private int wallsFound = 0; // starts at 0 and increments to 4 when all borders are found
    // initial position of agent is set to 0 on both axis with the agent facing
    // north
    private int xCurr = 0; // initial position is 0
    private int yCurr = 0; // initial p

    // represents direction facing with int values from 1 to 4:
    // 1 = N , 2 = E, 3 = S, 4 = W
    /// --- can be changed to enum?
    private int facing = 1;

    public String nextAction(Collection<String> percepts) {
        if (!hasStarted) {
            hasStarted = true;
            return "TURN_ON";
        }
        // The only things the agent can percieve is DIRT and BUMP
        // System.out.print("perceiving:");
        for (String percept : percepts) {
            // System.out.print("HERNA: '" + percept + "', ");
            // We should only bump each side of the border once
            // If we bump a border it means we didn't move so we correct
            // current position and also set the coordinate of the border
            if (percept.equals("BUMP")) {
                if (facing == 1) {
                    yCurr--;
                    // þetta á örugglega að vera yMax = yCurr;
                    yMax = yCurr - 1;
                } else if (facing == 2) {
                    xCurr--;
                    xMax = xCurr - 1;
                } else if (facing == 3) {
                    yCurr++;
                    yMin = yCurr + 1;
                } else {
                    xCurr++;
                    xMin = xCurr + 1;
                }
                visited--;
                wallsFound++;
                return rotate(true);
                // If we percieve DIRT then we SUCK it up
            } else if (percept.equals("DIRT")) {
                return "SUCK";
            }
        }
        /*
         * System.out.println(); System.out.println(wallsFound);
         */
        // If we have already found all borders, we check if we need to rotate
        // and change the borders of the box we move in order to make a spiral
        System.out.println("\nx max: " + xMax + ", y max: " + yMax);
        System.out.println("\nx min: " + xMin + ", y min: " + yMin);
        System.out.println("\nx curr: " + xCurr + ", y curr: " + yCurr);
        if (wallsFound == 4) {
            // THIS IF IS INCORRECT (off-by-one)
            // TODO: FIX THIS
            if (!setVisited) {
                // max is always width - 1 (at this point) then because we are counting from 0,
                // -1
                numOfTiles = (xMax + 2) * (yMax + 2) + yMax + 2;
                setVisited = true;
                System.out.println("/n SIZE: " + numOfTiles + "\n");
            }
            if (visited == numOfTiles && numOfTiles != 0) {
                // TODO: make a function to make robot go to initial position (0,0)
                return "TURN_OFF";
            }
            if (facing == 1 && yCurr == yMax) {
                yMax--;
                return rotate(true);
            } else if (facing == 2 && xCurr == xMax) {
                xMax--;
                return rotate(true);
            } else if (facing == 3 && yCurr == yMin) {
                yMin++;
                return rotate(true);
            } else if (facing == 4 && xCurr == xMin) {
                xMin++;
                return rotate(true);
            }
        }

        return go();
        /*
         * System.out.println(""); String[] actions = { "TURN_ON", "TURN_OFF",
         * "TURN_RIGHT", "TURN_LEFT", "GO", "SUCK" }; String returner =
         * actions[random.nextInt(actions.length)];
         * System.out.println(" --- --- ACTION RETURNED: " + returner); return "GO";
         */
    }

    private String rotate(boolean clockwise) {
        if (clockwise) {
            facing++;
        } else {
            facing--;
        }
        if (facing > 4) {
            facing = 1;
        } else if (facing < 1) {
            facing = 4;
        }
        if (clockwise)
            return "TURN_RIGHT";
        return "TURN_LEFT";
    }

    private String go() {
        if (facing == 1) {
            yCurr++;
        } else if (facing == 2) {
            xCurr++;
        } else if (facing == 3) {
            yCurr--;
        } else {
            xCurr--;
        }
        visited++;
        return "GO";
    }

    public void reset() {
        hasStarted = false;
        xCurr = 0;
        yCurr = 0;
        wallsFound = 0;
    }
}

/*
 * if (facing == 1) {
 * 
 * } else if (facing == 2) {
 * 
 * } else if (facing == 3) {
 * 
 * } else {
 * 
 * }
 */