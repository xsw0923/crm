package com.xsw.crm.workbench.service.impl;

import com.xsw.crm.commons.contans.Contans;
import com.xsw.crm.commons.utils.DateUtils;
import com.xsw.crm.commons.utils.UUIDUtils;
import com.xsw.crm.settings.domain.User;
import com.xsw.crm.workbench.domain.*;
import com.xsw.crm.workbench.mapper.*;
import com.xsw.crm.workbench.service.ClueRemarkService;
import com.xsw.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {

    @Autowired
    private ClueMapper clueMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Override
    public int saveCreateClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public List<Clue> queryClueByConditionForPage(Map<String, Object> map) {
        return clueMapper.selectClueByConditionForPage(map);
    }

    @Override
    public int queryCountOfClueByCondition(Map<String, Object> map) {
        return clueMapper.selectCountOfClueByCondition(map);
    }

    @Override
    public Clue queryClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

    @Override
    public void saveConvertClue(Map<String, Object> map) {
        String clueId = (String) map.get("clueId");
        User user = (User) map.get(Contans.SESSION_USER);
        String isCreateTran = (String) map.get("isCreateTran");
        //??????id?????????????????????
        Clue clue = clueMapper.selectClueById(clueId);
        //?????????????????????????????????????????????????????????
        Customer c = new Customer();
        c.setAddress(clue.getAddress());
        c.setContactSummary(clue.getContactSummary());
        c.setCreateBy(user.getId());
        c.setCreateTime(DateUtils.formateDateTime(new Date()));
        c.setDescription(clue.getDescription());
        c.setId(UUIDUtils.getUUID());
        c.setName(clue.getCompany());
        c.setNextContactTime(clue.getNextContactTime());
        c.setOwner(user.getId());
        c.setPhone(clue.getPhone());
        c.setWebsite(clue.getWebsite());
        customerMapper.insertCustomer(c);
        //?????????????????????????????????????????????????????????
        Contacts co = new Contacts();
        co.setAddress(clue.getAddress());
        co.setContactSummary(clue.getContactSummary());
        co.setDescription(clue.getDescription());
        co.setCreateBy(user.getId());
        co.setAppellation(clue.getAppellation());
        co.setCreateTime(DateUtils.formateDateTime(new Date()));
        co.setCustomerId(c.getId());
        co.setEmail(clue.getEmail());
        co.setFullname(clue.getFullname());
        co.setId(UUIDUtils.getUUID());
        co.setJob(clue.getJob());
        co.setMphone(clue.getMphone());
        co.setNextContactTime(clue.getNextContactTime());
        co.setOwner(user.getId());
        co.setSource(clue.getSource());
        contactsMapper.insertContacts(co);

        //??????clueId?????????????????????????????????
        List<ClueRemark> crList = clueRemarkMapper.selectClueRemarkByClueId(clueId);

        if (crList!=null&&crList.size()>0){
            //?????????????????????????????????????????????????????????
            //???????????????????????????????????????
            CustomerRemark cur=null;
            ContactsRemark cor=null;
            List<CustomerRemark> curList=new ArrayList<>();
            List<ContactsRemark> corList=new ArrayList<>();
            for (ClueRemark cr:crList){
                cur=new CustomerRemark();
                cur.setCreateBy(cr.getCreateBy());
                cur.setCreateTime(cr.getCreateTime());
                cur.setCustomerId(c.getId());
                cur.setEditBy(cr.getEditBy());
                cur.setEditFlag(cr.getEditFlag());
                cur.setEditTime(cr.getEditTime());
                cur.setId(UUIDUtils.getUUID());
                cur.setNoteContent(cr.getNoteContent());
                curList.add(cur);

                cor=new ContactsRemark();
                cor.setContactsId(co.getId());
                cor.setCreateBy(cr.getCreateBy());
                cor.setCreateTime(cr.getCreateTime());
                cor.setEditBy(cr.getEditBy());
                cor.setEditTime(cr.getEditTime());
                cor.setEditFlag(cr.getEditFlag());
                cor.setId(UUIDUtils.getUUID());
                cor.setNoteContent(cr.getNoteContent());
                corList.add(cor);
            }
            customerRemarkMapper.insertCustomerRemarkByList(curList);
            contactsRemarkMapper.insertContacktsRemarkByList(corList);
        }

        //??????clueId??????????????????????????????????????????
        List<ClueActivityRelation> carList = clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);

        //??????carList???????????????????????????????????????????????????????????????????????????????????????????????????
        if (carList!=null&&carList.size()>0){
            ContactsActivityRelation coar=null;
            List<ContactsActivityRelation> coarList=new ArrayList<>();
            for (ClueActivityRelation car:carList){
                coar=new ContactsActivityRelation();
                coar.setId(UUIDUtils.getUUID());
                coar.setActivityId(car.getActivityId());
                coar.setContactsId(co.getId());
                coarList.add(coar);
            }
            contactsActivityRelationMapper.insertClueActivityRelationByList(coarList);
        }

        //???????????????????????????????????????????????????????????????,??????????????????????????????????????????????????????????????????
        if ("true".equals(isCreateTran)) {
            Tran tran = new Tran();
            String activityId = (String) map.get("activityId");
            tran.setActivityId(activityId);
            tran.setContactsId(co.getId());
            tran.setCreateBy(user.getId());
            tran.setCreateTime(DateUtils.formateDateTime(new Date()));
            tran.setCustomerId(c.getId());
            tran.setExpectedDate((String) map.get("expectedDate"));
            tran.setId(UUIDUtils.getUUID());
            tran.setMoney((String) map.get("money"));
            tran.setName((String) map.get("name"));
            tran.setOwner(user.getId());
            tran.setStage((String) map.get("stage"));
            tranMapper.insert(tran);

            //?????????????????????????????????????????????????????????
            if (crList != null && crList.size() > 0) {
                TranRemark tr = null;
                List<TranRemark> trList = new ArrayList<>();
                for (ClueRemark cr : crList) {
                    tr = new TranRemark();
                    tr.setCreateBy(cr.getCreateBy());
                    tr.setCreateTime(cr.getCreateTime());
                    tr.setEditBy(cr.getEditBy());
                    tr.setEditFlag(cr.getEditFlag());
                    tr.setEditTime(cr.getEditTime());
                    tr.setId(UUIDUtils.getUUID());
                    tr.setNoteContent(cr.getNoteContent());
                    tr.setTranId(tran.getId());
                    trList.add(tr);
                }
                tranRemarkMapper.insertTranRemarkByList(trList);
            }
        }
        //?????????????????????????????????
        clueRemarkMapper.deleteClueRemarkByClueId(clueId);

        //?????????????????????????????????????????????
        clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);

        //????????????
        clueMapper.deleteClueById(clueId);
    }

}
