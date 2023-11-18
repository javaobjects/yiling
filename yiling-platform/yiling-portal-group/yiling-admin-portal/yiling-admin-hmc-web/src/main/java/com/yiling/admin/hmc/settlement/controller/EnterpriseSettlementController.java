package com.yiling.admin.hmc.settlement.controller;


import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.util.ExeclImportUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.hmc.settlement.form.EnterpriseSettlementImportForm;
import com.yiling.admin.hmc.settlement.form.EnterpriseSettlementPageForm;
import com.yiling.admin.hmc.settlement.form.ImportEnterpriseSettlementForm;
import com.yiling.admin.hmc.settlement.form.SettlementEnterprisePageForm;
import com.yiling.admin.hmc.settlement.handler.ImportEnterpriseSettlementDataHandler;
import com.yiling.admin.hmc.settlement.vo.EnterpriseSettlementPageResultVO;
import com.yiling.admin.hmc.settlement.vo.EnterpriseSettlementPageVO;
import com.yiling.admin.hmc.settlement.vo.SettlementEnterprisePageVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.vo.ImportResultVO;
import com.yiling.hmc.control.api.GoodsPurchaseControlApi;
import com.yiling.hmc.enterprise.api.EnterpriseAccountApi;
import com.yiling.hmc.enterprise.dto.EnterpriseAccountDTO;
import com.yiling.hmc.enterprise.dto.request.EnterpriseAccountPageRequest;
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
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 商家结账表 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-31
 */
@Api(tags = "商家结账接口")
@Slf4j
@RestController
@RequestMapping("/settlement/enterprise")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnterpriseSettlementController extends BaseController {

    @DubboReference
    EnterpriseSettlementApi enterpriseSettlementApi;

    @DubboReference
    EnterpriseAccountApi enterpriseAccountApi;

    @DubboReference
    GoodsPurchaseControlApi goodsPurchaseControlApi;

    @DubboReference
    OrderDetailControlApi orderDetailControlApi;

    @DubboReference
    EnterpriseApi enterpriseApi;


    private final ImportEnterpriseSettlementDataHandler importEnterpriseSettlementDataHandler;

    @ApiOperation(value = "以岭给商家药品对账分页查询")
    @PostMapping("/pageSettlement")
    public Result<Page<SettlementEnterprisePageVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody SettlementEnterprisePageForm form) {
        EnterpriseAccountPageRequest request = PojoUtils.map(form, EnterpriseAccountPageRequest.class);
        Page<EnterpriseAccountDTO> dtoPage = enterpriseAccountApi.pageList(request);
        Page<SettlementEnterprisePageVO> voPage = PojoUtils.map(dtoPage, SettlementEnterprisePageVO.class);
        if (CollUtil.isEmpty(voPage.getRecords())) {
            return Result.success(voPage);
        }

        List<Long> eidList = dtoPage.getRecords().stream().map(EnterpriseAccountDTO::getEid).collect(Collectors.toList());
        SettlementEnterprisePageRequest pageRequest = new SettlementEnterprisePageRequest().setEidList(eidList);
        List<SettlementEnterprisePageBO> boList = enterpriseSettlementApi.pageSettlement(pageRequest);
        Map<String, SettlementEnterprisePageBO> boMap = boList.stream().collect(Collectors.toMap(u -> u.getEid() + "-" + u.getTerminalSettleStatus(), e -> e, (k1, k2) -> k1));

        List<SettlementEnterprisePageBO> enList = enterpriseSettlementApi.countEnSettlementOrder(pageRequest);
        List<SettlementEnterprisePageBO> unList = enterpriseSettlementApi.countUnSettlementOrder(pageRequest);
        Map<Long, Integer> enMap = enList.stream().collect(Collectors.toMap(SettlementEnterprisePageBO::getEid, SettlementEnterprisePageBO::getPayCount, (k1, k2) -> k1));
        Map<Long, Integer> unMap = unList.stream().collect(Collectors.toMap(SettlementEnterprisePageBO::getEid, SettlementEnterprisePageBO::getPayCount, (k1, k2) -> k1));

        voPage.getRecords().forEach(e -> {
            // 药品终端结算状态 1-待结算/2-已打款/3-无需结算失效单
            SettlementEnterprisePageBO unPayBO = boMap.get(e.getEid() + "-1");
            SettlementEnterprisePageBO enPayBO = boMap.get(e.getEid() + "-2");
            e.setEnPayCount(0);
            e.setEnPayAmount(BigDecimal.ZERO);
            e.setUnPayCount(0);
            e.setUnPayAmount(BigDecimal.ZERO);
            if (null != unPayBO) {
                Integer unPayCount = unMap.get(e.getEid());
                e.setUnPayCount(Objects.nonNull(unPayCount) ? unPayCount : 0);
                e.setUnPayAmount(unPayBO.getPayAmount());
            }
            if (null != enPayBO) {
                Integer enPayCount = enMap.get(e.getEid());
                e.setEnPayCount(Objects.nonNull(enPayCount) ? enPayCount : 0);
                e.setEnPayAmount(enPayBO.getPayAmount());
            }
        });
        return Result.success(voPage);
    }

    @ApiOperation(value = "终端结算账单分页查询")
    @PostMapping("/pageList")
    public Result<EnterpriseSettlementPageResultVO> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid EnterpriseSettlementPageForm form) {
        EnterpriseSettlementPageRequest request = PojoUtils.map(form, EnterpriseSettlementPageRequest.class);
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

    @ApiOperation(value = "终端药品已结导入", httpMethod = "POST")
    @PostMapping(value = "importEnterpriseSettlement", headers = "content-type=multipart/form-data")
    public Result<ImportResultVO> importEnterpriseSettlement(@Valid EnterpriseSettlementImportForm form, @CurrentUser CurrentAdminInfo staffInfo) {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        params.setNeedVerify(true);
        params.setVerifyHandler(importEnterpriseSettlementDataHandler);
        params.setKeyIndex(0);
        InputStream in;
        try {
            in = form.getFile().getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }

        ImportResultModel<ImportEnterpriseSettlementForm> importResultModel;
        try {
            //包含了插入数据库失败的信息
            long start = System.currentTimeMillis();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, staffInfo.getCurrentUserId());
            paramMap.put("eid", form.getEid());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportEnterpriseSettlementForm.class, params, importEnterpriseSettlementDataHandler, paramMap);
            log.info("终端药品已结导入耗时：{},导入数据为:[{}]", System.currentTimeMillis() - start, importResultModel);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }
        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    }
}
