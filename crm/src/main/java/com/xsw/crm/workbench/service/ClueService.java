package com.xsw.crm.workbench.service;

import com.xsw.crm.workbench.domain.Clue;
import com.xsw.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface ClueService {

    int saveCreateClue(Clue clue);

    List<Clue> queryClueByConditionForPage(Map<String,Object> map);

    int queryCountOfClueByCondition(Map<String,Object> map);

    Clue queryClueForDetailById(String id);

    void saveConvertClue(Map<String,Object> map);
}
