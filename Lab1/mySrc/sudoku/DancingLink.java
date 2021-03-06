package mySrc.sudoku;

import java.util.Vector;
import java.util.concurrent.Callable;

public class DancingLink implements Callable {
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


    public final int kMaxNodes = 1 + 81 * 4 + 9 * 9 * 9 * 4;
    public final int kMaxColumns = 400;
    public final int kRow = 100, kCol = 200, kBox = 300;
    //原dance类变量
    Column root_;
    char[] inout_;
    Column columns_[] = new Column[400];
    Vector<Node> stack_ = new Vector<Node>();
    Node nodes_[] = new Node[kMaxNodes];
    int cur_node_;

    public DancingLink(char[] inout) {
        //构造代码块，将nodes_的每个元素赋予地址
        for (int i = 0; i < kMaxNodes; i++) {
            nodes_[i] = new Column();
        }

        this.inout_ = inout;
        this.cur_node_ = 0;
        //stack_.reserve(100);

        root_ = new_column();
        root_.left = root_.right = root_;
        //memset(columns_, 0, sizeof(columns_));

        boolean rows[][] = new boolean[N][10];
        boolean cols[][] = new boolean[N][10];
        boolean boxes[][] = new boolean[N][10];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < 10; j++) {
                rows[i][j] = cols[i][j] = boxes[i][j] = false;
            }
        }

        for (int i = 0; i < N; ++i) {
            int row = i / 9;
            int col = i % 9;
            int box = row / 3 * 3 + col / 3;
            int val = inout[i] - '0';
            rows[row][val] = true;
            cols[col][val] = true;
            boxes[box][val] = true;
        }

        for (int i = 0; i < N; ++i) {
            if (inout[i] == '0') {
                append_column(i);
            }
        }

        for (int i = 0; i < 9; ++i) {
            for (int v = 1; v < 10; ++v) {
                if (!rows[i][v]) {
                    append_column(get_row_col(i, v));
                }
                if (!cols[i][v]) {
                    append_column(get_col_col(i, v));
                }
                if (!boxes[i][v]) {
                    append_column(get_box_col(i, v));
                }
            }
        }

        for (int i = 0; i < N; ++i) {
            if (inout[i] == '0') {
                int row = i / 9;
                int col = i % 9;
                int box = row / 3 * 3 + col / 3;
                //int val = inout[i];
                for (int v = 1; v < 10; ++v) {
                    if (!(rows[row][v] || cols[col][v] || boxes[box][v])) {
                        Node n0 = new_row(i);
                        Node nr = new_row(get_row_col(row, v));
                        Node nc = new_row(get_col_col(col, v));
                        Node nb = new_row(get_box_col(box, v));
                        put_left((Column) n0, (Column) nr);
                        put_left((Column) n0, (Column) nc);
                        put_left((Column) n0, (Column) nb);
                    }
                }
            }
        }
    }

    boolean solve() {
        if (root_.left == root_) {
            for (int i = 0; i < stack_.size(); ++i) {
                Node n = stack_.get(i);
                int cell = -1;
                int val = -1;
                while (cell == -1 || val == -1) {
                    if (n.name < 100) {
                        cell = n.name;
                    } else {
                        val = n.name % 10;
                    }
                    n = n.right;
                }

                //assert(cell != -1 && val != -1);
                inout_[cell] = (char) (val + '0');
            }
            return true;
        }

        final Column col = get_min_column();
        cover(col);
        for (Node row = col.down; row != col; row = row.down) {
            stack_.addElement(row);
            for (Node j = row.right; j != row; j = j.right) {
                cover(j.col);
            }
            if (solve()) {
                return true;
            }
            stack_.remove(stack_.size() - 1);
            for (Node j = row.left; j != row; j = j.left) {
                uncover(j.col);
            }
        }
        uncover(col);
        return false;
    }

    Column get_min_column() {
        Column c = (Column) root_.right;
        int min_size = c.size;
        if (min_size > 1) {
            for (Column cc = (Column) c.right; cc != root_; cc = (Column) cc.right) {
                if (min_size > cc.size) {
                    c = cc;
                    min_size = cc.size;
                    if (min_size <= 1) {
                        break;
                    }
                }
            }
        }
        return c;
    }

    void cover(Column c) {
        c.right.left = c.left;
        c.left.right = c.right;
        for (Node row = c.down; row != c; row = row.down) {
            for (Node j = row.right; j != row; j = j.right) {
                j.down.up = j.up;
                j.up.down = j.down;
                j.col.size--;
            }
        }
    }

    void uncover(Column c) {
        for (Node row = c.up; row != c; row = row.up) {
            for (Node j = row.left; j != row; j = j.left) {
                j.col.size++;
                j.down.up = j;
                j.up.down = j;
            }
        }
        c.right.left = c;
        c.left.right = c;
    }


    void put_left(Column old, Column nnew) {
        nnew.left = old.left;
        nnew.right = old;
        old.left.right = nnew;
        old.left = nnew;
    }

    void put_up(Column old, Node nnew) {
        nnew.up = old.up;
        nnew.down = old;
        old.up.down = nnew;
        old.up = nnew;
        old.size++;
        nnew.col = old;
    }

    public Column new_column(int n) {
        assert (cur_node_ < kMaxNodes);
        Column c = (Column) nodes_[cur_node_++];
        //memset(c, 0, sizeof(Column));
        c.left = c;
        c.right = c;
        c.up = c;
        c.down = c;
        c.col = c;
        c.name = n;
        return c;
    }

    public Column new_column() {

        assert (cur_node_ < kMaxNodes);
        int n = 0;
        Column c = (Column) nodes_[cur_node_++];
        //memset(c, 0, sizeof(Column));
        c.left = c;
        c.right = c;
        c.up = c;
        c.down = c;
        c.col = c;
        c.name = n;
        return c;
    }

    public void append_column(int n) {
        if (columns_[n] != null) return;
        Column c = new_column(n);
        put_left(root_, c);
        columns_[n] = c;
    }

    Node new_row(int col) {
        assert (columns_[col] != null);
        assert (cur_node_ < kMaxNodes);

        Node r = nodes_[cur_node_++];

        //Node* r = new Node;
        //memset(r, 0, sizeof(Node));
        r.left = r;
        r.right = r;
        r.up = r;
        r.down = r;
        r.name = col;
        r.col = columns_[col];
        put_up(r.col, r);
        return r;
    }

    int get_row_col(int row, int val) {
        return kRow + row * 10 + val;
    }

    int get_col_col(int col, int val) {
        return kCol + col * 10 + val;
    }

    int get_box_col(int box, int val) {
        return kBox + box * 10 + val;
    }

    @Override
    public char[][] call() throws Exception {
        solve();
        char[][] ans = new char[9][9];
        int pos = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                ans[i][j] = inout_[pos++];
            }
        }
        return ans;
    }
}

class Node {
    Node left;
    Node right;
    Node up;
    Node down;
    Column col;
    int name;
    int size;
}

class Column extends Node {
} //使typedef Node Column;
