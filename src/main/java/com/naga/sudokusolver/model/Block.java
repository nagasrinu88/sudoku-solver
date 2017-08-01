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
public class Block {

    byte r;
    byte c;
    Cell1[][] cells;

    private Block(byte r, byte c) {
        this.r = r;
        this.c = c;
    }

    public static Block build(byte r, byte c) {
        Block block = new Block(r, c);
        block.cells = new Cell1[3][3];
        for (byte i = 0; i < 3; i++) {
            for (byte j = 0; j < 3; j++) {
                block.cells[i][j] = new Cell1((byte) 0, i, j);
            }
        }
        return block;
    }
}
