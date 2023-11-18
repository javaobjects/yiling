package com.yiling.erp.client.web;

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
import com.yiling.erp.client.common.ErpClientViewConfig;
import com.yiling.erp.client.dao.TaskConfigDao;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.open.erp.dto.TaskConfig;
import com.yiling.open.erp.enums.ErpTopicName;

@Controller
@RequestMapping({"/task"})
public class TaskConfigController {

    @Autowired
    public TaskConfigDao taskConfigDao;

    @ResponseBody
    @RequestMapping(value={"/getConfigInfo.htm"}, produces={"text/html;charset=UTF-8"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
    public String getOrderIssuedInfo(HttpServletRequest request, HttpServletResponse response)
    {
        //首先获取自己的任务配置信息
        List<TaskConfig> taskConfigList = null;
        ErpClientViewConfig erpClientViewConfig=new ErpClientViewConfig();
        try {
            taskConfigList = taskConfigDao.executeQueryTaskConfigList(InitErpConfig.DB_PATH, InitErpConfig.DB_NAME, "select * from task_config");
            if(!CollectionUtils.isEmpty(taskConfigList)){
               for(TaskConfig taskConfig:taskConfigList){
                   if(taskConfig.getTaskNo().equals(ErpTopicName.ErpGoods.getMethod())){
                       erpClientViewConfig.setGoodsPane("1");
                   }else if (taskConfig.getTaskNo().equals(ErpTopicName.ErpOrder.getMethod())){
                       erpClientViewConfig.setOrderPane("1");
                   }
               }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSONObject.toJSONString(erpClientViewConfig, new SerializerFeature[] { SerializerFeature.WriteNullStringAsEmpty });
    }
}
