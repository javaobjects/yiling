package com.yiling.erp.client.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.yiling.erp.client.dao.TaskConfigDao;
import com.yiling.erp.client.util.InitErpConfig;
import com.yiling.erp.client.util.InitErpSysData;
import com.yiling.open.erp.bo.BaseErpEntity;

import cn.hutool.core.util.StrUtil;

@Controller
@RequestMapping({ "/client" })
public class ClientSqlExecuteController {

    @Autowired
    public InitErpConfig erpCommon;

    @Autowired
    public InitErpSysData initErpSysData;
    @Autowired
    private TaskConfigDao taskConfigDao;

    @ResponseBody
    @RequestMapping(value = { "/sqlExecute.htm" }, produces = { "text/html;charset=UTF-8" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
    public JSONObject sqlExecute(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            String sqlContext = request.getParameter("sqlContext");
            String type = request.getParameter("type");
            String flowDateCount = request.getParameter("flowDateCount");
            Integer count = 0;
            if (StrUtil.isNotEmpty(flowDateCount)) {
                count = Integer.parseInt(flowDateCount);
            }
            List<BaseErpEntity> list = initErpSysData.findDataByMethodNoAndSql(type, sqlContext, count);
            json.put("code", 200);//成功
            json.put("rows", list);
            json.put("total", list.size());
            list=null;
        } catch (Exception e) {
            json.put("code", 1);//失败
            json.put("rows", e.getMessage());
            e.printStackTrace();
        }
        return json;
    }

}
