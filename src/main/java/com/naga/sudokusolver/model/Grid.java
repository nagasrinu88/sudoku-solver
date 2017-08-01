/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naga.sudokusolver.model;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author NagNav
 */
public class Grid {

    int rows;
    int cols;
    Cell[] cells;
    boolean locked = false;
    List<Cell> emptyCells = new CopyOnWriteArrayList<>();
    Stack<Entry> stack = new Stack<>();

    class Entry {

        Cell[] cells;
        //Map<Cell, Set<Short>> emptyCells;
        List<Cell> emptyCells;

        public Entry(Cell[] cells, List<Cell> emptyCells) {
            this.cells = cells.clone();
            this.emptyCells = new CopyOnWriteArrayList<>(emptyCells);
        }

    }

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        init();
    }

    private void init() {
        cells = new Cell[rows * cols];
        int n = rows * cols;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int ind = rows * i + j;
                cells[ind] = new Cell(i, j, (i / 3) * 3 + (ind % 9) / 3);
                //System.out.println((ind % 9) / 3);
            }
        }
    }

    public void unset(int r, int c) {
        set(r, c, 0);
    }

    public void unset(int ind) {
        set(ind, 0);
    }

    public void set(int r, int c, int v) {
        set(r * rows + c, v);
    }

    public void set(int ind, int v) {
        Cell cell = cells[ind];
        cell.val = (short) v;
        if (locked) {
            //Set<Cell> relativies = findRelativies(cell);
//            for (Cell relative : relativies) {
//                if (relative.val == 0) {
//                    //System.out.println(emptyCells.get(relative) + " : " + v);
//                    emptyCells.get(relative).remove((short) v);
//                    //System.out.println(emptyCells.get(relative) + " : " + v);
//                }
//            }
            // doing the extra stuff
        }
    }

    private Set<Short> findPossiblities(Cell cell) {
        Set<Short> set = new HashSet<>();
        for (short i = 1; i <= 9; i++) {
            set.add(i);
        }
        Set<Cell> relativies = findRelativies(cell);
        for (Cell relative : relativies) {
            if (relative.val != 0) {
                set.remove(relative.val);
            }
        }
        return set;
    }

    private Set<Cell> findRelativies(Cell cell) {
        Set<Cell> set = new HashSet<>();
        for (int i = 0; i < cols; i++) {
            Cell c = cells[cols * cell.row + i];
            set.add(c);
        }
        for (int i = 0; i < rows; i++) {
            Cell c = cells[rows * i + cell.col];
            set.add(c);
        }
        for (Cell c : cells) {
            if (c.block == cell.block) {
                set.add(c);
            }
        }
        // removing own cell
        set.remove(cell);
        return set;
    }

    private void scan() {
    }

    public Grid lock() {
        // first scanning the values
        for (Cell cell : cells) {
            if (cell.val == 0) {
                //emptyCells.put(cell, findPossiblities(cell));
                emptyCells.add(cell);
            }
        }

        this.locked = true;
        return this;
    }

    private boolean isSolved() {
        for (Cell cell : cells) {
            if (cell.val == 0) {
                return false;
            }
        }
        return true;
    }

    boolean solveNackedSingles() {
        boolean flag = false;
        for (Cell c : cells) {
            if (c.val == 0) {
                Set<Short> vals = findPossiblities(c);
                if (vals.size() == 1) {
                    flag = true;
                    //System.out.println("Found Nacked Value :" + c);
                    c.val = vals.iterator().next();
                    //return solveNackedSingles();
                }
            }
        }
        return flag;
    }

    boolean solveHiddenSingles() {
        boolean flag = false;
        for (int i = 0; i < 9; i++) {
            Map<Short, Integer> map = new HashMap<>();
            Map<Short, Cell> cellMap = new HashMap<>();
            for (int j = 0; j < cols; j++) {
                int ind = rows * i + j;
                Cell cell = cells[ind];
                if (cell.val == 0) {
                    for (Short s : findPossiblities(cells[ind])) {
                        Integer frq = map.get(s);
                        if (frq == null) {
                            frq = 0;
                        }
                        cellMap.put(s, cell);
                        map.put(s, frq + 1);
                    }
                }
            }
            for (Short key : map.keySet()) {
                if (map.get(key) == 1) {
                    flag = true;
                    cellMap.get(key).val = key;
                    //System.out.println("Found Hidden Value In Row :" + i);
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            Map<Short, Integer> map = new HashMap<>();
            Map<Short, Cell> cellMap = new HashMap<>();
            for (int j = 0; j < cols; j++) {
                int ind = i + cols * j;
                Cell cell = cells[ind];
                if (cell.val == 0) {
                    for (Short s : findPossiblities(cell)) {
                        Integer frq = map.get(s);
                        if (frq == null) {
                            frq = 0;
                        }
                        cellMap.put(s, cell);
                        map.put(s, frq + 1);
                    }
                }
            }
            for (Short key : map.keySet()) {
                if (map.get(key) == 1) {
                    flag = true;
                    cellMap.get(key).val = key;
                    //System.out.println("Found Hidden Value In Column :" + i);
                }
            }
        }
        return flag;
    }

    private boolean _solve() {
        boolean flag;
        do {
            //System.out.println("Solving Recurricively");
        } while (solveNackedSingles() || solveHiddenSingles());
        return isSolved();
    }

    boolean solve(int level) {
        boolean flag = false;
        if (isSolved()) {
            return true;
        }
        for (Cell cell : cells) {
            if (cell.val == 0) {
                Set<Short> set = findPossiblities(cell);
                if (set.isEmpty()) {
                    return false;
                }
                //System.out.println("Cell " + cell + " Possible Set: " + set + " at level " + level);
                //System.out.println("set = " + set);
                //flag = true;
                //Short[] array = set.toArray(new Short[]{});
                for (Short s : set) {
                    //emptyCells.remove(cell);
                    cell.val = s;
                    //set(cell.row, cell.col, s);
                    //System.out.println("Path Deciding to use the Value: " + cell);
                    if (solve(level)) {
                        return true;
                    } else {
                        //System.out.println("Wrong path so adding back the cell: " + cell);
                        cell.val = 0;
                        //emptyCells.add(cell);
                        //unset(cell.row, cell.col);
                    }
                }
            }
        }
        return true;
    }

    public void solve() {
        long s = System.currentTimeMillis();
        boolean solved = _solve();
        if (solved) {
            System.out.println("Hurry Problem is solved :)");
        } else {
            System.out.println("Shhhh Not able to find the solution :(");
            for (Cell cell : cells) {
                if (cell.val == 0) {
                    System.out.println(cell + " ---> " + findPossiblities(cell));
                }
            }
        }
        long e = System.currentTimeMillis();
        //System.out.println("Final Status: " + flag);
        System.out.println("Time took: " + (e - s) / 1000F + " seconds");
//        System.out.println("=========================================");

    }

    public void print() {
        System.out.println("###############################");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int ind = rows * i + j;
                Cell cell = cells[ind];
                //System.out.print(cell.row + ":" + cell.col + " ");
                System.out.print(cell.val + " ");
            }
            System.out.println();
        }
        System.out.println("###############################");
    }

    private static Grid test1() {
        Grid grid = new Grid(9, 9);
        grid.set(0, 0, 4);
        grid.set(0, 1, 9);
        grid.set(0, 2, 2);
        grid.set(0, 3, 1);
        grid.set(0, 5, 3);

        grid.set(1, 0, 6);
        grid.set(1, 1, 3);
        grid.set(1, 4, 4);
        grid.set(1, 5, 8);
        grid.set(1, 6, 1);
        grid.set(1, 7, 5);
        grid.set(1, 8, 2);

        grid.set(2, 3, 7);
        grid.set(2, 4, 2);
        grid.set(2, 8, 9);

        grid.set(3, 6, 7);
        grid.set(3, 7, 1);

        grid.set(4, 2, 3);
        grid.set(4, 6, 4);

        grid.set(5, 1, 8);
        grid.set(5, 2, 6);

        grid.set(6, 0, 9);
        grid.set(6, 4, 6);
        grid.set(6, 5, 4);

        grid.set(7, 0, 3);
        grid.set(7, 1, 7);
        grid.set(7, 2, 5);
        grid.set(7, 3, 2);
        grid.set(7, 4, 1);
        grid.set(7, 7, 6);
        grid.set(7, 8, 4);

        grid.set(8, 3, 3);
        grid.set(8, 5, 7);
        grid.set(8, 6, 5);
        grid.set(8, 7, 9);
        grid.set(8, 8, 1);
        return grid;
    }

    public static void main(String[] args) throws Exception {
        Grid grid = new Grid(9, 9);
        Scanner in = new Scanner(new File("problem4.txt"));
        int row = 0;
        while (in.hasNext()) {
            String line = in.nextLine();
            for (String token : line.trim().split(" ")) {
                String[] tkns = token.split(":");
                grid.set(Integer.parseInt(tkns[0]) + row * 9 - 1, Integer.parseInt(tkns[1]));
                //System.out.println(token);
            }
            row++;
        }
        //System.out.println("ind = " + row);
        //grid.print();
        grid.lock();
        grid.solve();
        grid.print();
        //System.out.println("Total Empty Cells: " + grid.emptyCells.size());
        //System.out.println("empty Cells: " + grid.emptyCells);

    }

    public static void main1(String[] args) {
        ConcurrentHashMap map = new ConcurrentHashMap();
        map.put("naga", "Srinu");
        ConcurrentHashMap map1 = new ConcurrentHashMap(map);
        map1.remove("naga");
        System.out.println("map1 = " + map);
        System.out.println("map1 = " + map1);
    }

}
