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
public class Cell {

    public short val;
    public final short row;
    public final short col;
    public final short block;

    public Cell(int row, int col, int block) {
        this.col = (short) col;
        this.row = (short) row;
        this.block = (short) block;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.row;
        hash = 97 * hash + this.col;
        hash = 97 * hash + this.block;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cell other = (Cell) obj;
        if (this.row != other.row) {
            return false;
        }
        if (this.col != other.col) {
            return false;
        }
        if (this.block != other.block) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + row + ':' + col + ':' + val;
    }
}
