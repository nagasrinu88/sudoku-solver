/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naga.sudokusolver;

import com.naga.sudokusolver.model.Grid1;

/**
 *
 * @author NagNav
 */
public class Solver {

    public static void main(String[] args) {
        Grid1 grid = new Grid1(3, 3);
        grid.setValue(6, 5, 7);
        grid.setValue(6, 6, 7);
        grid.setValue(0, 3, 7);
        grid.setValue(0, 2, 3);
        grid.setValue(4, 8, 2);
        grid.print();
    }
}
