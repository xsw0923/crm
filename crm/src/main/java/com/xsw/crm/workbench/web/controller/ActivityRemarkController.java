package com.xsw.crm.workbench.web.controller;

import com.xsw.crm.commons.contans.Contans;
import com.xsw.crm.commons.domain.ReturnObject;
import com.xsw.crm.commons.utils.DateUtils;
import com.xsw.crm.commons.utils.UUIDUtils;
import com.xsw.crm.settings.domain.User;
import com.xsw.crm.workbench.domain.ActivityRemark;
import com.xsw.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ActivityRemarkController {
    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
    @ResponseBody
    public Object saveCreateActivityRemark(ActivityRemark remark, HttpSession session){
        User user = (User) session.getAttribute(Contans.SESSION_USER);
        //封装参数
        remark.setId(UUIDUtils.getUUID());
        remark.setCreateTime(DateUtils.formateDateTime(new Date()));
        remark.setCreateBy(user.getId());
        remark.setEditFlag(Contans.REMARK_EDIT_FLAG_NO_EDITED);
        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service层
            int ret = activityRemarkService.saveCreateActivityRemark(remark);

            if (ret>0){
                returnObject.setCode(Contans.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetDate(remark);
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

    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    @ResponseBody
    public Object deleteActivityRemarkById(String id){
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = activityRemarkService.deleteActivityRemarkById(id);

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

    @RequestMapping("/workbench/activity/saveEditActivityRemark.do")
    @ResponseBody
    public Object saveEditActivityRemark(ActivityRemark remark,HttpSession session){
        User user = (User) session.getAttribute(Contans.SESSION_USER);
        //封装参数
        remark.setEditTime(DateUtils.formateDateTime(new Date()));
        remark.setEditBy(user.getId());
        remark.setEditFlag(Contans.REMARK_EDIT_FLAG_YES_EDITED);

        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service
            int ret = activityRemarkService.saveEditActivityRemark(remark);

            if (ret>0){
                returnObject.setCode(Contans.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetDate(remark);
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
}
