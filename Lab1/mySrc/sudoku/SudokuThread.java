package sudoku;

import java.util.concurrent.Callable;

public class SudokuThread implements Callable {
    public char[][] ans = new char[9][9];  //存放解
    //记录某行、列、宫中某个数字是否已填写
    //行代表数独中的某行(列、宫)，列代表填的是哪个数字
    private boolean[][] row = new boolean[10][10];
    private boolean[][] col = new boolean[10][10];
    private boolean[][] block = new boolean[10][10];

    public SudokuThread(char[] problem) {
        for (int i = 1; i <= 81; i++) {
            int r = (i + 8) / 9;
            int c = i - (r - 1) * 9;
            ans[r - 1][c - 1] = problem[i - 1];

            if (problem[i - 1] != '0') {
                int value = problem[i - 1] - '0';
                row[r][value] = true;
                col[c][value] = true;
                block[(r - 1) / 3 * 3 + (c - 1) / 3 + 1][value] = true;
            }
        }
    }


    public boolean dfs(int i, int j) {
        while (i <= 9 && j <= 9) {
            if (ans[i - 1][j - 1] != '0') {
                if (j < 9) j++;
                else {
                    j = 1;
                    i++;
                }
            } else {
                for (int num = 1; num <= 9; num++) {
                    int blockIndex = (i - 1) / 3 * 3 + (j - 1) / 3 + 1;
                    if (!row[i][num] && !col[j][num] && !block[blockIndex][num]) {
                        ans[i - 1][j - 1] = (char) (num + '0');
                        row[i][num] = true;
                        col[j][num] = true;
                        block[blockIndex][num] = true;
                        if (dfs(i, j)) return true;
                        else {
                            ans[i - 1][j - 1] = '0';
                            row[i][num] = false;
                            col[j][num] = false;
                            block[blockIndex][num] = false;
                        }
                    }
                }
                return false;  //该空无法填入不冲突的数字，解错误需要回溯
            }
        }
        return true;
    }

    @Override
    public char[][] call() {
        if (dfs(1, 1)) {
            return ans;
        } else {
            System.out.println("Sorry, the solution failed.");
            return new char[][]{};
        }
    }
}