package com.yiling.hmc.admin.mr.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.hmc.admin.mr.form.AddOrRemoveSalesGoodsForm;
import com.yiling.hmc.admin.mr.form.QueryMrPageListForm;
import com.yiling.hmc.admin.mr.form.QuerySalesGoodsPageListForm;
import com.yiling.hmc.admin.mr.vo.MrDetailPageVO;
import com.yiling.hmc.admin.mr.vo.MrPageListItemVO;
import com.yiling.hmc.admin.mr.vo.SalesGoodsPageListItemVO;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.DoctorInfoDTO;
import com.yiling.ih.user.dto.request.QueryMrDoctorListRequest;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.system.api.MrApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.bo.MrBO;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.request.AddOrRemoveMrSalesGoodsRequest;
import com.yiling.user.system.dto.request.QueryMrPageListRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 医药代表模块
 *
 * @author: xuan.zhou
 * @date: 2022/6/6
 */
@RestController
@RequestMapping("/mr")
@Api(tags = "医药代表模块接口")
@Slf4j
public class MrController extends BaseController {

    @DubboReference
    EmployeeApi employeeApi;
    @DubboReference
    MrApi mrApi;
    @DubboReference
    PopGoodsApi popGoodsApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    DoctorApi doctorApi;

    @ApiOperation(value = "医药代表分页列表")
    @PostMapping("/pageList")
    public Result<Page<MrPageListItemVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryMrPageListForm form) {
        QueryMrPageListRequest request = PojoUtils.map(form, QueryMrPageListRequest.class);
        request.setEid(staffInfo.getCurrentEid());

        Page<MrBO> page = mrApi.pageList(request);
        List<MrBO> list = page.getRecords();
        if (CollUtil.isEmpty(list)) {
            return Result.success(request.getPage());
        }

        List<Long> employeeIds = list.stream().map(MrBO::getId).distinct().collect(Collectors.toList());
        Map<Long, List<Long>> employeeSalesGoodsIdsMap = mrApi.listGoodsIdsByEmployeeIds(employeeIds);

        Page<MrPageListItemVO> pageVO = PojoUtils.map(page, MrPageListItemVO.class);
        pageVO.getRecords().forEach(e -> {
            e.setHasSalesGoodsSettingFlag(employeeSalesGoodsIdsMap.containsKey(e.getId()));
        });

        return Result.success(pageVO);
    }

    @ApiOperation(value = "获取医药代表详细信息")
    @GetMapping("/detail")
    public Result<MrDetailPageVO> detail(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam @ApiParam(value = "员工ID", required = true) Long employeeId) {
        // 员工信息
        EnterpriseEmployeeDTO employeeDTO = employeeApi.getById(employeeId);
        // 用户信息
        UserDTO userDTO = userApi.getById(employeeDTO.getUserId());

        // 药代基本信息
        MrDetailPageVO.BaseInfoVO baseInfoVO = new MrDetailPageVO.BaseInfoVO();
        baseInfoVO.setCode(employeeDTO.getCode());
        baseInfoVO.setMobile(userDTO.getMobile());
        baseInfoVO.setName(userDTO.getName());

        // 药代可售药品信息列表
        List<MrDetailPageVO.SalesGoodsInfoVO> salesGoodsInfoVOList = CollUtil.newArrayList();
        // 获取医药代表设置的可售商品ID集合
        List<Long> goodsIds = mrApi.listGoodsIdsByEmployeeIds(ListUtil.toList(employeeId)).get(employeeId);
        if (!CollUtil.isEmpty(goodsIds)) {
            List<GoodsDTO> goodsDTOList = goodsApi.batchQueryInfo(goodsIds);
            Map<Long, GoodsDTO> goodsDTOMap = goodsDTOList.stream().collect(Collectors.toMap(GoodsDTO::getId, Function.identity()));
            goodsIds.forEach(e -> {
                GoodsDTO goodsDTO = goodsDTOMap.get(e);

                MrDetailPageVO.SalesGoodsInfoVO salesGoodsInfoVO = new MrDetailPageVO.SalesGoodsInfoVO();
                salesGoodsInfoVO.setId(goodsDTO.getId());
                salesGoodsInfoVO.setName(goodsDTO.getName());
                salesGoodsInfoVO.setSellSpecifications(goodsDTO.getSellSpecifications());

                salesGoodsInfoVOList.add(salesGoodsInfoVO);
            });
        }

        QueryMrDoctorListRequest request = new QueryMrDoctorListRequest();
        request.setMrId(employeeId);
        List<DoctorInfoDTO> doctorInfoDTOList = doctorApi.listByMrId(request);
        List<MrDetailPageVO.DoctorInfoVO> doctorInfoVOList = PojoUtils.map(doctorInfoDTOList, MrDetailPageVO.DoctorInfoVO.class);

        MrDetailPageVO pageVO = new MrDetailPageVO();
        pageVO.setBaseInfo(baseInfoVO);
        pageVO.setSalesGoodsInfoList(salesGoodsInfoVOList);
        pageVO.setDoctorInfoList(doctorInfoVOList);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "可售商品分页列表")
    @PostMapping("/salesGoodsPageList")
    public Result<Page<SalesGoodsPageListItemVO>> salesGoodsPageList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QuerySalesGoodsPageListForm form) {
        QueryGoodsPageListRequest request = new QueryGoodsPageListRequest();
        request.setCurrent(form.getCurrent());
        request.setSize(form.getSize());
        request.setName(form.getName());
        // 区分以岭、非以岭
        if (staffInfo.getYilingFlag()) {
            List<Long> subEids = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
            request.setEidList(subEids);
        } else {
            request.setEidList(ListUtil.toList(staffInfo.getCurrentEid()));
        }
        // 可售商品分页列表
        Page<GoodsListItemBO> page = popGoodsApi.queryPopGoodsPageList(request);

        List<GoodsListItemBO> list = page.getRecords();
        if (CollUtil.isEmpty(list)) {
            return Result.success(form.getPage());
        }

        List<Long> goodsIds = list.stream().map(GoodsListItemBO::getId).distinct().collect(Collectors.toList());
        List<Long> addedGoodsIds = mrApi.listGoodsIdsByEmployeeIdAndGoodsIds(form.getEmployeeId(), goodsIds);

        Page<SalesGoodsPageListItemVO> pageVO = PojoUtils.map(page, SalesGoodsPageListItemVO.class);
        pageVO.getRecords().forEach(e -> {
            e.setAddedFlag(addedGoodsIds.contains(e.getId()));
        });

        return Result.success(pageVO);
    }

    @ApiOperation(value = "添加或删除可售商品")
    @PostMapping("/addOrRemoveSalesGoods")
    public Result addOrRemoveSalesGoods(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody AddOrRemoveSalesGoodsForm form) {
        AddOrRemoveMrSalesGoodsRequest request = new AddOrRemoveMrSalesGoodsRequest();
        request.setEmployeeId(form.getEmployeeId());
        request.setGoodsIds(form.getGoodsIds());
        request.setOpType(form.getOpType());
        request.setOpUserId(staffInfo.getCurrentUserId());

        Boolean result = mrApi.addOrRemoveSalesGoods(request);
        return Result.success(result);
    }
}
