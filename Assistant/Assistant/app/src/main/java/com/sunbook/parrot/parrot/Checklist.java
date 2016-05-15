package com.sunbook.parrot.parrot;

/**
 * Created by hieuapp on 06/03/2016.
 */
public class Checklist extends Note implements AlarmParrot {
    private boolean isDone;

    public Checklist(){

    }

    public Checklist(String title, long deadline, boolean done){
        this.isDone = done;
        this.setTitle(title);
        this.setDeadline(deadline);
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
