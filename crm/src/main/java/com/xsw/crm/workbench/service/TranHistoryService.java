package com.xsw.crm.workbench.service;

import com.xsw.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryService {

    List<TranHistory> queryTranHistoryForDetailByTranId(String tranId);
}
