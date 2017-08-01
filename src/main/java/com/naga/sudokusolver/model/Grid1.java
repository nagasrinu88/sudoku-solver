/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naga.sudokusolver.model;

/**
 *
 * @author NagNav
 */
public class Grid1 {

    Block[][] blocks;
    Cell1[] cells;
    int rows;
    int cols;

    public Grid1(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        blocks = new Block[rows][cols];
        cells = new Cell1[81];
        for (byte i = 0; i < 3; i++) {
            for (byte j = 0; j < 3; j++) {
                blocks[i][j] = Block.build(i, j);
            }
        }
        fetchCells();
    }

    public void setValue(int r, int c, int v) {
        cells[r * 9 + c].v = (byte) v;
    }

    private void fetchCells() {
        int k = 0;
        for (byte i = 0; i < 9; i++) {
            Block block = blocks[i / 3][i % 3];
            //System.out.print(block.r + ":" + block.c + " ");
            for (byte j = 0; j < 3; j++) {
                for (Cell1 cell : block.cells[(i + j) % 3]) {
                    cells[k++] = cell;
                    //System.out.print(cell.r + ":" + cell.c + " ");
                }
            }
            //System.out.print("\n");
        }
    }

    public void print() {
        for (int i = 0; i < cells.length; i++) {
            Cell1 cell = cells[i];
            if (i % 9 == 0) {
                System.out.println("");
            }
            //System.out.print(cell.r + ":" + cell.c + " ");
            System.out.print(cell.v + " ");

        }
    }

}
