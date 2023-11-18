package com.yiling.sjms.flow.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.api.CrmSupplierApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.enums.CrmSupplierLevelEnum;
import com.yiling.dataflow.order.api.FlowGoodsBatchDetailApi;
import com.yiling.dataflow.order.api.FlowPurchaseApi;
import com.yiling.dataflow.order.api.FlowSaleApi;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.dataflow.statistics.api.FlowBalanceStatisticsApi;
import com.yiling.dataflow.statistics.api.FlowDistributionEnterpriseApi;
import com.yiling.dataflow.statistics.bo.FlowDailyStatisticsBO;
import com.yiling.dataflow.statistics.dto.request.QueryBalanceStatisticsPageRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.sjms.flow.form.QueryBalanceStatisticsForm;
import com.yiling.sjms.flow.vo.FlowBalanceStatisticsVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2023/2/6
 */
@Slf4j
@RestController
@RequestMapping("/flow")
@Api(tags = "日流向汇总")
public class FlowDailyStatisticsController extends BaseController {

    @DubboReference
    private FlowBalanceStatisticsApi flowBalanceStatisticsApi;
    @DubboReference
    private FlowSaleApi              flowSaleApi;
    @DubboReference
    private FlowPurchaseApi          flowPurchaseApi;
    @DubboReference
    private FlowGoodsBatchDetailApi  flowGoodsBatchDetailApi;
    @DubboReference(timeout = 10 * 1000)
    CrmEnterpriseApi              crmEnterpriseApi;
    @DubboReference
    FlowDistributionEnterpriseApi flowDistributionEnterpriseApi;
    @DubboReference
    ErpClientApi                  erpClientApi;
    @DubboReference
    SjmsUserDatascopeApi          sjmsUserDatascopeApi;
    @DubboReference
    private CrmSupplierApi crmSupplierApi;

    @ApiOperation(value = "日流向汇总列表")
    @PostMapping("day/list")
    public Result<Page<FlowBalanceStatisticsVO>> list(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryBalanceStatisticsForm form) {
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("采购渠道查询权限:userDatascopeBO={}", JSONUtil.toJsonStr(userDatascopeBO));
        //代表没权限返回空
        if (null == userDatascopeBO || OrgDatascopeEnum.NONE.getCode().equals(userDatascopeBO.getOrgDatascope())) {
            return Result.success(new Page());
        }
        if (OrgDatascopeEnum.PORTION.getCode().equals(userDatascopeBO.getOrgDatascope())&&CollUtil.isEmpty(userDatascopeBO.getOrgPartDatascopeBO().getCrmEids())&&CollUtil.isEmpty(userDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes())) {
            return Result.success(new Page());
        }
        QueryBalanceStatisticsPageRequest request = PojoUtils.map(form, QueryBalanceStatisticsPageRequest.class);
        if (null != userDatascopeBO.getOrgPartDatascopeBO()) {
            request.setUserDatascopeBO(userDatascopeBO);
        }

        Page<FlowDailyStatisticsBO> page = flowBalanceStatisticsApi.page(request);
        Page<FlowBalanceStatisticsVO> pageVO = PojoUtils.map(page, FlowBalanceStatisticsVO.class);
        if (ObjectUtil.isNotNull(pageVO) && CollUtil.isNotEmpty(pageVO.getRecords())) {
            for (FlowBalanceStatisticsVO vo : pageVO.getRecords()) {
                CrmSupplierLevelEnum crmLevelEnum = CrmSupplierLevelEnum.getByCode(vo.getSupplierLevel());
                if(crmLevelEnum!=null) {
                    vo.setEnameLevel(crmLevelEnum.getName());
                }else{
                    vo.setEnameLevel("");
                }
            }
        }
        return Result.success(pageVO);
    }

}
