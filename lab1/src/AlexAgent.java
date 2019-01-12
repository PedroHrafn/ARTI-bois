import java.security.Principal;
import java.util.Collection;
import java.util.Random;

public class AlexAgent implements Agent {
    private boolean hasStarted = false;
    // Grid
    /*---------------------*/
    private int xMax;
    private int xMin;
    private int yMax;
    private int yMin;
    private int width;
    private int height;

    // Agent
    /*---------------------*/
    // Tells us if we have found all 4 walls, so we can calculate number of tiles
    private boolean setVisited = false;
    // Start counting visited tiles
    private boolean startVisited = false;
    // Returning to init position
    private boolean goHome = false;
    // Tiles visited
    private int visited;
    // Represents number of tiles on the grid
    private int numOfTiles = 0;
    // starts at 0 and increments to 4 when all borders are found
    private int wallsFound = 0;
    //

    // NOTE: initial position of agent is set to 0 on both axis with the agent
    // facing north
    private int xCurr = 0; // initial position is 0
    private int yCurr = 0; // initial p

    // represents direction facing with int values from 1 to 4:
    // 1 = N , 2 = E, 3 = S, 4 = W
    /// TODO: --- can be changed to enum?
    private int facing = 1;

    public String nextAction(Collection<String> percepts) {
        if (!hasStarted) {
            hasStarted = true;
            return "TURN_ON";
        }
        if (goHome)
            return returnHome();
        // The only things the agent can percieve is DIRT and BUMP
        for (String percept : percepts) {
            // We should only bump each side of the border once
            // If we bump a border it means we didn't move so we correct
            // current position and also set the coordinate of the border
            if (percept.equals("BUMP")) {
                return bump();
                // If we percieve DIRT then we SUCK it up
            } else if (percept.equals("DIRT")) {
                return "SUCK";
            }
        }
        // If we have already found all borders, we check if we need to rotate
        // and change the borders of the box we move in order to make a spiral
        System.out.println("\nx max: " + xMax + ", y max: " + yMax);
        System.out.println("\nx min: " + xMin + ", y min: " + yMin);
        System.out.println("\nx curr: " + xCurr + ", y curr: " + yCurr);
        System.out.println("\nVisited: " + visited + "\n");
        if (wallsFound == 4) {
            return spiral();
        }

        return go();
    }

    private String spiral() {
        if (!setVisited) {
            numOfTiles = width * height;
            setVisited = true;
        }
        System.out.println("\nNumOfTiles: " + numOfTiles + "\n");
        if (visited == numOfTiles && numOfTiles != 0) {
            goHome = true;
            return returnHome();
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
        return go();
    }

    private String returnHome() {
        if (xCurr == 0 && yCurr == 0)
            return "TURN_OFF";
        if (facing == 1) {
            if (yCurr < 0)
                return go();
            if (xCurr < 0)
                return rotate(true);
            return rotate(false);

        } else if (facing == 2) {
            if (xCurr < 0)
                return go();
            if (yCurr > 0)
                return rotate(true);
            return rotate(false);
        } else if (facing == 3) {
            if (yCurr > 0)
                return go();
            if (xCurr > 0)
                return rotate(true);
            return rotate(false);
        }
        if (xCurr > 0)
            return go();
        if (yCurr < 0)
            return rotate(true);
        return rotate(false);
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
        if (startVisited)
            visited++;
        return "GO";
    }

    private String bump() {
        boolean right = true;
        int OFFSET = 1;
        if (facing == 1) {
            yCurr--;
            yMax = yCurr;
        } else if (facing == 2) {
            // Start counting visited tiles
            startVisited = true;
            // Count current tile as well
            visited++;
            xCurr--;
            xMax = xCurr - OFFSET;
        } else if (facing == 3) {
            yCurr++;
            yMin = yCurr + OFFSET;
            height = visited - OFFSET;
        } else {
            xCurr++;
            xMin = xCurr + OFFSET;
            // add the one we are in
            width = visited - height;
        }
        System.out.println("\nWidth: " + width + "\n");
        System.out.println("\nheight: " + height + "\n");
        System.out.println("visited: " + visited);
        if (startVisited && visited > 1) {
            System.out.println("Bye one Visit !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
            visited--;
        }
        wallsFound++;
        return rotate(right);
    }

    public void reset() {
        hasStarted = false;
        xCurr = 0;
        yCurr = 0;
        wallsFound = 0;
    }
}
