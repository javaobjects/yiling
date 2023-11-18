package com.yiling.hmc.admin.settlement.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.admin.settlement.form.EnterpriseSettlementPageForm;
import com.yiling.hmc.admin.settlement.vo.EnterpriseSettlementPageResultVO;
import com.yiling.hmc.admin.settlement.vo.EnterpriseSettlementPageVO;
import com.yiling.hmc.admin.settlement.vo.SettlementEnterpriseVO;
import com.yiling.hmc.order.api.OrderDetailControlApi;
import com.yiling.hmc.order.bo.OrderDetailControlBO;
import com.yiling.hmc.settlement.api.EnterpriseSettlementApi;
import com.yiling.hmc.settlement.bo.EnterpriseSettlementPageBO;
import com.yiling.hmc.settlement.bo.EnterpriseSettlementPageResultBO;
import com.yiling.hmc.settlement.bo.SettlementEnterprisePageBO;
import com.yiling.hmc.settlement.dto.request.EnterpriseSettlementPageRequest;
import com.yiling.hmc.settlement.dto.request.SettlementEnterprisePageRequest;
import com.yiling.hmc.settlement.enums.TerminalSettlementStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 药品结算账单
 *
 * @author: yong.zhang
 * @date: 2022/6/29
 */
@Api(tags = "药品结算账单")
@Slf4j
@RestController
@RequestMapping("/settlement/enterprise")
public class EnterpriseSettlementController extends BaseController {

    @DubboReference
    EnterpriseSettlementApi enterpriseSettlementApi;

    @DubboReference
    OrderDetailControlApi orderDetailControlApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @ApiOperation(value = "药品结算账单主信息查询")
    @PostMapping("queryPayAmount")
    public Result<SettlementEnterpriseVO> queryPayAmount(@CurrentUser CurrentStaffInfo staffInfo) {
        List<Long> eidList = new ArrayList<Long>() {{
            add(staffInfo.getCurrentEid());
        }};
        SettlementEnterprisePageRequest pageRequest = new SettlementEnterprisePageRequest().setEidList(eidList);
        List<SettlementEnterprisePageBO> boList = enterpriseSettlementApi.pageSettlement(pageRequest);

        SettlementEnterpriseVO settlementEnterpriseVO = new SettlementEnterpriseVO();
        settlementEnterpriseVO.setEid(staffInfo.getCurrentEid());
        settlementEnterpriseVO.setEnPayAmount(BigDecimal.ZERO);
        settlementEnterpriseVO.setUnPayAmount(BigDecimal.ZERO);
        if (CollUtil.isEmpty(boList)) {
            return Result.success(settlementEnterpriseVO);
        }

        Map<String, SettlementEnterprisePageBO> boMap = boList.stream().collect(Collectors.toMap(u -> u.getEid() + "-" + u.getTerminalSettleStatus(), e -> e, (k1, k2) -> k1));
        SettlementEnterprisePageBO unPayBO = boMap.get(staffInfo.getCurrentEid() + "-1");
        SettlementEnterprisePageBO enPayBO = boMap.get(staffInfo.getCurrentEid() + "-2");
        if (null != unPayBO) {
            settlementEnterpriseVO.setUnPayAmount(unPayBO.getPayAmount());
        }
        if (null != enPayBO) {
            settlementEnterpriseVO.setEnPayAmount(enPayBO.getPayAmount());
        }
        return Result.success(settlementEnterpriseVO);
    }

    @ApiOperation(value = "终端结算账单分页查询")
    @PostMapping("/pageList")
    public Result<EnterpriseSettlementPageResultVO> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid EnterpriseSettlementPageForm form) {
        EnterpriseSettlementPageRequest request = new EnterpriseSettlementPageRequest();
        if (2 == form.getTerminalSettleStatus()) {
            request.setTerminalSettleStatus(form.getTerminalSettleStatus());
            request.setPayStartTime(form.getStartTime());
            request.setPayStopTime(form.getStopTime());
        } else {
            request = PojoUtils.map(form, EnterpriseSettlementPageRequest.class);
        }
        request.setEid(staffInfo.getCurrentEid());
        EnterpriseSettlementPageResultBO resultBO = enterpriseSettlementApi.pageResult(request);
        EnterpriseSettlementPageResultVO resultVO = PojoUtils.map(resultBO, EnterpriseSettlementPageResultVO.class);
        Page<EnterpriseSettlementPageBO> dtoPage = resultBO.getPage();
        if (CollUtil.isEmpty(dtoPage.getRecords())) {
            return Result.success(resultVO);
        }

        Page<EnterpriseSettlementPageVO> voPage = PojoUtils.map(dtoPage, EnterpriseSettlementPageVO.class);

        // 待结算列表才需要展示管控渠道
        if (TerminalSettlementStatusEnum.getByCode(form.getTerminalSettleStatus()) == TerminalSettlementStatusEnum.UN_SETTLEMENT) {
            voPage.getRecords().forEach(e -> {
                List<OrderDetailControlBO> orderDetailControlBOS = orderDetailControlApi.listByOrderIdAndSellSpecificationsIdList(e.getOrderId(), new ArrayList<Long>() {{
                    add(e.getSellSpecificationsId());
                }});
                if (CollUtil.isNotEmpty(orderDetailControlBOS)) {
                    OrderDetailControlBO controlBO = orderDetailControlBOS.get(0);
                    List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.listByIds(controlBO.getEidList());
                    List<String> channelNameList = enterpriseDTOS.stream().map(EnterpriseDTO::getName).collect(Collectors.toList());
                    e.setChannelNameList(channelNameList);
                }
            });
        }
        resultVO.setPage(voPage);
        return Result.success(resultVO);
    }
}
