package com.yiling.open.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.annotations.ErpLogAnnotation;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.util.OpenConstants;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 企业资质查询
 * @author: houjie.sun
 * @date: 2021/9/28
 */
@RestController
@Slf4j
@RequestMapping("/orderAttachment")
public class ErpOrderAttachmentController extends OpenBaseController {

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;
    @Autowired
    private FileService             fileService;

    /**
     * 企业资质查询
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ErpLogAnnotation
    @PostMapping(path = "/getByFileKeyList")
    public Result<String> customerSync(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = getRequestParams(request);
        log.info("查询订单销售合同, header参数为："+ JSON.toJSONString(params));
        Long suId = getSuIdByAppKey(params.get(OpenConstants.APP_KEY));
        if (suId == null) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "商业公司不存在");
        }

        ErpTaskInterfaceDTO erpTaskInterface = getInterfaceByTaskNo(params.get(OpenConstants.METHOD));
        if (erpTaskInterface == null) {
            return Result.failed(OpenErrorCode.Invalid_Method.getCode(), "参数" + OpenConstants.METHOD + "不存在");
        }

        String body = getDataRequest(request);
        log.info("查询订单销售合同, body参数为："+ JSON.toJSONString(body));
        if (StringUtils.isBlank(body)) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "参数" + OpenConstants.DATA_PARAM + "不存在");
        }

        JSONObject jsonBody = JSON.parseObject(body);
        if (CollectionUtils.isEmpty(jsonBody)) {
            return Result.failed(OpenErrorCode.Parameter_Body_Null.getCode(), OpenConstants.DATA_PARAM + "值为空");
        }

        Map<String, String> orderContractMap = new HashMap<>();
        try {
            // 订单销售合同key
            JSONArray fileKeyList = jsonBody.getJSONArray("fileKeyList");
            if (ObjectUtil.isNull(fileKeyList) || fileKeyList.size() == 0) {
                return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "参数" + " fileKeyList 不存在");
            }

            for (Object fileKeyObject : fileKeyList) {
                String fileKey = (String) fileKeyObject;
                // 查询订单销售合同
                String url = fileService.getUrl(fileKey, FileTypeEnum.ORDER_SALES_CONTRACT);
                if (!orderContractMap.containsKey(fileKey)) {
                    orderContractMap.put(fileKey, url);
                }
            }
        } catch (Exception e) {
            log.error("获取订单销售合同接口报错", e);
            return Result.failed(OpenErrorCode.Remote_Service_Error);
        }
        return Result.success(JSONArray.toJSONString(orderContractMap));
    }

}
