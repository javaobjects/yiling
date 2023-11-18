package com.yiling.erp.client.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yiling.erp.client.dao.PushConfigDao;
import com.yiling.erp.client.dao.TaskConfigDao;
import com.yiling.erp.client.quartz.JobTaskService;
import com.yiling.erp.client.util.DateUtil;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.erp.client.util.StringUtil;
import com.yiling.open.erp.dto.PushConfig;
import com.yiling.open.erp.dto.TaskConfig;
import com.yiling.open.erp.enums.ErpTopicName;

/**
 * @author shuan
 */
@Controller
@RequestMapping({"/order"})
public class ClientOrderReturnController {

    @Autowired
    private TaskConfigDao taskConfigDao;

    @Autowired
    private JobTaskService jobTaskService;

    @Autowired
    private PushConfigDao pushConfigDao;

    @ResponseBody
    @RequestMapping(value = {"/saveOrderReturn.htm"}, produces = {"text/html;charset=UTF-8"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public String saveOrderReturn(HttpServletRequest request, HttpServletResponse response) {
        TaskConfig taskConfig=new TaskConfig();
        String openDock = request.getParameter("openDock");
        String frequencyValue = request.getParameter("frequency");
        if(Integer.parseInt(frequencyValue) > 60*24){
            return "false";
        }

        taskConfig.setTaskNo(ErpTopicName.ErpOrderBack.getMethod());
        taskConfig.setSpringId("clientOrderReturnService");
        taskConfig.setTaskName("退货单同步");
        taskConfig.setTaskStatus(StringUtil.isEmpty(openDock)?"0":"1");
        taskConfig.setMethodName("syncData");
        taskConfig.setCreateTime(DateUtil.convertDate2String(new Date(), DateUtil.DATE_SPLIT_FORMAT));
        taskConfig.setTaskInterval(frequencyValue);

        //先判断是否已经配置推送连接配置
        if(taskConfig.getTaskStatus().equals("1")) {
            try {
                List<PushConfig> pushConfigList=pushConfigDao.executeQueryPushConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME,"select * from push_config");
                if(CollectionUtils.isEmpty(pushConfigList)){
                    return "false";
                }
             } catch (Exception e) {
                return "false";
            }
        }


        try {
            if(!taskConfigDao.saveTaskConfig(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, taskConfig)){
                return "false";
            }
            if(taskConfig.getTaskStatus().equals("1")) {
                String interval = taskConfig.getTaskInterval();
                taskConfig.setTaskInterval(interval);
                jobTaskService.addJob(taskConfig);
            }else{
                jobTaskService.deleteJob(taskConfig);
            }
        }catch (Exception e) {
            return "false";
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping(value={"/getOrderReturnInfo.htm"}, produces={"text/html;charset=UTF-8"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
    public String getOrderReturnInfo(HttpServletRequest request, HttpServletResponse response)
    {
        TaskConfig taskConfig=null;
        List<TaskConfig> lists= null;
        try {
            lists = taskConfigDao.executeQueryTaskConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from task_config where taskNo="+ ErpTopicName.ErpOrderBack.getMethod());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(CollectionUtils.isEmpty(lists)){
            return null;
        }
        taskConfig=lists.get(0);
        return JSONObject.toJSONString(taskConfig, new SerializerFeature[] { SerializerFeature.WriteNullStringAsEmpty });
    }

}
