package com.yiling.admin.data.center.report.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.data.center.report.form.AddReportPriceForm;
import com.yiling.admin.data.center.report.form.QueryReportPricePageForm;
import com.yiling.admin.data.center.report.form.UpdateReportPriceForm;
import com.yiling.admin.data.center.report.vo.ReportPriceVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.goods.ylprice.api.GoodsYilingPriceApi;
import com.yiling.goods.ylprice.dto.GoodsYilingPriceDTO;
import com.yiling.goods.ylprice.dto.request.AddGoodsYilingPriceLogRequest;
import com.yiling.goods.ylprice.dto.request.AddReportPriceRequest;
import com.yiling.goods.ylprice.dto.request.QueryReportPricePageRequest;
import com.yiling.goods.ylprice.dto.request.UpdateReportPriceRequest;
import com.yiling.settlement.report.api.ReportParamApi;
import com.yiling.user.system.api.AdminApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.Admin;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.request.QueryAdminPageListRequest;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2022/2/22
 */
@RestController
@RequestMapping("/report/param")
@Api(tags = "报表参数")
@Slf4j
public class ReportPriceController extends BaseController {
    @DubboReference
    private ReportParamApi reportParamApi;
    @DubboReference
    private GoodsYilingPriceApi goodsYilingPriceApi;
    @DubboReference
    private UserApi userApi;
    @DubboReference
    private AdminApi adminApi;

    @Log(title = "商销价-添加价格", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "添加价格")
    @PostMapping("addReportPrice")
    public Result<BoolObject> addReportPrice(@CurrentUser CurrentAdminInfo adminInfo, @Valid @RequestBody AddReportPriceForm addReportPriceForm) {
        // 出货价、供货价，以岭品ID不能为空、不能为0
        Long paramId = addReportPriceForm.getParamId();
        if (ObjectUtil.equal(paramId, 1L) || ObjectUtil.equal(paramId, 2L)) {
            Long goodsId = addReportPriceForm.getGoodsId();
            if (ObjectUtil.isNull(goodsId) || 0 == goodsId.intValue()) {
                throw new BusinessException(ResultCode.FAILED, "以岭商品ID不能为空或0，请选择其他规格的商品");
            }
        }
        //todo gxl 时间校验
        AddReportPriceRequest addReportPriceRequest = new AddReportPriceRequest();
        PojoUtils.map(addReportPriceForm, addReportPriceRequest);
        addReportPriceRequest.setOpUserId(adminInfo.getCurrentUserId());
        GoodsYilingPriceDTO goodsYilingPriceDTO = goodsYilingPriceApi.addReportPrice(addReportPriceRequest);
        if (ObjectUtil.isNotNull(goodsYilingPriceDTO)) {
            AddGoodsYilingPriceLogRequest after = PojoUtils.map(goodsYilingPriceDTO, AddGoodsYilingPriceLogRequest.class);
            reportParamApi.addPriceLog(null, after, addReportPriceRequest.getOpUserId());
        }
        return Result.success(new BoolObject(true));
    }

    @ApiOperation(value = "价格列表")
    @GetMapping("listReportPricePage")
    public Result<Page<ReportPriceVO>> listReportPricePage(QueryReportPricePageForm queryReportPricePageForm) {
        QueryReportPricePageRequest queryReportPricePageRequest = new QueryReportPricePageRequest();
        PojoUtils.map(queryReportPricePageForm, queryReportPricePageRequest);
        Page<ReportPriceVO> reportPriceVOPage = new Page<ReportPriceVO>(queryReportPricePageForm.getCurrent(), queryReportPricePageForm.getSize());
        if (StringUtils.isNotEmpty(queryReportPricePageForm.getOptName())) {
            QueryAdminPageListRequest request = new QueryAdminPageListRequest();
            request.setCurrent(1).setSize(100);
            request.setName(queryReportPricePageForm.getOptName());
            Page<Admin> adminPage = adminApi.pageList(request);
            if (adminPage.getTotal() > 0) {
                List<Long> list = adminPage.getRecords().stream().map(Admin::getId).collect(Collectors.toList());
                queryReportPricePageRequest.setUserIds(list);
            } else {
                return Result.success(reportPriceVOPage);
            }
        }
        Page<GoodsYilingPriceDTO> reportPriceDTOPage = goodsYilingPriceApi.listReportPricePage(queryReportPricePageRequest);

        if (reportPriceDTOPage.getTotal() == 0) {
            return Result.success(reportPriceVOPage);
        }
        List<Long> userIds = reportPriceDTOPage.getRecords().stream().map(GoodsYilingPriceDTO::getUpdateUser).collect(Collectors.toList());
        Map<Long, UserDTO> userDTOMap = userApi.listByIds(userIds).stream().collect(Collectors.toMap(UserDTO::getId, e -> e));
        reportPriceVOPage = PojoUtils.map(reportPriceDTOPage, ReportPriceVO.class);
        reportPriceVOPage.getRecords().forEach(reportPriceVO -> {
            if (Objects.nonNull(userDTOMap) && Objects.nonNull(userDTOMap.get(reportPriceVO.getUpdateUser()))) {
                reportPriceVO.setUpdateUserName(userDTOMap.get(reportPriceVO.getUpdateUser()).getName());
            } else {
                reportPriceVO.setUpdateUserName(Constants.SEPARATOR_MIDDLELINE);
            }

        });
        return Result.success(reportPriceVOPage);
    }

    @Log(title = "商销价-修改", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation(value = "修改")
    @PostMapping("updateReportPrice")
    public Result<BoolObject> updateReportPrice(@CurrentUser CurrentAdminInfo adminInfo, @Valid @RequestBody UpdateReportPriceForm updateReportPriceForm) {
        GoodsYilingPriceDTO before = goodsYilingPriceApi.getById(updateReportPriceForm.getId());
        if (ObjectUtil.isNull(before)) {
            throw new BusinessException(ResultCode.FAILED, "该商销价信息不存在");
        }
        UpdateReportPriceRequest reportPriceRequest = new UpdateReportPriceRequest();
        reportPriceRequest.setOpUserId(adminInfo.getCurrentUserId());
        PojoUtils.map(updateReportPriceForm, reportPriceRequest);
        GoodsYilingPriceDTO after = goodsYilingPriceApi.updateReportPrice(reportPriceRequest);
        if (ObjectUtil.isNotNull(after)) {
            AddGoodsYilingPriceLogRequest beforeRequest = PojoUtils.map(before, AddGoodsYilingPriceLogRequest.class);
            AddGoodsYilingPriceLogRequest afterRequest = PojoUtils.map(after, AddGoodsYilingPriceLogRequest.class);
            reportParamApi.addPriceLog(beforeRequest, afterRequest, reportPriceRequest.getOpUserId());
        }
        return Result.success(new BoolObject(true));
    }
}