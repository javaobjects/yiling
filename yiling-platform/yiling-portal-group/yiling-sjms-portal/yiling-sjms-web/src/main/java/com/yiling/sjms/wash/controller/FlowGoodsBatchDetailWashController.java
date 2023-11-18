package com.yiling.sjms.wash.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.dataflow.wash.api.FlowGoodsBatchDetailWashApi;
import com.yiling.dataflow.wash.dto.FlowGoodsBatchDetailWashDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchDetailWashPageRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowPurchaseWashPageRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.wash.form.QueryFlowGoodsBatchDetailWashPageForm;
import com.yiling.sjms.wash.vo.FlowGoodsBatchDetailWashListVO;
import com.yiling.sjms.wash.vo.FlowSaleWashListVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/3/1
 */
@Slf4j
@RestController
@RequestMapping("/flowGoodsBatchDetailWash")
@Api(tags = "月库存清洗明细")
public class FlowGoodsBatchDetailWashController extends BaseController {

    @DubboReference
    private FlowGoodsBatchDetailWashApi flowGoodsBatchDetailWashApi;

    @DubboReference
    private SjmsUserDatascopeApi sjmsUserDatascopeApi;

    @ApiOperation(value = "月库存清洗明细列表")
    @PostMapping("/list")
    public Result<Page<FlowGoodsBatchDetailWashListVO>> queryFlowGoodsBatchDetailWashListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryFlowGoodsBatchDetailWashPageForm form) {
        if (userInfo == null) {
            return Result.success(new Page<>());
        }

        //  查询权限
        SjmsUserDatascopeBO datascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        if (datascopeBO == null || OrgDatascopeEnum.NONE.getCode().equals(datascopeBO.getOrgDatascope())) {
            return Result.success(new Page<>());
        }

        QueryFlowGoodsBatchDetailWashPageRequest request = PojoUtils.map(form, QueryFlowGoodsBatchDetailWashPageRequest.class);
        // 暂不加
//        if (OrgDatascopeEnum.PORTION.getCode().equals(datascopeBO.getOrgDatascope())) {
//            request.setProvinceCodes(datascopeBO.getOrgPartDatascopeBO().getProvinceCodes());
//            request.setCrmEnterpriseIdList(datascopeBO.getOrgPartDatascopeBO().getCrmEids());
//        }

        Page<FlowGoodsBatchDetailWashDTO> page = flowGoodsBatchDetailWashApi.listPage(request);
        if (CollUtil.isEmpty(page.getRecords())) {
            return Result.success(new Page<>());
        }

        Page<FlowGoodsBatchDetailWashListVO> pageResult = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<FlowGoodsBatchDetailWashListVO> list = new ArrayList<>();
        for (FlowGoodsBatchDetailWashDTO flowGoodsBatchDetailWashDTO : page.getRecords()) {
            FlowGoodsBatchDetailWashListVO flowGoodsBatchDetailWashListVO  = PojoUtils.map(flowGoodsBatchDetailWashDTO, FlowGoodsBatchDetailWashListVO.class);
            flowGoodsBatchDetailWashListVO.setGoodsName(flowGoodsBatchDetailWashDTO.getGbName());
            flowGoodsBatchDetailWashListVO.setGbQuantity(flowGoodsBatchDetailWashDTO.getGbNumber());
            list.add(flowGoodsBatchDetailWashListVO);
        }
        pageResult.setRecords(list);
        return Result.success(pageResult);
    }
}
