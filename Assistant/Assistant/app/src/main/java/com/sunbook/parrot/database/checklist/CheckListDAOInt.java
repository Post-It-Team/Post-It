package com.sunbook.parrot.database.checklist;

import com.sunbook.parrot.postit.Checklist;

import java.util.List;

/**
 * Created by hieuapp on 04/04/2016.
 */
public interface CheckListDAOInt {

    CheckListDAOInt getCheckListById(int checkListId);
    List<Checklist> getAllCheckList();
    boolean addCheckList(Checklist checklist);
    boolean addChecklists(List<Checklist> lists);
    boolean deleteAllCheckList();
    boolean deleteReminder(String id);
}
