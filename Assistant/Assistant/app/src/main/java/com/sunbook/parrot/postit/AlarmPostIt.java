package com.sunbook.parrot.postit;

/**
 * Created by hieuapp on 03/03/2016.
 */
public interface AlarmPostIt {
    int OUT_OF_DATE = 1;
    int NEAR_DEADLINE = 2;
    int IN_DATE = 3;

    /**
     * edit the note, Event, checklist
     */
    void edit();

    /**
     * Called when deadline finish
     */
    void alarm();

}
