package com.sunbook.parrot.database.checklist;

import com.sunbook.parrot.postit.Reminder;

import java.util.List;

/**
 * Created by hieuapp on 04/04/2016.
 */
public interface CheckListDAOInt {

    CheckListDAOInt getCheckListById(int checkListId);
    List<Reminder> getAllCheckList();
    boolean addCheckList(Reminder reminder);
    boolean addChecklists(List<Reminder> lists);
    boolean deleteAllCheckList();
    boolean deleteReminder(String id);
}
