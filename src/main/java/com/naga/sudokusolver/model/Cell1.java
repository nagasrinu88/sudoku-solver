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
public class Cell1 {

    public byte v;
    public final byte r;
    public final byte c;

    public Cell1(byte v, byte r, byte c) {
        this.v = v;
        this.r = r;
        this.c = c;
    }

}
