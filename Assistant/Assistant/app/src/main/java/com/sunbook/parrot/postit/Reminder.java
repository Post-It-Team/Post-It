package com.sunbook.parrot.postit;

import java.util.Date;

/**
 * Created by hieuapp on 06/03/2016.
 */
public class Reminder extends Note implements AlarmPostIt {
    private boolean isDone;

    public Reminder(){

    }

    public Reminder(String title, long deadline, boolean done, boolean important){
        this.isDone = done;
        this.setTitle(title);
        this.setDeadline(deadline);
        this.setImportant(important);
    }
    @Override
    public void edit() {
    }

    @Override
    public void alarm() {

    }

    public boolean isDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }


}
