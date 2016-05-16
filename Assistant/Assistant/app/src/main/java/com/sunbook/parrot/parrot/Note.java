package com.sunbook.parrot.parrot;

import android.graphics.Bitmap;

/**
 * Created by hieuapp on 28/02/2016.
 */
public class Note implements AlarmParrot {

    private int id;
    private String title;
    private String description;
    private long deadline;
    private String date;
    private boolean important;
    private Bitmap avatar;
    private String history;

    public Note(){
    }

    /**
     * Constructor for init full attribute
     * @param title
     * @param description
     * @param date time create note
     * @param deadline
     * @param avatar
     * @param important
     */
    public Note(String title, String description,
                String date, long deadline,
                Bitmap avatar, boolean important){
        this.setTitle(title);
        this.setDescription(description);
        this.setDeadline(deadline);
        this.setDate(date);
        this.setImportant(important);
        this.setAvatar(avatar);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    @Override
    public void edit() {

    }

    @Override
    public void alarm() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
