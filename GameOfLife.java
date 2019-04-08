import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author MOIZ AHMED
 */
class GameOfLife {

    // File (with path) to read the initial grid from
    final String FILENAME = "startinggrid.txt";
    // Size of the game grid
    final int NUM_ROWS = 20;
    final int NUM_COLS = 20;

    private char current[][];  // current game grid iteration
    private char next[][];  // grid to store the next generation to be calculated

    int iterations;  // number of generations to be computed.

    public GameOfLife() {
        // Initialize grids
        current = new char[NUM_ROWS][NUM_COLS];
        next = new char[NUM_ROWS][NUM_COLS];

        // Read in initial grid state
        try {
            Scanner s = new Scanner(new File(FILENAME));
            for (int row = 0; row < NUM_ROWS; row++) {
                String str = s.next();
                for (int col = 0; col < NUM_COLS; col++) {
                    current[row][col] = str.charAt(col);
                }
            }
            String str = s.next();
            iterations = Integer.parseInt(str);
            s.close();
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
    }

    // Computes the next generation grid and replaces the current generation grid with it
    public void computeNextGen() {
        int thread_count = NUM_ROWS * NUM_COLS;
        Thread threads[] = new Thread[thread_count];
        int i = 0;  // thread index
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                threads[i] = new Thread(new MyRunnable(row, col, current, next));
                threads[i].start();
                i++;
            }
        }
        try {
            // Hold until all threads are finished
            for (i = 0; i < thread_count; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            System.err.println(e);
        }

        // Copy the newly calculated grid "next" to be the new "current"
        for (int row = 0; row < NUM_ROWS; row++) {
            System.arraycopy(next[row], 0, current[row], 0, NUM_COLS);
        }
    }

    // Returns a string representing the current generation grid for printing
    @Override
    public String toString() {
        String str = "";
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                str += current[row][col];
            }
            str += '\n';
        }
        return str;
    }

    public static void main(String[] args) {
        GameOfLife game = new GameOfLife();
        for (int i = 0; i < game.iterations; i++) {
            game.computeNextGen();
        }

        System.out.println("Final grid state:");
        System.out.println(game);
    }
}
