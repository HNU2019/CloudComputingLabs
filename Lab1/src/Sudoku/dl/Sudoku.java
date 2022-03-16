package Lab1.src.Sudoku.dl;

/**
 * @author Alistair
 * @description
 * @create 2022/3/16 17:27
 */
public abstract class Sudoku {
    public abstract class sudoku{

        public final boolean DEBUG_MODE = false;
        public final int ROW = 9;
        public final int N = 81;
        public final int COL = 9;
        public final int NEIGHBOR = 20;
        public final int NUM = 9;

        public final int neighbors[][] = new int[N][NEIGHBOR];
        public final int board[] = new int[N];
        public final int spaces[] = new int[N];
        //public final int nspaces;
        public final int[][] chess = new int[N][];

        abstract void init_neighbors();
        abstract void input(final char[] in[]);
        abstract void init_cache();

        abstract boolean available(int guess, int cell);

        abstract boolean solve_sudoku_basic(int which_space);
        abstract boolean solve_sudoku_min_arity(int which_space);
        abstract boolean solve_sudoku_min_arity_cache(int which_space);
        abstract boolean solve_sudoku_dancing_links(int unused);
        abstract boolean solved();


    }

}
