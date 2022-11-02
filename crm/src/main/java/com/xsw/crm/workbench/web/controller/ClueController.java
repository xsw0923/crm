package com.xsw.crm.workbench.web.controller;

import com.xsw.crm.commons.contans.Contans;
import com.xsw.crm.commons.domain.ReturnObject;
import com.xsw.crm.commons.utils.DateUtils;
import com.xsw.crm.commons.utils.UUIDUtils;
import com.xsw.crm.settings.domain.DicValue;
import com.xsw.crm.settings.domain.User;
import com.xsw.crm.settings.service.DicValueService;
import com.xsw.crm.settings.service.UserService;
import com.xsw.crm.workbench.domain.Activity;
import com.xsw.crm.workbench.domain.Clue;
import com.xsw.crm.workbench.domain.ClueActivityRelation;
import com.xsw.crm.workbench.domain.ClueRemark;
import com.xsw.crm.workbench.service.ActivityService;
import com.xsw.crm.workbench.service.ClueActivityRelationService;
import com.xsw.crm.workbench.service.ClueRemarkService;
import com.xsw.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {
    @Autowired
    private UserService userService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private ClueService clueService;

    @Autowired
    private ClueRemarkService clueRemarkService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ClueActivityRelationService clueActivityRelationService;

    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request){
        //调用service层方法，查询动态数据
        List<User> userList = userService.queryAllUsers();
        List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> clueStateList = dicValueService.queryDicValueByTypeCode("clueState");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");

        //把数据保存到作用域中
        request.setAttribute("userList",userList);
        request.setAttribute("appellationList",appellationList);
        request.setAttribute("clueStateList",clueStateList);
        request.setAttribute("sourceList",sourceList);

        //请求转发
        return "workbench/clue/index";
    }

    @RequestMapping("/workbench/clue/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpSession session){
        User user = (User) session.getAttribute(Contans.SESSION_USER);
        //封装参数
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateTime(DateUtils.formateDateTime(new Date()));
        clue.setCreateBy(user.getId());

        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service
            int ret = clueService.saveCreateClue(clue);
            if (ret>0){
                returnObject.setCode(Contans.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contans.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试。。。。。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contans.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试。。。。。");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/clue/queryClueByConditionForPage.do")
    @ResponseBody
    public Object queryClueByConditionForPage(String fullname,String company,String phone,String source,String owner,String mphone,String state,int pageNo,int pageSize){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("state",state);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        //调用service方法，查询数据
        List<Clue> clueList = clueService.queryClueByConditionForPage(map);
        int totalRows = clueService.queryCountOfClueByCondition(map);
        //根据查询结果返回响应信息
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("clueList",clueList);
        retMap.put("totalRows",totalRows);
        return  retMap;
    }

    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id,HttpServletRequest request){
        //调用service方法，查询数据
        Clue clue = clueService.queryClueForDetailById(id);
        List<ClueRemark> remarkList = clueRemarkService.queryClueRemarkForDetailByClueId(id);
        List<Activity> activityList = activityService.queryActivityForDetailByClueId(id);
        //把数据保存到作用域
        request.setAttribute("clue",clue);
        request.setAttribute("remarkList",remarkList);
        request.setAttribute("activityList",activityList);
        //请求转发
        return "workbench/clue/detail";
    }

    @RequestMapping("/workbench/clue/queryActivityForDetailByNameClueId.do")
    @ResponseBody
    public Object queryActivityForDetailByNameClueId(String activityName,String clueId){
        //封s装参数
        Map<String,Object> map = new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);

        //调用service ，查询市场活动
        List<Activity> activityList = activityService.queryActivityForDetailByNameClueId(map);
        //根据查询结果返回响应系信息
        return activityList;

    }

    @RequestMapping("/workbench/clue/saveBund.do")
    @ResponseBody
    public Object saveBund(String[] activityId,String clueId){
        //封装参数
        ClueActivityRelation car=null;
        List<ClueActivityRelation> relationList = new ArrayList<>();
        for (String ai : activityId){
           car = new ClueActivityRelation();
           car.setId(UUIDUtils.getUUID());
           car.setClueId(clueId);
           car.setActivityId(ai);
            relationList.add(car);
        }

        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service方法，批量保存线索和市场活动的关联关系
            int ret = clueActivityRelationService.saveCreateClueActivityRelationByList(relationList);
            List<Activity> activityList = activityService.queryActivityForDetailByIds(activityId);

            if (ret>0){
                returnObject.setCode(Contans.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetDate(activityList);
            }else {
                returnObject.setCode(Contans.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后再试。。。。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contans.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后再试。。。。");
        }

        return returnObject;

    }

    @RequestMapping("/workbench/clue/saveUnBund.do")
    @ResponseBody
    public Object saveUnBund(ClueActivityRelation relation){
        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service层方法
            int ret = clueActivityRelationService.deleteClueActivityRelationByClueIdActivityId(relation);

            if (ret>0){
                returnObject.setCode(Contans.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contans.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试。。。。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contans.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试。。。。");
        }
        return returnObject;
    }


    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id,HttpServletRequest request){
        Clue clue = clueService.queryClueForDetailById(id);
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");

        //把数据保存到作用域中
        request.setAttribute("clue",clue);
        request.setAttribute("stageList",stageList);
        //请求转发
        return  "workbench/clue/convert";
    }

    @RequestMapping("/workbench/clue/queryActivityForConvertByNameClueId.do")
    @ResponseBody
    public Object queryActivityForConvertByNameClueId(String activityName,String clueId){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        //调用service
        List<Activity> activityList = activityService.queryActivityForConvertByNameClueId(map);
        //根据查询结果，返回响应信息
        return activityList;
    }

    @RequestMapping("/workbench/clue/convertClue.do")
    @ResponseBody
    public Object convertClue(String clueId,String money,String name,String expectedDate,String stage,String activityId,String isCreateTran,HttpSession session){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("clueId",clueId);
        map.put("money",money);
        map.put("name",name);
        map.put("expectedDate",expectedDate);
        map.put("stage",stage);
        map.put("activityId",activityId);
        map.put("isCreateTran",isCreateTran);
        map.put(Contans.SESSION_USER,session.getAttribute(Contans.SESSION_USER));

        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service，保存线索转换
            clueService.saveConvertClue(map);

            returnObject.setCode(Contans.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contans.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试。。。。");
        }
        return returnObject;
    }

}
