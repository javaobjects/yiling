package com.yiling.admin.b2b.coupon.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.coupon.from.DeleteCouponActivityAutoGetMemberLimitFrom;
import com.yiling.admin.b2b.coupon.from.QueryCouponActivityAutoGetFrom;
import com.yiling.admin.b2b.coupon.from.QueryCouponActivityAutoGetMemberLimitFrom;
import com.yiling.admin.b2b.coupon.from.QueryCouponActivityAutoGivePageFrom;
import com.yiling.admin.b2b.coupon.from.QueryCouponActivityEnterpriseFrom;
import com.yiling.admin.b2b.coupon.from.QueryCouponActivityEnterpriseGiveFrom;
import com.yiling.admin.b2b.coupon.from.QueryMemberCouponActivityFrom;
import com.yiling.admin.b2b.coupon.from.SaveCouponActivityAutoGetBasicFrom;
import com.yiling.admin.b2b.coupon.from.SaveCouponActivityAutoGetMemberLimitFrom;
import com.yiling.admin.b2b.coupon.from.SaveCouponActivityAutoGetRulesFrom;
import com.yiling.admin.b2b.coupon.from.SaveMemberCouponActivityFrom;
import com.yiling.admin.b2b.coupon.vo.CouponActivityAutoGetCouponDetailVO;
import com.yiling.admin.b2b.coupon.vo.CouponActivityAutoGetDetailVO;
import com.yiling.admin.b2b.coupon.vo.CouponActivityAutoGetPageVo;
import com.yiling.admin.b2b.coupon.vo.CouponActivityAutoGiveCouponDetailVO;
import com.yiling.admin.b2b.coupon.vo.CouponActivityAutoGiveMemberPageVO;
import com.yiling.admin.b2b.coupon.vo.CouponActivityEnterpriseGivePageVO;
import com.yiling.admin.b2b.coupon.vo.CouponActivityEnterprisePageVO;
import com.yiling.admin.b2b.coupon.vo.CouponActivityGivePageVo;
import com.yiling.admin.b2b.coupon.vo.CouponActivityMemberPageVo;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponActivityStatusEnum;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.coupon.api.CouponApi;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.coupon.dto.request.QueryHasGiveCouponAutoRequest;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEnterpriseLimitDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityGiveDTO;
import com.yiling.marketing.couponactivity.dto.CouponMemberActivityLimitDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityEnterpriseGiveRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityEnterpriseRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGivePageRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponMemberActivityPageRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryGiveEnterpriseInfoListRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityBasicRequest;
import com.yiling.marketing.couponactivityautoget.api.CouponActivityAutoGetApi;
import com.yiling.marketing.couponactivityautoget.dto.request.CouponActivityAutoGetOperateRequest;
import com.yiling.marketing.couponactivityautoget.dto.request.QueryCouponActivityAutoGetRequest;
import com.yiling.marketing.couponactivityautoget.dto.request.SaveCouponActivityAutoGetBasicRequest;
import com.yiling.marketing.couponactivityautoget.dto.request.SaveCouponActivityAutoGetRulesRequest;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGetCouponDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveEnterpriseInfoDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.DeleteCouponActivityAutoGetMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGetMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGetMemberLimitRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseListRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.api.MemberBuyStageApi;
import com.yiling.user.member.dto.MemberSimpleDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * B2B运营后台 优惠券活动自主领取
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Api(tags = "优惠券-自主领券券活动接口")
@RestController
@RequestMapping("/couponActivityAutoGet")
public class CouponActivityAutoGetController extends BaseController {

    @DubboReference
    CouponActivityAutoGetApi couponActivityAutoGetApi;
    @DubboReference
    CouponActivityApi couponActivityApi;
    @DubboReference
    CouponApi couponApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    CouponActivityAutoGetApi autoGetApi;
    @DubboReference
    MemberApi memberApi;
    @DubboReference
    MemberBuyStageApi memberBuyStageApi;


    @Log(title = "会员优惠券活动-保存会员方案", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "领券-保存会员方案", httpMethod = "POST")
    @PostMapping("/saveMemberCouponRelation")
    public Result<Boolean> saveMemberCouponRelation(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveMemberCouponActivityFrom form) {
        SaveCouponActivityBasicRequest request = PojoUtils.map(form, SaveCouponActivityBasicRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setEid(0L);
        return Result.success(couponActivityApi.saveAutoGetMemberCouponRelation(request));
    }

    @Log(title = "领券惠券活动-关联会员方案", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "领券-删除会员方案", httpMethod = "POST")
    @PostMapping("/deleteMemberCouponRelation")
    public Result<Boolean> deleteMemberCouponRelation(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveMemberCouponActivityFrom form) {
        SaveCouponActivityBasicRequest request = PojoUtils.map(form, SaveCouponActivityBasicRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setEid(0L);
        return Result.success(couponActivityApi.deleteAutoGetMemberCouponRelation(request));
    }

    @Log(title = "查询已经保存的会员方案", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "查询已经保存的会员方案", httpMethod = "POST")
    @PostMapping("/queryMemberCouponRelationLimit")
    public Result<Page<CouponActivityMemberPageVo>> queryMemberCouponRelationLimit(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryMemberCouponActivityFrom form) {
        Page<CouponActivityMemberPageVo> page = new Page();
        QueryCouponMemberActivityPageRequest request = new QueryCouponMemberActivityPageRequest();
        request.setId(form.getId());
        Page<CouponMemberActivityLimitDTO> memberBuyStageDTOPage = couponActivityApi.PageActivityAutoGetMeberLimit(request);
        page = PojoUtils.map(memberBuyStageDTOPage, CouponActivityMemberPageVo.class);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            List<MemberSimpleDTO> memberSimpleDTOS = memberApi.queryAllList();
            Map<Long, MemberSimpleDTO> entMap = memberSimpleDTOS.stream().collect(Collectors.toMap(MemberSimpleDTO::getId, e -> e));
            if (CollectionUtils.isNotEmpty(memberSimpleDTOS)) {
                page.getRecords().forEach(item -> {
                    MemberSimpleDTO memberBuyStageDTO = entMap.get(item.getMemberId());
                    if (ObjectUtil.isNotNull(memberBuyStageDTO)) {
                        item.setMemberName(memberBuyStageDTO.getName());
                        item.setName(memberBuyStageDTO.getName());
                    }
                });
            }
        }
        return Result.success(page);
    }

    @ApiOperation(value = "列表", httpMethod = "POST")
    @PostMapping("/queryListPage")
    public Result<Page<CouponActivityAutoGetPageVo>> queryListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryCouponActivityAutoGetFrom form) {
        QueryCouponActivityAutoGetRequest request = PojoUtils.map(form, QueryCouponActivityAutoGetRequest.class);
        Page<CouponActivityAutoGetPageVo> page = PojoUtils.map(couponActivityAutoGetApi.queryListPage(request), CouponActivityAutoGetPageVo.class);
        // 创建人、修改人id
        List<Long> userIdList = this.getGetUserIdList(page.getRecords());
        Map<Long, UserDTO> userMap = this.getUserMapByIds(userIdList);
        long nowTime = System.currentTimeMillis();
        Long currentEid = 0L;
        // 自主领取优惠券数量
        Map<Long, Integer> getCountMap = buildGetCountMapMap(page);

        for (CouponActivityAutoGetPageVo p : page.getRecords()) {
            Long id = p.getId();
            // 已领取数量
            int getCount = ObjectUtil.isNull(getCountMap.get(id)) ? 0 : getCountMap.get(id);
            p.setReceivedNum(getCount);
            // 活动状态 todo 停用、作废的  活动状态=已结束
            if (p.getBeginTime().getTime() > nowTime) {
                p.setActivityStatus(1);
            } else if (p.getBeginTime().getTime() <= nowTime && p.getEndTime().getTime() > nowTime) {
                p.setActivityStatus(2);
            } else if (p.getEndTime().getTime() <= nowTime) {
                p.setActivityStatus(3);
            }
            // 操作人姓名
            String createUserName = this.getUserNameById(userMap, p.getCreateUser());
            String updateUserName = this.getUserNameById(userMap, p.getUpdateUser());
            p.setCreateUserName(createUserName);
            p.setUpdateUserName(updateUserName);

            // 操作标识
            Integer status = p.getStatus();
            boolean updateFlag = true;
            boolean stopFlag = true;
            boolean scrapFlag = true;

            // 可停用标识
            if (!ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), status)) {
                updateFlag = false;
                stopFlag = false;
            }
            // 可作废标识
            if (ObjectUtil.equal(CouponActivityStatusEnum.SCRAP.getCode(), status)) {
                scrapFlag = false;
            }
            // 可修改标识
            if (getCount > 0) {
                updateFlag = false;
            }
            if (p.getEndTime().getTime() <= nowTime) {
                updateFlag = false;
            }

            p.setUpdateFlag(updateFlag);
            p.setStopFlag(stopFlag);
            p.setScrapFlag(scrapFlag);
        }
        return Result.success(page);
    }




    private Map<Long, Integer> buildGetCountMapMap(Page<CouponActivityAutoGetPageVo> page) {
        Map<Long, Integer> getCountMap = new HashMap<>();
        if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
            return getCountMap;
        }
        List<Long> autoGetIdList = page.getRecords().stream().map(CouponActivityAutoGetPageVo::getId).collect(Collectors.toList());
        // 查询关联的优惠券活动
        List<CouponActivityAutoGetCouponDTO> autoGetCouponList = couponActivityAutoGetApi.getByCouponActivityGetIdList(autoGetIdList);
        if (CollUtil.isEmpty(autoGetCouponList)) {
            return getCountMap;
        }
        List<Long> autoGetIdEffectiveList = autoGetCouponList.stream().map(CouponActivityAutoGetCouponDTO::getCouponActivityAutoGetId).distinct().collect(Collectors.toList());
        List<Long> couponActivityIdList = autoGetCouponList.stream().map(CouponActivityAutoGetCouponDTO::getCouponActivityId).distinct().collect(Collectors.toList());
        // 已领取优惠券
        QueryHasGiveCouponAutoRequest getRequest = new QueryHasGiveCouponAutoRequest();
        getRequest.setGetType(CouponGetTypeEnum.AUTO_GET.getCode());
        getRequest.setCouponActivityIds(couponActivityIdList);
        getRequest.setAutoGetIds(autoGetIdEffectiveList);
        getRequest.setBusinessType(1);
        //List<CouponDTO> couponList = couponApi.getHasGiveListByCouponActivityIdList(getRequest);
        List<CouponDTO> couponList = couponApi.getHasGiveCountByCouponActivityIdList(getRequest);
        if (CollUtil.isEmpty(couponList)) {
            return getCountMap;
        }
        Map<Long, Integer> couponMap = couponList.stream().collect(Collectors.toMap(CouponDTO::getCouponActivityAutoId, CouponDTO::getNum));
       for (Long autoGetId : autoGetIdEffectiveList) {
            int getCount = 0;
            Integer couponDTOS = couponMap.get(autoGetId);
            if (ObjectUtil.isNotEmpty(couponDTOS)) {
                getCount = couponDTOS;
            }
            getCountMap.put(autoGetId, getCount);
        }
        return getCountMap;
    }

    @ApiOperation(value = "列表-查看详情", httpMethod = "GET")
    @GetMapping("/getDetail")
    public Result<CouponActivityAutoGetDetailVO> getCouponActivity(@RequestParam("id") Long id) {
        CouponActivityAutoGetDetailVO couponActivityDetail = PojoUtils.map(couponActivityAutoGetApi.getDetailById(id), CouponActivityAutoGetDetailVO.class);
        if (ObjectUtil.isNotNull(couponActivityDetail)) {
            // 关联优惠券列表
            List<CouponActivityAutoGetCouponDTO> list = couponActivityAutoGetApi.getByCouponActivityGetId(couponActivityDetail.getId());
            if (CollUtil.isEmpty(list)) {
                couponActivityDetail.setCouponActivityList(ListUtil.empty());
            } else {
                // 查询优惠券
                List<Long> couponActivityIdList = list.stream().map(CouponActivityAutoGetCouponDTO::getCouponActivityId).distinct().collect(Collectors.toList());
                Map<Long, CouponActivityDetailDTO> activityMap = new HashMap<>();
                List<CouponActivityDetailDTO> activityList = couponActivityApi.getCouponActivityById(couponActivityIdList);
                if (CollUtil.isNotEmpty(activityList)) {
                    activityMap = activityList.stream().collect(Collectors.toMap(a -> a.getId(), a -> a, (v1, v2) -> v1));
                }
                // 用户卡包里面的优惠券数量
                List<CouponDTO> hasGiveCouponList = couponApi.getHasGiveCountByCouponActivityList(couponActivityIdList);

                Map<Long, Integer> giveCountMap = new HashMap<>();
                if (CollUtil.isNotEmpty(hasGiveCouponList)) {
                    giveCountMap = hasGiveCouponList.stream().collect(Collectors.toMap(CouponDTO::getCouponActivityId,CouponDTO::getNum));
                }
                List<CouponActivityAutoGetCouponDetailVO> couponActivityList = new ArrayList<>();
                CouponActivityAutoGetCouponDetailVO detail;
                for (CouponActivityAutoGetCouponDTO dto : list) {
                    detail = new CouponActivityAutoGetCouponDetailVO();
                    Long couponActivityId = dto.getCouponActivityId();
                    detail.setId(couponActivityId);
                    detail.setGiveNum(dto.getGiveNum());
                    detail.setGiveNumDaily(dto.getGiveNumDaily());
                    //补充优惠券信息
                    CouponActivityDetailDTO couponActivity = activityMap.get(couponActivityId);
                    if (ObjectUtil.isNotNull(couponActivity)) {
                        detail.setEnterpriseLimit(couponActivity.getEnterpriseLimit());
                        detail.setMemberType(couponActivity.getMemberType());
                        detail.setName(couponActivity.getName());
                        Integer couponDTOS = giveCountMap.get(couponActivityId);
                        detail.setSurplusCount(couponActivity.getTotalCount());
                        if(ObjectUtil.isNotNull(couponDTOS)){
                            detail.setSurplusCount(couponActivity.getTotalCount()-couponDTOS);
                        }
                    }
                    couponActivityList.add(detail);
                }
                couponActivityDetail.setCouponActivityList(couponActivityList);
            }
            // 未开始、已开始状态
            boolean running = false;
            long nowTime = System.currentTimeMillis();
            Date beginTime = couponActivityDetail.getBeginTime();
            Date endTime = couponActivityDetail.getEndTime();
            if (beginTime.getTime() <= nowTime && endTime.getTime() > nowTime) {
                running = true;
            }
            couponActivityDetail.setRunning(running);
        }
        return Result.success(couponActivityDetail);
    }

    @Log(title = "自动领券活动-新增", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "列表-保存/编辑基本信息", httpMethod = "POST")
    @PostMapping("/saveOrUpdateBasic")
    public Result<String> saveOrUpdateBasic(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveCouponActivityAutoGetBasicFrom form) {
        SaveCouponActivityAutoGetBasicRequest request = PojoUtils.map(form, SaveCouponActivityAutoGetBasicRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        Long id = couponActivityAutoGetApi.saveOrUpdateBasic(request);
        return Result.success(ObjectUtil.isNull(id) ? "" : id.toString());
    }

    @Log(title = "自动领券活动-修改规则", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation(value = "列表-保存/编辑规则信息", httpMethod = "POST")
    @PostMapping("/saveOrUpdateRules")
    public Result<String> saveOrUpdateRules(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveCouponActivityAutoGetRulesFrom form) {
        SaveCouponActivityAutoGetRulesRequest request = PojoUtils.map(form, SaveCouponActivityAutoGetRulesRequest.class);
        request.setCurrentUserId(adminInfo.getCurrentUserId());
        Long id = couponActivityAutoGetApi.saveOrUpdateRules(request);
        return Result.success(ObjectUtil.isNull(id) ? "" : id.toString());
    }

    @Log(title = "自动领券活动-停用", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation(value = "列表-停用", httpMethod = "GET")
    @GetMapping("/stop")
    public Result<Boolean> stop(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("id") Long id) {
        CouponActivityAutoGetOperateRequest request = new CouponActivityAutoGetOperateRequest();
        request.setId(id);
        request.setUserId(adminInfo.getCurrentUserId());
        return Result.success(couponActivityAutoGetApi.stop(request));
    }

    @Log(title = "自动领券活动-启用", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation(value = "列表-启用", httpMethod = "GET")
    @GetMapping("/enable")
    public Result<Boolean> enable(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("id") Long id) {
        CouponActivityAutoGetOperateRequest request = new CouponActivityAutoGetOperateRequest();
        request.setId(id);
        request.setUserId(adminInfo.getCurrentUserId());
        return Result.success(couponActivityAutoGetApi.enable(request));
    }

    @Log(title = "自动领券活动-作废", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation(value = "列表-作废", httpMethod = "GET")
    @GetMapping("/scrap")
    public Result<Boolean> scrap(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("id") Long id) {
        CouponActivityAutoGetOperateRequest request = new CouponActivityAutoGetOperateRequest();
        request.setId(id);
        request.setUserId(adminInfo.getCurrentUserId());
        return Result.success(couponActivityAutoGetApi.scrap(request));
    }

    @ApiOperation(value = "列表-已领取优惠券查看", httpMethod = "POST")
    @PostMapping("/queryAutoGiveListPage")
    public Result<Page<CouponActivityGivePageVo>> queryAutoGiveListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryCouponActivityAutoGivePageFrom form) {
        QueryCouponActivityGivePageRequest request = PojoUtils.map(form, QueryCouponActivityGivePageRequest.class);
        request.setEid(0L);
        request.setGetType(CouponGetTypeEnum.AUTO_GET.getCode());
        Page<CouponActivityGiveDTO> page = couponActivityAutoGetApi.queryGiveListPage(request);
        if (ObjectUtil.isNotNull(page) && CollUtil.isNotEmpty(page.getRecords())) {
            List<Long> userIdList = page.getRecords().stream().map(CouponActivityGiveDTO::getGetUserId).distinct().collect(Collectors.toList());
            Map<Long, UserDTO> userMap = this.getUserMapByIds(userIdList);
            List<Long> eidList = page.getRecords().stream().map(CouponActivityGiveDTO::getEid).distinct().collect(Collectors.toList());
            Map<Long, EnterpriseDTO> enterpriseMap = this.getEnterpriseMapByIds(eidList);
            page.getRecords().forEach(p -> {
                String getUserName = this.getUserNameById(userMap, p.getGetUserId());
                p.setGetUserName(getUserName);
                EnterpriseDTO enterprise = enterpriseMap.get(p.getEid());
                if (ObjectUtil.isNotNull(enterprise)) {
                    p.setEname(enterprise.getName());
                }
            });
        }
        return Result.success(PojoUtils.map(page, CouponActivityGivePageVo.class));
    }

    @ApiOperation(value = "设置指定客户-查询已添加指定客户", httpMethod = "POST")
    @PostMapping("/queryMemberLimitListPage")
    public Result<Page<CouponActivityAutoGiveMemberPageVO>> queryMemberLimitListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody QueryCouponActivityAutoGetMemberLimitFrom from) {
        QueryCouponActivityAutoGetMemberLimitRequest request = PojoUtils.map(from, QueryCouponActivityAutoGetMemberLimitRequest.class);
        Page<CouponActivityAutoGiveMemberPageVO> page = PojoUtils.map(couponActivityAutoGetApi.queryEnterpriseLimitListPage(request), CouponActivityAutoGiveMemberPageVO.class);
        return Result.success(page);
    }

    @ApiOperation(value = "设置推广方-查询已添加推广方企业", httpMethod = "POST")
    @PostMapping("/queryPromotionListPage")
    public Result<Page<CouponActivityAutoGiveMemberPageVO>> queryPromotionListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody QueryCouponActivityAutoGetMemberLimitFrom from) {
        QueryCouponActivityAutoGetMemberLimitRequest request = PojoUtils.map(from, QueryCouponActivityAutoGetMemberLimitRequest.class);
        if(StringUtils.isNotEmpty(from.getProvinceCode())||StringUtils.isNotEmpty(from.getCityCode())||StringUtils.isNotEmpty(from.getRegionCode())){
            QueryEnterpriseListRequest eidRequest = new QueryEnterpriseListRequest();
            eidRequest.setCityCode(from.getCityCode());
            eidRequest.setRegionCode(from.getRegionCode());
            eidRequest.setProvinceCode(from.getProvinceCode());
            eidRequest.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
            eidRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.queryListByArea(eidRequest);
            if(CollectionUtils.isNotEmpty(enterpriseDTOList)){
                List<Long> eids = enterpriseDTOList.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
                request.setEids(eids);
            }
        }
        Page<CouponActivityAutoGiveMemberPageVO> page = PojoUtils.map(couponActivityAutoGetApi.queryPromotionListPage(request), CouponActivityAutoGiveMemberPageVO.class);
        if(CollectionUtils.isNotEmpty(page.getRecords())){
            List<Long> eids = page.getRecords().stream().map(CouponActivityAutoGiveMemberPageVO::getEid).collect(Collectors.toList());
            Map<Long, EnterpriseDTO> mapByIds = enterpriseApi.getMapByIds(eids);
            page.getRecords().forEach(item->{
                EnterpriseDTO enterpriseDTO = mapByIds.get(item.getEid());
                if(ObjectUtil.isNotEmpty(enterpriseDTO)){
                    String address = enterpriseDTO.getProvinceName() + enterpriseDTO.getCityName() + enterpriseDTO.getRegionName();
                    item.setAddress(address);
                    item.setEname(enterpriseDTO.getName());
                }
            });
        }
        return Result.success(page);
    }

    @Log(title = "自动领券活动-添加可领取会员企业", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "设置指定客户-添加指定客户", httpMethod = "POST")
    @PostMapping("/saveEnterpriseLimit")
    public Result<Boolean> saveEnterpriseLimit(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveCouponActivityAutoGetMemberLimitFrom form) {
        SaveCouponActivityAutoGetMemberLimitRequest request = PojoUtils.map(form, SaveCouponActivityAutoGetMemberLimitRequest.class);
        request.setUserId(adminInfo.getCurrentUserId());
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(couponActivityAutoGetApi.saveEnterpriseLimit(request));
    }

    @Log(title = "自动领券活动-添加推广方企业", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "添加推广方企业", httpMethod = "POST")
    @PostMapping("/savePromotionEnterpriseLimit")
    public Result<Boolean> savePromotionEnterpriseLimit(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveCouponActivityAutoGetMemberLimitFrom form) {
        SaveCouponActivityAutoGetMemberLimitRequest request = PojoUtils.map(form, SaveCouponActivityAutoGetMemberLimitRequest.class);
        request.setUserId(adminInfo.getCurrentUserId());
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(couponActivityAutoGetApi.savePromotionEnterpriseLimit(request));
    }

    @Log(title = "自动领券活动-删除可领取会员企业", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation(value = "设置商家-删除推广方企业", httpMethod = "POST")
    @PostMapping("/deletePromotionEnterpriseLimit")
    public Result<Boolean> deletePromotionEnterpriseLimit(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid DeleteCouponActivityAutoGetMemberLimitFrom form) {
        DeleteCouponActivityAutoGetMemberLimitRequest request = PojoUtils.map(form, DeleteCouponActivityAutoGetMemberLimitRequest.class);
        request.setUserId(adminInfo.getCurrentUserId());
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(couponActivityAutoGetApi.deletePromotionEnterpriseLimit(request));
    }

    @Log(title = "自动领券活动-删除可领取会员企业", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation(value = "设置指定客户-删除指定客户", httpMethod = "POST")
    @PostMapping("/deleteEnterpriseLimit")
    public Result<Boolean> deleteEnterpriseLimit(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid DeleteCouponActivityAutoGetMemberLimitFrom form) {
        DeleteCouponActivityAutoGetMemberLimitRequest request = PojoUtils.map(form, DeleteCouponActivityAutoGetMemberLimitRequest.class);
        request.setUserId(adminInfo.getCurrentUserId());
        request.setOpUserId(adminInfo.getCurrentUserId());
        return Result.success(couponActivityAutoGetApi.deleteEnterpriseLimit(request));
    }

    public Map<Long, UserDTO> getUserMapByIds(List<Long> userIds) {
        Map<Long, UserDTO> userMap = new HashMap<>();
        if (CollUtil.isEmpty(userIds)) {
            return userMap;
        }
        List<UserDTO> userList = userApi.listByIds(userIds);
        if (CollUtil.isEmpty(userList)) {
            return userMap;
        }
        return userList.stream().collect(Collectors.toMap(u -> u.getId(), u -> u, (v1, v2) -> v1));
    }

    public List<Long> getGetUserIdList(List<CouponActivityAutoGetPageVo> list) {
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        Set<Long> userIdSet = new HashSet<>();
        list.forEach(p -> {
            if (ObjectUtil.isNotNull(p.getCreateUser())) {
                userIdSet.add(p.getCreateUser());
            }
            if (ObjectUtil.isNotNull(p.getUpdateUser())) {
                userIdSet.add(p.getUpdateUser());
            }
        });
        return new ArrayList<>(userIdSet);
    }

    public String getUserNameById(Map<Long, UserDTO> userMap, Long userId) {
        if (MapUtil.isEmpty(userMap) || ObjectUtil.isNull(userId)) {
            return "";
        }
        UserDTO user = userMap.get(userId);
        if (ObjectUtil.isNotNull(user)) {
            return user.getName();
        }
        return "";
    }

    public Map<Long, EnterpriseDTO> getEnterpriseMapByIds(List<Long> eids) {
        Map<Long, EnterpriseDTO> userMap = new HashMap<>();
        if (CollUtil.isEmpty(eids)) {
            return userMap;
        }
        List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(eids);
        if (CollUtil.isEmpty(enterpriseList)) {
            return userMap;
        }
        return enterpriseList.stream().collect(Collectors.toMap(e -> e.getId(), e -> e, (v1, v2) -> v1));
    }


    @ApiOperation(value = "设置商家-查询供应商", httpMethod = "POST")
    @PostMapping("/queryEnterpriseListPage")
    public Result<Page<CouponActivityEnterprisePageVO>> queryEnterpriseListPage(@CurrentUser CurrentAdminInfo adminInfo,
                                                                                @RequestBody QueryCouponActivityEnterpriseFrom from) {
        QueryCouponActivityEnterpriseRequest request = PojoUtils.map(from, QueryCouponActivityEnterpriseRequest.class);

        Page<CouponActivityEnterprisePageVO> page = request.getPage();
        if (ObjectUtil.isNull(request)) {
            Result.success(page);
        }
        // 根据id、name查询企业信息
        QueryEnterprisePageListRequest enterprisePageListRequest = PojoUtils.map(request, QueryEnterprisePageListRequest.class);
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

        if (StrUtil.isNotBlank(request.getEname())) {
            enterprisePageListRequest.setName(request.getEname());
        }
        Page<EnterpriseDTO> enterpriseDTOPage = enterpriseApi.pageList(enterprisePageListRequest);
        List<CouponActivityEnterprisePageVO> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(enterpriseDTOPage.getRecords())) {
            CouponActivityEnterprisePageVO couponActivityEnterprise;
            int index = 0;
            for (EnterpriseDTO enterprise : enterpriseDTOPage.getRecords()) {
                couponActivityEnterprise = new CouponActivityEnterprisePageVO();
                couponActivityEnterprise.setEid(enterprise.getId());
                couponActivityEnterprise.setEname(enterprise.getName());
                couponActivityEnterprise.setAddress(enterprise.getProvinceName()+enterprise.getCityName()+enterprise.getRegionName());
                list.add(index, couponActivityEnterprise);
                index++;
            }
        }
        page = PojoUtils.map(enterpriseDTOPage, CouponActivityEnterprisePageVO.class);
        page.setRecords(list);
        return Result.success(page);
    }

    @ApiOperation(value = "设置商家-查询供应商-工业类型", httpMethod = "POST")
    @PostMapping("/queryPromotionEnteroriseListPage")
    public Result<Page<CouponActivityEnterprisePageVO>> queryPromotionEnteroriseListPage(@CurrentUser CurrentAdminInfo adminInfo,
                                                                                @RequestBody QueryCouponActivityEnterpriseFrom from) {
        QueryCouponActivityEnterpriseRequest request = PojoUtils.map(from, QueryCouponActivityEnterpriseRequest.class);

        Page<CouponActivityEnterprisePageVO> page = request.getPage();
        if (ObjectUtil.isNull(request)) {
            Result.success(page);
        }
        // 根据id、name查询企业信息
        QueryEnterprisePageListRequest enterprisePageListRequest = PojoUtils.map(request, QueryEnterprisePageListRequest.class);
        enterprisePageListRequest.setCurrent(request.getCurrent());
        enterprisePageListRequest.setSize(request.getSize());
        enterprisePageListRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
        enterprisePageListRequest.setStatus(EnableStatusEnum.ENABLED.getCode());
        // 仅能添加开通B2B、商业类型的
        List<Integer> inTypeList = new ArrayList<>();
        inTypeList.add(EnterpriseTypeEnum.BUSINESS.getCode());
        enterprisePageListRequest.setInTypeList(inTypeList);
        if (ObjectUtil.isNotNull(request.getEid()) && request.getEid() != 0) {
            enterprisePageListRequest.setId(request.getEid());
        }
        if (StrUtil.isNotBlank(request.getEname())) {
            enterprisePageListRequest.setName(request.getEname());
        }

        if (StrUtil.isNotBlank(request.getEname())) {
            enterprisePageListRequest.setName(request.getEname());
        }
        Page<EnterpriseDTO> enterpriseDTOPage = enterpriseApi.pageList(enterprisePageListRequest);
        List<CouponActivityEnterprisePageVO> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(enterpriseDTOPage.getRecords())) {
            CouponActivityEnterprisePageVO couponActivityEnterprise;
            int index = 0;
            for (EnterpriseDTO enterprise : enterpriseDTOPage.getRecords()) {
                couponActivityEnterprise = new CouponActivityEnterprisePageVO();
                couponActivityEnterprise.setEid(enterprise.getId());
                couponActivityEnterprise.setEname(enterprise.getName());
                couponActivityEnterprise.setAddress(enterprise.getProvinceName()+enterprise.getCityName()+enterprise.getRegionName());
                list.add(index, couponActivityEnterprise);
                index++;
            }
        }
        page = PojoUtils.map(enterpriseDTOPage, CouponActivityEnterprisePageVO.class);
        page.setRecords(list);
        return Result.success(page);
    }
}
