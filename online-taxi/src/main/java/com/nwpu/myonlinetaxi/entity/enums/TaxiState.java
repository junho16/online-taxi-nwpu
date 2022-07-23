package com.nwpu.myonlinetaxi.entity.enums;

/**
 * @author Junho
 * @date 2022/7/22 12:26
 */
public enum TaxiState {

    free(0),
    busy(1);

//    gotoPick(1),
//    gotoTar(2);

    private int state;

    TaxiState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
