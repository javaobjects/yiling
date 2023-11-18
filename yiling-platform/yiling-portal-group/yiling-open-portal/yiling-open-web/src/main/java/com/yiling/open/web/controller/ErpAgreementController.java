package com.yiling.open.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.reflect.TypeToken;
import com.yiling.framework.common.annotations.ErpLogAnnotation;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.open.erp.dto.ErpAgreementDTO;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.order.order.api.OrderAddressApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderErpApi;
import com.yiling.user.agreement.api.AgreementApplyApi;
import com.yiling.user.agreement.dto.AgreementApplyOpenDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;

import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@RestController
@Slf4j
public class ErpAgreementController extends OpenBaseController {

    @DubboReference
    OrderApi        orderApi;
    @DubboReference
    EnterpriseApi   enterpriseApi;
    @DubboReference
    OrderAddressApi orderAddressApi;
    @DubboReference
    OrderErpApi     orderErpApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
	@DubboReference
	AgreementApplyApi    agreementApplyApi;

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;

    @ErpLogAnnotation
    @PostMapping(path = "/agreement/push")
    public Result<String> agreementPush(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = getRequestParams(request);
        Long suId = getSuIdByAppKey(params.get(OpenConstants.APP_KEY));
        if (suId == null) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "商业公司不存在");
        }

        ErpTaskInterfaceDTO erpTaskInterface = getInterfaceByTaskNo(params.get(OpenConstants.METHOD));
        if (erpTaskInterface == null) {
            return Result.failed(OpenErrorCode.Invalid_Method.getCode(),
                    "参数" + OpenConstants.METHOD + "不存在");
        }

        String body = getDataRequest(request);
        if (StringUtils.isBlank(body)) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "参数" + OpenConstants.DATA_PARAM + "不存在");
        }

        JSONObject JsonBody = JSON.parseObject(body);
        if (CollectionUtils.isEmpty(JsonBody)) {
            return Result.failed(OpenErrorCode.Parameter_Body_Null.getCode(), OpenConstants.DATA_PARAM + "值为空");
        }

        List<ErpAgreementDTO> erpOrderList;
        try {
			List<AgreementApplyOpenDTO> dtoList = agreementApplyApi.queryAgreementApplyOpenList(null, null);
			erpOrderList= PojoUtils.map(dtoList,ErpAgreementDTO.class);
			//转换功能
            composeERPOrderResult(suId, erpOrderList, JsonBody.getInteger("pageSize"));
            //订单验证
            verifyERPOrderResult(suId, erpOrderList);
        } catch (Exception e) {
            log.error("抽取工具获取订单接口报错", e);
            return Result.failed(OpenErrorCode.Remote_Service_Error);
        }
        return Result.success(JSONArray.toJSONString(erpOrderList));
    }

    private void composeERPOrderResult(Long suId, List<ErpAgreementDTO> erpOrderList, Integer pageSize) {
    }

    private void verifyERPOrderResult(Long suId, List<ErpAgreementDTO> erpOrderList) {

    }

    @ErpLogAnnotation
    @PostMapping(path = "/agreement/back")
    public Result<String> agreementBack(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = getRequestParams(request);
        Long suId = getSuIdByAppKey(params.get(OpenConstants.APP_KEY));
        if (suId == null) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "商业公司不存在");
        }

        ErpTaskInterfaceDTO erpTaskInterface = getInterfaceByTaskNo(params.get(OpenConstants.METHOD));
        if (erpTaskInterface == null) {
            return Result.failed(OpenErrorCode.Invalid_Method.getCode(),
                    "参数" + OpenConstants.METHOD + "不存在");
        }

        String body = getDataRequest(request);
        if (StringUtils.isBlank(body)) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "参数" + OpenConstants.DATA_PARAM + "不存在");
        }


        try {
            List<Map<String, String>> paramList = JSON.parseObject(body, new TypeToken<List<Map<String, String>>>() {
            }.getType());
			List<Long> applyIds= ListUtil.toList();
            for (Map<String, String> param : paramList) {
                if (param.get("erp_sn") == null || StringUtils.isBlank(param.get("order_id"))) {
                    continue;
                }
				applyIds.add(Long.valueOf(param.get("order_id")));
            }
			Boolean isUpdate = agreementApplyApi.applyCompletePush(applyIds);
		} catch (Exception e) {
            log.error("抽取工具回些订单状态接口报错", e);
            return Result.failed(OpenErrorCode.Remote_Service_Error);
        }
        return Result.success();
    }
}
