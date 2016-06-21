package com.sunbook.parrot;

import android.util.Log;

import com.sunbook.parrot.postit.AlarmPostIt;
import com.sunbook.parrot.postit.Reminder;
import com.sunbook.parrot.postit.Note;

/**
 * Created by hieuapp on 06/03/2016.
 */
public class NoteFactory {

    public static final String TAG = "NoteFactory";
    public static final String NOTE = "note";
    public static final String CHECKLIST = "check_list";
    public static final String EVENT = "event";

    public AlarmPostIt getNote(String noteType){
        if(noteType == null){
            return null;
        }

        switch (noteType){
            case NOTE:
                return new Note();
            case CHECKLIST:
                return new Reminder();
            default:
                Log.e(TAG, "note type is undefine");
        }

        return null;

    }
}
