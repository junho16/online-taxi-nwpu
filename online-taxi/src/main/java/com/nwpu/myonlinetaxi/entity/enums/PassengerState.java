package com.nwpu.myonlinetaxi.entity.enums;

/**
 * @author Junho
 * @date 2022/7/22 14:27
 */
public enum PassengerState {

    //0：空闲可叫车 1：已叫车 2：在路上即运动中
    free(0),
    call(1),
    run(2);

    private int state;

    PassengerState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
