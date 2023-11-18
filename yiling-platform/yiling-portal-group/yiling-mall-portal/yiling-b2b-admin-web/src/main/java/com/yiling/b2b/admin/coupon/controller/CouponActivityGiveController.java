package com.yiling.b2b.admin.coupon.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.yiling.b2b.admin.coupon.from.DeleteCouponActivityEnterpriseGiveRecordFrom;
import com.yiling.b2b.admin.coupon.from.QueryCouponActivityEnterpriseGiveFrom;
import com.yiling.b2b.admin.coupon.from.QueryCouponActivityEnterpriseGiveRecordFrom;
import com.yiling.b2b.admin.coupon.from.SaveCouponActivityEnterpriseGiveRecordFrom;
import com.yiling.b2b.admin.coupon.vo.CouponActivityEnterpriseGivePageVO;
import com.yiling.b2b.admin.coupon.vo.CouponActivityEnterpriseGiveRecordPageVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityGiveEnterpriseInfoDTO;
import com.yiling.marketing.couponactivity.dto.request.DeleteCouponActivityEnterpriseGiveRecordRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityEnterpriseGiveRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGiveEnterpriseInfoRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryGiveEnterpriseInfoListRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityGiveEnterpriseInfoRequest;
import com.yiling.marketing.couponactivityautogive.api.CouponActivityAutoGiveApi;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveEnterpriseInfoDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * B2B运营后台 优惠券活动发放
 * </p>
 *
 * @author: houjie.sun
 * @date: 2021/10/29
 */
@Slf4j
@Api(tags = "优惠券活动接口")
@RestController
@RequestMapping("/couponActivityGive")
public class CouponActivityGiveController extends BaseController {

    @DubboReference
    CouponActivityApi         couponActivityApi;
    @DubboReference
    EnterpriseApi             enterpriseApi;
    @DubboReference
    UserApi                   userApi;
    @DubboReference
    CouponActivityAutoGiveApi autoGiveApi;

    @ApiOperation(value = "发放-查询供应商", httpMethod = "POST")
    @PostMapping("/queryEnterpriseListPage")
    public Result<Page<CouponActivityEnterpriseGivePageVO>> queryEnterpriseListPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryCouponActivityEnterpriseGiveFrom from) {
        QueryCouponActivityEnterpriseGiveRequest request = PojoUtils.map(from, QueryCouponActivityEnterpriseGiveRequest.class);
        Page<CouponActivityEnterpriseGivePageVO> page = request.getPage();
        // 查询企业信息
        QueryEnterprisePageListRequest enterprisePageListRequest = new QueryEnterprisePageListRequest();
        enterprisePageListRequest.setCurrent(request.getCurrent());
        enterprisePageListRequest.setSize(request.getSize());
        enterprisePageListRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
        enterprisePageListRequest.setStatus(EnableStatusEnum.ENABLED.getCode());
        // B2B不展示工业、商业类型，仅展示终端类型可发放
        List<Integer> notInTypeList = new ArrayList<>();
        notInTypeList.add(EnterpriseTypeEnum.INDUSTRY.getCode());
        notInTypeList.add(EnterpriseTypeEnum.BUSINESS.getCode());
        enterprisePageListRequest.setNotInTypeList(notInTypeList);
        if (ObjectUtil.isNotNull(request.getEid()) && request.getEid() != 0) {
            enterprisePageListRequest.setId(request.getEid());
        }
        if (StrUtil.isNotBlank(request.getEname())) {
            enterprisePageListRequest.setName(request.getEname());
        }
        if (ObjectUtil.isNotNull(request.getEtype()) && request.getEtype() != 0) {
            enterprisePageListRequest.setType(request.getEtype());
        }
        if (StrUtil.isNotBlank(request.getRegionCode())) {
            enterprisePageListRequest.setProvinceCode(request.getRegionCode());
        }
        Page<EnterpriseDTO> enterpriseDTOPage = enterpriseApi.pageList(enterprisePageListRequest);
        List<CouponActivityEnterpriseGivePageVO> list = new ArrayList<>();
        if (ObjectUtil.isNotNull(enterpriseDTOPage) && CollUtil.isNotEmpty(enterpriseDTOPage.getRecords())) {
            // 查询已添加的采购商
            Map<Long, CouponActivityAutoGiveEnterpriseInfoDTO> enterpriseInfoMap = new HashMap<>();
            QueryGiveEnterpriseInfoListRequest requestEnterpriseInfo = new QueryGiveEnterpriseInfoListRequest();
            requestEnterpriseInfo.setCouponActivityId(from.getCouponActivityId());
            List<Long> eidList = enterpriseDTOPage.getRecords().stream().map(EnterpriseDTO::getId).distinct().collect(Collectors.toList());
            requestEnterpriseInfo.setEidList(eidList);
            List<CouponActivityAutoGiveEnterpriseInfoDTO> autoGiveEnterpriseInfoList = autoGiveApi.getByEidAndCouponActivityId(requestEnterpriseInfo);
            if(CollUtil.isNotEmpty(autoGiveEnterpriseInfoList)){
                enterpriseInfoMap = autoGiveEnterpriseInfoList.stream().collect(Collectors.toMap(e -> e.getEid(), e -> e, (v1,v2) -> v1));
            }
            // 已添加的采购商发放数量
            CouponActivityEnterpriseGivePageVO couponActivityEnterpriseGive;
            int index = 0;
            for (EnterpriseDTO enterprise : enterpriseDTOPage.getRecords()) {
                couponActivityEnterpriseGive = new CouponActivityEnterpriseGivePageVO();
                couponActivityEnterpriseGive.setEid(enterprise.getId());
                couponActivityEnterpriseGive.setEname(enterprise.getName());
                couponActivityEnterpriseGive.setEtype(enterprise.getType());
                couponActivityEnterpriseGive.setRegionCode(enterprise.getProvinceCode());
                couponActivityEnterpriseGive.setRegionName(enterprise.getProvinceName());
                couponActivityEnterpriseGive.setAuthStatus(enterprise.getAuthStatus());
                // 已发放数量
                CouponActivityAutoGiveEnterpriseInfoDTO enterpriseInfo = enterpriseInfoMap.get(enterprise.getId());
                int giveNum = 0;
                if(ObjectUtil.isNotNull(enterpriseInfo) && ObjectUtil.isNotNull(enterpriseInfo.getGiveNum())){
                    giveNum = enterpriseInfo.getGiveNum();
                }
                couponActivityEnterpriseGive.setGiveNum(giveNum);
                list.add(index, couponActivityEnterpriseGive);
                index++;
            }
        }

        page = PojoUtils.map(enterpriseDTOPage, CouponActivityEnterpriseGivePageVO.class);
        page.setRecords(list);
        return Result.success(page);
    }

    @ApiOperation(value = "发放-查询已发券供应商", httpMethod = "POST")
    @PostMapping("/queryEnterpriseGiveRecordListPage")
    public Result<Page<CouponActivityEnterpriseGiveRecordPageVO>> queryEnterpriseGiveRecordListPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryCouponActivityEnterpriseGiveRecordFrom from) {
        QueryCouponActivityGiveEnterpriseInfoRequest request = PojoUtils.map(from, QueryCouponActivityGiveEnterpriseInfoRequest.class);
        Page<CouponActivityEnterpriseGiveRecordPageVO> page = new Page();
        Page<CouponActivityGiveEnterpriseInfoDTO> givePage = couponActivityApi.queryEnterpriseGiveRecordListPage(request);
        if(ObjectUtil.isNull(givePage) || CollUtil.isEmpty(givePage.getRecords())){
            return Result.success(page);
        }
        // 查询企业信息
        List<Long> eidList = givePage.getRecords().stream().map(CouponActivityGiveEnterpriseInfoDTO::getEid).collect(Collectors.toList());
        List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(eidList);
        if (CollUtil.isEmpty(enterpriseList)) {
            return Result.success(page);
        }
        Map<Long, EnterpriseDTO> emap = enterpriseList.stream().collect(Collectors.toMap(e -> e.getId(), e -> e, (v1, v2) -> v1));
        if (MapUtil.isNotEmpty(emap)) {
            for (CouponActivityGiveEnterpriseInfoDTO giveEnterpriseInfo : givePage.getRecords()) {
                EnterpriseDTO enterprise = emap.get(giveEnterpriseInfo.getEid());
                if (ObjectUtil.isNull(enterprise)) {
                    continue;
                }
                giveEnterpriseInfo.setEname(enterprise.getName());
                giveEnterpriseInfo.setEtype(enterprise.getType());
                giveEnterpriseInfo.setRegionCode(enterprise.getProvinceCode());
                giveEnterpriseInfo.setRegionName(enterprise.getProvinceName());
                giveEnterpriseInfo.setAuthStatus(enterprise.getAuthStatus());
            }
            page = PojoUtils.map(givePage,CouponActivityEnterpriseGiveRecordPageVO.class);
        }
        return Result.success(page);
    }

    @Log(title = "优惠券活动-添加发券供应商",businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "发放-添加供应商进行发券", httpMethod = "POST")
    @PostMapping("/addEnterpriseGiveRecord")
    public Result<Boolean> addEnterpriseGiveRecord(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SaveCouponActivityEnterpriseGiveRecordFrom form) {
        SaveCouponActivityGiveEnterpriseInfoRequest request = PojoUtils.map(form, SaveCouponActivityGiveEnterpriseInfoRequest.class);
        // 用户信息
        request.setOpUserId(staffInfo.getCurrentUserId());
        UserDTO user = userApi.getById(staffInfo.getCurrentUserId());
        request.setOpUserName(user.getUsername());
        // 企业信息
        request.setOwnEid(staffInfo.getCurrentEid());
        EnterpriseDTO enterprise = enterpriseApi.getById(staffInfo.getCurrentEid());
        request.setOwnEname(enterprise.getName());
        return Result.success(couponActivityApi.addGiveEnterpriseInfo(request));
    }

    @Log(title = "优惠券活动-删除已发券供应商",businessType = BusinessTypeEnum.DELETE)
    @ApiOperation(value = "发放-删除已发券供应商", httpMethod = "POST")
    @PostMapping("/deleteEnterpriseGiveRecord")
    public Result<Boolean> deleteEnterpriseLimit(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid DeleteCouponActivityEnterpriseGiveRecordFrom form) {
        DeleteCouponActivityEnterpriseGiveRecordRequest request = PojoUtils.map(form, DeleteCouponActivityEnterpriseGiveRecordRequest.class);
        request.setUserId(staffInfo.getCurrentUserId());
        request.setEid(staffInfo.getCurrentEid());
        return Result.success(couponActivityApi.deleteEnterpriseGiveRecord(request));
    }

}
