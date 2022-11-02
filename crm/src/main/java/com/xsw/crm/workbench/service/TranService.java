package com.xsw.crm.workbench.service;

import com.xsw.crm.workbench.domain.FunnelVo;
import com.xsw.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {
    void saveCreateTran(Map<String,Object> map);

    Tran queryTranForDetailById(String id);

    List<FunnelVo> queryCountOfTranGroupByStage();
}
