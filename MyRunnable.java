/**
 *
 * @author MOIZ AHMED
 */
public class MyRunnable implements Runnable {

    private char current[][]; // Current grid iteration
    private char next[][]; // Next grid iteration that this thread will write to
    int row, col;  // Row and Column number to id the square this thread is responsible for

    public MyRunnable(int row, int col, char[][] current, char[][] next) {
        // Assumes row and col values legal grid indices
        this.row = row;
        this.col = col;
        this.current = current;
        this.next = next;
    }

    // Helper function for run()
    // Returns the number of living neighbors to the square this thread is responsible for
    private int count_neighbors() {
        int LAST_ROW = current.length - 1; // index of the last row of the grid for boundary checking
        int LAST_COL = current[0].length - 1; // index of the last column of the grid for boundary checking
        int count = 0;
        /*
            Count the number of living neighbors to this thread's square
            Takes advantage of short circuit expression evaluation to not dereference an illegal array index
         */

        // If this thread's square is on the first row, it has no northern neighbors
        if (row != 0) {
            // Check N neighbor
            if (current[row - 1][col] == 'X') {
                count++;
            }
            // Check NW neighbor
            if (col != 0 && current[row - 1][col - 1] == 'X') {
                count++;
            }
            // Check NE neighbor
            if (col != LAST_COL && current[row - 1][col + 1] == 'X') {
                count++;
            }
        }

        // Check W neighbor
        if (col != 0 && current[row][col - 1] == 'X') {
            count++;
        }
        // Check E neighbor
        if (col != LAST_COL && current[row][col + 1] == 'X') {
            count++;
        }

        // If this thread's square is on the last row, it has no southern neighbors
        if (row != LAST_ROW) {
            // Check S neighbor
            if (current[row + 1][col] == 'X') {
                count++;
            }
            // Check SW neighbor
            if (col != 0 && current[row + 1][col - 1] == 'X') {
                count++;
            }
            // Check SE neighbor
            if (col != LAST_COL && current[row + 1][col + 1] == 'X') {
                count++;
            }
        }

        return count;
    }

    // Calculate and update the next generation grid for the square this thread was assigned
    @Override
    public void run() {
        int neighbors = count_neighbors();
        char current_state = current[row][col];

        if (current_state == 'X' && (neighbors < 2 || neighbors > 3)) // Death by over or underpopulation
        {
            next[row][col] = 'O';
        } else if (current_state == 'O' && neighbors == 3) // Given life by reproduction
        {
            next[row][col] = 'X';
        } else // State unchanged from previous iteration
        {
            next[row][col] = current_state;
        }
    }
}
