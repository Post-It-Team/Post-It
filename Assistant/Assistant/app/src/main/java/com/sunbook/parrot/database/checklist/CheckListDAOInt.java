package com.sunbook.parrot.database.checklist;

import com.sunbook.parrot.parrot.Checklist;

import java.util.List;

/**
 * Created by hieuapp on 04/04/2016.
 */
public interface CheckListDAOInt {

    public CheckListDAOInt getCheckListById(int checkListId);
    public List<Checklist> getAllCheckList();
    public boolean addCheckList(Checklist checklist);
    public boolean addChecklists(List<Checklist> lists);
    public boolean deleteAllCheckList();
}
