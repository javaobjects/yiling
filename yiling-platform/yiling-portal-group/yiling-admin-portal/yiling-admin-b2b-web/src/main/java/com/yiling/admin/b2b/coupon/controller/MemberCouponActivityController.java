package com.yiling.admin.b2b.coupon.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
import com.yiling.admin.b2b.coupon.from.CouponActivityIncreaseFrom;
import com.yiling.admin.b2b.coupon.from.QueryCouponActivityFrom;
import com.yiling.admin.b2b.coupon.from.QueryCouponActivityGivePageFrom;
import com.yiling.admin.b2b.coupon.from.QueryCouponActivityGoodsFrom;
import com.yiling.admin.b2b.coupon.from.QueryCouponActivityUsedPageFrom;
import com.yiling.admin.b2b.coupon.from.QueryMemberCouponActivityFrom;
import com.yiling.admin.b2b.coupon.from.SaveCouponActivityRulesFrom;
import com.yiling.admin.b2b.coupon.from.SaveMemberCouponActivityBasicFrom;
import com.yiling.admin.b2b.coupon.from.SaveMemberCouponActivityFrom;
import com.yiling.admin.b2b.coupon.vo.CouponActivityCreatorVO;
import com.yiling.admin.b2b.coupon.vo.CouponActivityDetailVO;
import com.yiling.admin.b2b.coupon.vo.CouponActivityEnterpriseVO;
import com.yiling.admin.b2b.coupon.vo.CouponActivityGivePageVo;
import com.yiling.admin.b2b.coupon.vo.CouponActivityGoodsGiftPageVo;
import com.yiling.admin.b2b.coupon.vo.CouponActivityMemberPageVo;
import com.yiling.admin.b2b.coupon.vo.CouponActivityPageVo;
import com.yiling.admin.b2b.coupon.vo.CouponActivityResidueCountVO;
import com.yiling.admin.b2b.coupon.vo.CouponActivityUsedPageVo;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.marketing.common.enums.CouponActivityStatusEnum;
import com.yiling.marketing.common.enums.CouponActivityTypeEnum;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.coupon.api.CouponApi;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityGiveDTO;
import com.yiling.marketing.couponactivity.dto.CouponMemberActivityLimitDTO;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityIncreaseRequest;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityOperateRequest;
import com.yiling.marketing.couponactivity.dto.request.DeleteCouponActivityGoodsLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGivePageRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGoodsRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponMemberActivityPageRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityBasicRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityRulesRequest;
import com.yiling.marketing.couponactivity.enums.CouponActivityUseDateTypeEnum;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderCouponUseApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.api.MemberBuyStageApi;
import com.yiling.user.member.api.MemberOrderApi;
import com.yiling.user.member.dto.MemberBuyStageDTO;
import com.yiling.user.member.dto.MemberOrderCouponDTO;
import com.yiling.user.member.dto.MemberSimpleDTO;
import com.yiling.user.member.dto.request.QueryMemberBuyStagePageRequest;
import com.yiling.user.member.dto.request.QueryMemberCouponPageRequest;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.request.QueryStaffListRequest;
import com.yiling.user.system.enums.UserStatusEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * B2B运营后台 会员优惠券活动
 * </p>
 *
 * @author shixing.sun
 * @date 2022-08-10
 */
@Api(tags = "会员优惠券活动接口")
@RestController
@RequestMapping("/memberCouponActivity")
public class MemberCouponActivityController extends BaseController {

    @DubboReference
    CouponActivityApi couponActivityApi;
    @DubboReference
    CouponApi couponApi;
    @DubboReference
    OrderCouponUseApi orderCouponUseApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    StaffApi staffApi;
    @DubboReference
    B2bGoodsApi b2bgoodsApi;
    @DubboReference
    InventoryApi inventoryApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    MemberApi memberApi;
    @DubboReference
    MemberBuyStageApi memberBuyStageApi;
    @DubboReference
    MemberOrderApi memberOrderApi;


    @Log(title = "会员优惠券活动-新增", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "会员优惠券活动-新增", httpMethod = "POST")
    @PostMapping("/saveOrUpdateBasic")
    public Result<String> saveOrUpdateBasic(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveMemberCouponActivityBasicFrom form) {
        SaveCouponActivityBasicRequest request = PojoUtils.map(form, SaveCouponActivityBasicRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setEid(0L);
        request.setMemberType(2);
        Long id = couponActivityApi.saveOrUpdateBasicForMember(request);
        return Result.success(ObjectUtil.isNull(id) ? "" : id.toString());
    }

    @Log(title = "会员优惠券活动-保存会员规格", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "保存会员规格-时长维度", httpMethod = "POST")
    @PostMapping("/saveMemberCouponRelation")
    public Result<Boolean> saveMemberCouponRelation(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveMemberCouponActivityFrom form) {
        SaveCouponActivityBasicRequest request = PojoUtils.map(form, SaveCouponActivityBasicRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setEid(0L);
        return Result.success(couponActivityApi.saveMemberCouponRelation(request));
    }

    @Log(title = "会员优惠券活动-关联会员规格", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "删除会员规格-时长维度", httpMethod = "POST")
    @PostMapping("/deleteMemberCouponRelation")
    public Result<Boolean> deleteMemberCouponRelation(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveMemberCouponActivityFrom form) {
        SaveCouponActivityBasicRequest request = PojoUtils.map(form, SaveCouponActivityBasicRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setEid(0L);
        return Result.success(couponActivityApi.deleteMemberCouponRelation(request));
    }

    @Log(title = "查询会员规格列表", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "查询会员规格列表-会员维度", httpMethod = "POST")
    @PostMapping("/queryMemberCouponRelationList")
    public Result<Page<CouponActivityMemberPageVo>> queryMemberCouponRelationList(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody QueryMemberCouponActivityFrom form) {
        Page<CouponActivityMemberPageVo> page = new Page();
        List<MemberSimpleDTO> memberSimpleDTOS = memberApi.queryAllList();
        List<CouponActivityMemberPageVo> couponActivityMemberPageVos = new ArrayList<>();
        memberSimpleDTOS.forEach(item -> {
            CouponActivityMemberPageVo couponActivityMember = new CouponActivityMemberPageVo();
            couponActivityMember.setId(item.getId());
            couponActivityMember.setMemberName(item.getName());
            couponActivityMemberPageVos.add(couponActivityMember);
        });
        page.setRecords(couponActivityMemberPageVos);
        return Result.success(page);
    }

    @Log(title = "查询会员规格列表", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "查询会员规格列表-时长维度", httpMethod = "POST")
    @PostMapping("/queryMemberCouponSpecsList")
    public Result<Page<CouponActivityMemberPageVo>> queryMemberCouponSpecsList(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody QueryMemberCouponActivityFrom form) {
        Page<CouponActivityMemberPageVo> page = new Page();
        List<Long> idsByName = new ArrayList<>();
        if (StringUtils.isNotEmpty(form.getName())) {
            idsByName = memberApi.listIdsByName(form.getName());
            if (CollectionUtils.isEmpty(idsByName)) {
                return Result.success(page);
            }
        }
        QueryMemberBuyStagePageRequest request = new QueryMemberBuyStagePageRequest();
        request.setCurrent(form.getCurrent());
        request.setSize(form.getSize());
        request.setMemberIdList(idsByName);
        Page<MemberBuyStageDTO> memberBuyStageDTOPage = memberBuyStageApi.queryMemberBuyStagePage(request);
        Page<CouponActivityMemberPageVo> result = PojoUtils.map(memberBuyStageDTOPage, CouponActivityMemberPageVo.class);
        return Result.success(result);
    }

    @Log(title = "查询已经保存的会员规格", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "查询已经保存的会员规格-时长维度", httpMethod = "POST")
    @PostMapping("/queryMemberCouponRelationLimit")
    public Result<Page<CouponActivityMemberPageVo>> queryMemberCouponRelationLimit(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryMemberCouponActivityFrom form) {
        Page<CouponActivityMemberPageVo> page = new Page();
        List<Long> idsByName = new ArrayList<>();
        if (StringUtils.isNotEmpty(form.getName())) {
            List<MemberBuyStageDTO> stageByMemberName = memberBuyStageApi.getStageByMemberName(form.getName());
            if (CollectionUtils.isEmpty(stageByMemberName)) {
                return Result.success(page);
            }
            idsByName = stageByMemberName.stream().map(MemberBuyStageDTO::getId).collect(Collectors.toList());
        }
        QueryCouponMemberActivityPageRequest request = new QueryCouponMemberActivityPageRequest();
        request.setId(form.getId());
        request.setMemberIds(idsByName);
        Page<CouponMemberActivityLimitDTO> memberBuyStageDTOPage = couponActivityApi.PageActivityMeberLimit(request);
        page = PojoUtils.map(memberBuyStageDTOPage, CouponActivityMemberPageVo.class);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            List<MemberBuyStageDTO> stageByMemberName = memberBuyStageApi.getStageByMemberName(null);
            if (CollectionUtils.isNotEmpty(stageByMemberName)) {
                Map<Long, MemberBuyStageDTO> entMap = stageByMemberName.stream().collect(Collectors.toMap(MemberBuyStageDTO::getId, e -> e));
                page.getRecords().forEach(item -> {
                    MemberBuyStageDTO memberBuyStageDTO = entMap.get(item.getMemberId());
                    if (ObjectUtil.isNotNull(memberBuyStageDTO)) {
                        item.setMemberName(memberBuyStageDTO.getMemberName());
                        item.setName(memberBuyStageDTO.getName());
                        item.setPrice(memberBuyStageDTO.getPrice());
                        item.setValidTime(memberBuyStageDTO.getValidTime());
                    }
                });
            }
        }
        return Result.success(page);
    }

    @ApiOperation(value = "列表", httpMethod = "POST")
    @PostMapping("/queryListPage")
    public Result<Page<CouponActivityPageVo>> queryListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryCouponActivityFrom form) {
        Page<CouponActivityPageVo> page = new Page();
        QueryCouponActivityRequest request = PojoUtils.map(form, QueryCouponActivityRequest.class);
        List<Long> userIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(form.getCreateUserName())) {
            QueryStaffListRequest queryStaffListRequest = new QueryStaffListRequest();
            queryStaffListRequest.setNameLike(form.getCreateUserName());
            queryStaffListRequest.setStatusEq(UserStatusEnum.ENABLED.getCode());
            List<Staff> staffList = staffApi.list(queryStaffListRequest);
            if (CollectionUtils.isNotEmpty(staffList)) {
                userIds = staffList.stream().map(Staff::getId).distinct().collect(Collectors.toList());
                request.setCreatUserIds(userIds);
            }else {
                return Result.success(page);
            }
        }
        // 运营后台默认eid=0
        request.setCurrentEid(0L);
        request.setMemberType(2);
        // 运营后台查询所有
        Page<CouponActivityDTO> dtoPage = couponActivityApi.queryListForMarketing(request);
        // 设置操作标识
        if (ObjectUtil.isNotNull(dtoPage) && CollUtil.isNotEmpty(dtoPage.getRecords())) {
            // 设置已发放数量、已使用数量、优惠规则、设置操作标识
            setCountAndCanOperateFlag(dtoPage, request.getCurrentEid());
            page = PojoUtils.map(dtoPage, CouponActivityPageVo.class);
        }
        return Result.success(page);
    }

    @ApiOperation(value = "用户名称搜索用户列表", httpMethod = "POST")
    @PostMapping("/queryCreatorByName")
    public Result<CollectionObject<CouponActivityCreatorVO>> queryCreatorByNmae(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody QueryCouponActivityFrom form) {
        QueryStaffListRequest queryStaffListRequest = new QueryStaffListRequest();
        queryStaffListRequest.setNameLike(form.getName());
        queryStaffListRequest.setStatusEq(UserStatusEnum.ENABLED.getCode());
        List<Staff> staffList = staffApi.list(queryStaffListRequest);
        List<CouponActivityCreatorVO> couponActivityCreatorVOS = new ArrayList<>();
        staffList.forEach(item -> {
            CouponActivityCreatorVO couponActivityCreatorVO = new CouponActivityCreatorVO();
            couponActivityCreatorVO.setId(item.getId());
            couponActivityCreatorVO.setCreaterName(item.getName());
            couponActivityCreatorVOS.add(couponActivityCreatorVO);
        });
        CollectionObject collectionObject = new CollectionObject(couponActivityCreatorVOS);
        return Result.success(collectionObject);
    }

    @ApiOperation(value = "企业名称搜索企业列表", httpMethod = "POST")
    @PostMapping("/queryActivityEidByName")
    public Result<CollectionObject<CouponActivityEnterpriseVO>> queryActivityEidByName(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody QueryCouponActivityFrom form) {
        QueryEnterpriseByNameRequest byNameRequest = new QueryEnterpriseByNameRequest();
        byNameRequest.setName(form.getName());
        List<Integer> inTypeList = new ArrayList<>();
        inTypeList.add(EnterpriseTypeEnum.BUSINESS.getCode());
        byNameRequest.setTypeList(inTypeList);
        List<EnterpriseDTO> enterpriseListByName = enterpriseApi.getEnterpriseListByName(byNameRequest);
        CollectionObject collectionObject = new CollectionObject(PojoUtils.map(enterpriseListByName, CouponActivityEnterpriseVO.class));
        return Result.success(collectionObject);
    }


    private void setCountAndCanOperateFlag(Page<CouponActivityDTO> page, Long currentEid) {
        // 优惠券活动id
        List<Long> idList = page.getRecords().stream().filter(couponActivity -> ObjectUtil.isNotNull(couponActivity.getId())).map(CouponActivityDTO::getId).collect(Collectors.toList());
        // 创建人、修改人id
        List<Long> userIdList = this.getUserIdList(page.getRecords());
        Map<Long, UserDTO> userMap = this.getUserMapByIds(userIdList);
        // 查询优惠券卡包，已发放
        List<CouponDTO> hasGiveCouponList = couponApi.getHasGiveCountByCouponActivityList(idList);

        long nowTime = System.currentTimeMillis();
        Map<Long, Integer> giveCountMap = new HashMap<>();
        Map<Long, Long> orderUseCountMap = new HashMap<>();

        if (CollUtil.isNotEmpty(hasGiveCouponList)) {
            giveCountMap = hasGiveCouponList.stream().collect(Collectors.toMap(CouponDTO::getCouponActivityId,CouponDTO::getNum));
            // 查询会员优惠券使用优惠券记录
            orderUseCountMap = memberOrderApi.getMemberCouponUseTimes(idList);
        }

        for (CouponActivityDTO couponActivity : page.getRecords()) {
            Long id = couponActivity.getId();
            Integer giveCountList = giveCountMap.get(id);
            Integer useCount = orderUseCountMap.get(id) == null ? 0 : orderUseCountMap.get(id).intValue();
            // 优惠规则
            Integer type = couponActivity.getType();
            BigDecimal discountValue = couponActivity.getDiscountValue();
            StringBuilder sb = new StringBuilder();
            if (ObjectUtil.equal(CouponActivityTypeEnum.REDUCE.getCode(), type)) {
                // 满减
                couponActivity.setFaceValue(discountValue.toString());
                sb.append("减").append(discountValue).append("元");
            } else if (ObjectUtil.equal(CouponActivityTypeEnum.DISCOUNT.getCode(), type)) {
                // 满折
                couponActivity.setFaceValue("-");
                sb.append("打").append(discountValue).append("%折");
            }
            couponActivity.setCouponRules(sb.toString());
            // 总数量
            int totalCount = ObjectUtil.isNull(couponActivity.getTotalCount()) ? 0 : couponActivity.getTotalCount();
            // 已发放数量
            int giveCount = ObjectUtil.isNull(giveCountList) ? 0 : giveCountList;
            couponActivity.setGiveCount(giveCount);
            // 已使用数量
            couponActivity.setUseCount(ObjectUtil.isNull(useCount) ? 0 : useCount.intValue());
            couponActivity.setSurplusCount(totalCount - giveCount);
            // 操作标识
            Integer status = couponActivity.getStatus();
            boolean updateFlag = true;
            boolean stopFlag = true;
            boolean scrapFlag = true;
            boolean giveFlag = true;
            boolean enterpriseFlag = true;
            boolean copyFlag = true;
            boolean increaseFlag = true;

            // 可操作标识
            // 可停用标识
            if (!ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), status)) {
                updateFlag = false;
                stopFlag = false;
                giveFlag = false;
                increaseFlag = false;
                copyFlag = false;
            }
            // 可作废标识
            if (ObjectUtil.equal(CouponActivityStatusEnum.SCRAP.getCode(), status)) {
                scrapFlag = false;
            }
            // 可修改标识
            //            if (giveCount > 0) {
            //                updateFlag = false;
            //            }
            if (ObjectUtil.equal(CouponActivityUseDateTypeEnum.FIXED.getCode(), couponActivity.getUseDateType())) {
                if (couponActivity.getEndTime().getTime() <= nowTime) {
                    updateFlag = false;
                    stopFlag = false;
                    scrapFlag = false;
                    giveFlag = false;
                    increaseFlag = false;
                }
            }
            // 可发放标识
            if (giveCount > 0 && giveCount >= totalCount) {
                giveFlag = false;
            }

            // 不是所属企业，只能查看（运营后台没有企业信息，默认eid=0）
            if (!ObjectUtil.equal(currentEid, couponActivity.getEid())) {
                updateFlag = false;
                stopFlag = false;
                scrapFlag = false;
                giveFlag = false;
                enterpriseFlag = false;
                copyFlag = false;
                increaseFlag = false;
            }

            couponActivity.setEnterpriseFlag(enterpriseFlag);
            couponActivity.setUpdateFlag(updateFlag);
            couponActivity.setStopFlag(stopFlag);
            couponActivity.setScrapFlag(scrapFlag);
            couponActivity.setGiveFlag(giveFlag);
            couponActivity.setCopyFlag(copyFlag);
            couponActivity.setIncreaseFlag(increaseFlag);

            if (ObjectUtil.isNotNull(couponActivity.getBeginTime()) && ObjectUtil.isNotNull(couponActivity.getEndTime())) {
                // 活动状态 todo 停用、作废的  活动状态=已结束
                if (couponActivity.getBeginTime().getTime() > nowTime) {
                    couponActivity.setActivityStatus(1);
                } else if (couponActivity.getBeginTime().getTime() <= nowTime && couponActivity.getEndTime().getTime() > nowTime) {
                    couponActivity.setActivityStatus(2);
                } else if (couponActivity.getEndTime().getTime() <= nowTime) {
                    couponActivity.setActivityStatus(3);
                }
            }
            // 发放后生效规则，按发放/领取XX天过期
            if (ObjectUtil.equal(CouponActivityUseDateTypeEnum.AFTER_GIVE.getCode(), couponActivity.getUseDateType())) {
                StringBuilder builder = new StringBuilder().append("按发放/领取").append(couponActivity.getExpiryDays()).append("天过期");
                couponActivity.setGiveOutEffectiveRules(builder.toString());
            }
            // 操作人姓名
            String createUserName = this.getUserNameById(userMap, couponActivity.getCreateUser());
            String updateUserName = this.getUserNameById(userMap, couponActivity.getUpdateUser());
            couponActivity.setCreateUserName(createUserName);
            couponActivity.setUpdateUserName(updateUserName);
        }
    }


    @Log(title = "优惠券活动-修改使用规则", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation(value = "列表-保存/编辑使用规则", httpMethod = "POST")
    @PostMapping("/saveOrUpdateRules")
    public Result<String> saveOrUpdateRules(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveCouponActivityRulesFrom form) {
        SaveCouponActivityRulesRequest request = PojoUtils.map(form, SaveCouponActivityRulesRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setEid(0L);
        Long id = couponActivityApi.saveOrUpdateRules(request);
        return Result.success(ObjectUtil.isNull(id) ? "" : id.toString());
    }

    @ApiOperation(value = "列表-查看详情", httpMethod = "GET")
    @GetMapping("/getCouponActivity")
    public Result<CouponActivityDetailVO> getCouponActivity(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("id") Long id) {
        QueryCouponActivityDetailRequest request = new QueryCouponActivityDetailRequest();
        request.setId(id);
        request.setEid(0L);
        CouponActivityDetailVO couponActivityDetail = PojoUtils.map(couponActivityApi.getCouponActivityById(request), CouponActivityDetailVO.class);
        return Result.success(couponActivityDetail);
    }

    @Log(title = "优惠券活动-复制", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "列表-复制", httpMethod = "GET")
    @GetMapping("/copy")
    public Result<String> copy(@CurrentUser CurrentAdminInfo adminInfo, @NotNull @RequestParam("id") Long id) {
        CouponActivityOperateRequest request = new CouponActivityOperateRequest();
        request.setId(id);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setEid(0L);
        CouponActivityDetailVO couponActivityDetail = PojoUtils.map(couponActivityApi.copy(request), CouponActivityDetailVO.class);
        Long newId = couponActivityDetail.getId();
        return Result.success(ObjectUtil.isNull(newId) ? "" : newId.toString());
    }

    @Log(title = "优惠券活动-停用", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation(value = "列表-停用", httpMethod = "GET")
    @GetMapping("/stop")
    public Result<Boolean> stop(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("id") Long id) {
        CouponActivityOperateRequest request = new CouponActivityOperateRequest();
        request.setId(id);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setEid(0L);
        return Result.success(couponActivityApi.stop(request));
    }

    @Log(title = "优惠券活动-作废", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation(value = "列表-作废", httpMethod = "GET")
    @GetMapping("/scrap")
    public Result<Boolean> scrap(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("id") Long id) {
        CouponActivityOperateRequest request = new CouponActivityOperateRequest();
        request.setId(id);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setEid(0L);
        return Result.success(couponActivityApi.scrap(request));
    }

    @Log(title = "优惠券活动-增券", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation(value = "列表-增券", httpMethod = "POST")
    @PostMapping("/increase")
    public Result<Boolean> increase(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody CouponActivityIncreaseFrom from) {
        CouponActivityIncreaseRequest request = PojoUtils.map(from, CouponActivityIncreaseRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setEid(0L);
        return Result.success(couponActivityApi.increase(request));
    }


    @Log(title = "优惠券活动-删除商品搜索结果", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation(value = "设置商品-删除商品搜索结果", httpMethod = "POST")
    @PostMapping("/deleteGoodsLimitSearchResult")
    public Result<Boolean> deleteGoodsLimitSearchResult(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody QueryCouponActivityGoodsFrom from) {
        QueryCouponActivityGoodsRequest request = PojoUtils.map(from, QueryCouponActivityGoodsRequest.class);
        List<Long> ids = couponActivityApi.queryGoodsLimitList(request);
        if (CollectionUtils.isEmpty(ids)) {
            return Result.success(false);
        }
        DeleteCouponActivityGoodsLimitRequest activityGoodsLimitRequest = new DeleteCouponActivityGoodsLimitRequest();
        activityGoodsLimitRequest.setIds(ids);
        activityGoodsLimitRequest.setCouponActivityId(request.getCouponActivityId());
        activityGoodsLimitRequest.setOpUserId(adminInfo.getCurrentUserId());
        activityGoodsLimitRequest.setCurrentEid(0L);
        activityGoodsLimitRequest.setPlatformType(1);
        return Result.success(couponActivityApi.deleteGoodsLimit(activityGoodsLimitRequest));
    }

    @ApiOperation(value = "列表-已发放优惠券查看", httpMethod = "POST")
    @PostMapping("/queryGiveListPage")
    public Result<Page<CouponActivityGivePageVo>> queryGiveListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryCouponActivityGivePageFrom form) {
        QueryCouponActivityGivePageRequest request = PojoUtils.map(form, QueryCouponActivityGivePageRequest.class);
        request.setCurrent(form.getCurrent());
        request.setSize(form.getSize());
        //        request.setEid(0L);
        request.setGetType(CouponGetTypeEnum.GIVE.getCode());
        Page<CouponActivityGiveDTO> page = couponActivityApi.queryGiveListPage(request);
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

    @ApiOperation(value = "列表-已使用会员优惠券查看", httpMethod = "POST")
    @PostMapping("/queryUseListPage")
    public Result<Page<CouponActivityUsedPageVo>> queryUseListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryCouponActivityUsedPageFrom form) {
        Page<CouponActivityUsedPageVo> page = form.getPage();

        // 查询优惠券活动信息
        QueryCouponActivityDetailRequest request = new QueryCouponActivityDetailRequest();
        request.setId(form.getCouponActivityId());
        CouponActivityDetailDTO couponActivity = couponActivityApi.getCouponActivityById(request);
        if (ObjectUtil.isNull(couponActivity)) {
            return Result.success(page);
        }
        // 优惠规则
        String name = couponActivity.getName();
        CouponActivityDTO dto = PojoUtils.map(couponActivity, CouponActivityDTO.class);
        String couponRules = couponActivityApi.buildCouponRules(dto);
        String effectiveTime = buildEffectiveTime(couponActivity);

        // 查询已使用优惠券
        QueryMemberCouponPageRequest queryMemberCouponPageRequest = new QueryMemberCouponPageRequest();
        queryMemberCouponPageRequest.setCouponActivityId(form.getCouponActivityId());
        Page<MemberOrderCouponDTO> memberOrderCouponDTOPage = memberOrderApi.queryMemberOrderPageByCoupon(queryMemberCouponPageRequest);
        if (ObjectUtil.isNull(memberOrderCouponDTOPage) || CollUtil.isEmpty(memberOrderCouponDTOPage.getRecords())) {
            return Result.success(page);
        }
        // 获券企业名称
        List<Long> eidList = memberOrderCouponDTOPage.getRecords().stream().map(MemberOrderCouponDTO::getEid).distinct().collect(Collectors.toList());
        Map<Long, String> enameMap = getEnameMap(eidList);
        // 组装优惠券信息
        page = PojoUtils.map(memberOrderCouponDTOPage, CouponActivityUsedPageVo.class);
        List<Long> couponIds = memberOrderCouponDTOPage.getRecords().stream().map(MemberOrderCouponDTO::getCouponId).collect(Collectors.toList());
        List<CouponDTO> couponById = couponActivityApi.getCouponById(couponIds);
        Map<Long, CouponDTO> couponDTOMap = couponById.stream().collect(Collectors.toMap(CouponDTO::getId, Function.identity()));
        for (CouponActivityUsedPageVo couponUsed : page.getRecords()) {
            Long couponId = couponUsed.getCouponId();
            CouponDTO couponDTO = couponDTOMap.get(couponId);
            if(ObjectUtil.isNotNull(couponDTO)){
                couponUsed.setGetType(couponDTO.getGetType());
                couponUsed.setGetTypeStr(CouponGetTypeEnum.getByCode(couponDTO.getGetType()).getName());
                couponUsed.setUseTime(couponDTO.getUseTime());
                couponUsed.setGetTime(couponDTO.getGetTime());
            }
            couponUsed.setCouponActivityId(form.getCouponActivityId());
            couponUsed.setCouponRules(couponRules);
            couponUsed.setCouponName(name);
            couponUsed.setEname(enameMap.get(couponUsed.getEid()));
            couponUsed.setEffectiveTime(effectiveTime);
        }
        return Result.success(page);
    }

    private Map<Long, String> getEnameMap(List<Long> eidList) {
        Map<Long, String> enameMap = new HashMap<>();
        if (CollUtil.isEmpty(eidList)) {
            return enameMap;
        }
        List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(eidList);
        if (CollUtil.isNotEmpty(enterpriseList)) {
            enameMap = enterpriseList.stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getName(), (v1, v2) -> v1));
        }
        return enameMap;
    }

    public String buildEffectiveTime(CouponActivityDetailDTO couponActivity) {
        String effectiveTime = "";
        Integer useDateType = couponActivity.getUseDateType();
        if (ObjectUtil.equal(CouponActivityUseDateTypeEnum.FIXED.getCode(), useDateType)) {
            Date beginTime = couponActivity.getBeginTime();
            Date endTime = couponActivity.getEndTime();
            String begin = DateUtil.format(beginTime, "yyyy-MM-dd HH:mm:ss");
            String end = DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss");
            effectiveTime = begin.concat(" - ").concat(end);
        } else {
            Integer expiryDays = couponActivity.getExpiryDays();
            effectiveTime = "按发放/领取".concat(expiryDays.toString()).concat("天过期");
        }
        return effectiveTime;
    }

    @ApiOperation(value = "查询优惠券剩余数量、可用供应商类型", httpMethod = "GET")
    @GetMapping("/getResidueCount")
    public Result<CouponActivityResidueCountVO> getResidueCount(@CurrentUser CurrentAdminInfo adminInfo, @NotNull @RequestParam("couponActivityId") Long couponActivityId) {
        CouponActivityResidueCountVO result = PojoUtils.map(couponActivityApi.getResidueCount(couponActivityId), CouponActivityResidueCountVO.class);
        return Result.success(result);
    }

    @ApiOperation(value = "赠品库-优惠券列表", httpMethod = "POST")
    @PostMapping("/queryListPageForGoodsGift")
    public Result<Page<CouponActivityGoodsGiftPageVo>> queryListPageForGoodsGift(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryCouponActivityFrom form) {
        Page<CouponActivityGoodsGiftPageVo> page = form.getPage();
        QueryCouponActivityRequest request = PojoUtils.map(form, QueryCouponActivityRequest.class);
        request.setCurrentEid(0L);
        request.setCurrent(form.getCurrent());
        request.setSize(form.getSize());
        Page<CouponActivityDTO> dtoPage = couponActivityApi.getPageForGoodsGift(request);
        if (ObjectUtil.isNotNull(dtoPage) && CollUtil.isNotEmpty(dtoPage.getRecords())) {
            page = PojoUtils.map(dtoPage, CouponActivityGoodsGiftPageVo.class);
            page.getRecords().forEach(p -> {
                Integer type = p.getType();
                BigDecimal discountValue = p.getDiscountValue();
                StringBuilder couponRulesBuilder = new StringBuilder();
                if (ObjectUtil.equal(CouponActivityTypeEnum.REDUCE.getCode(), type)) {
                    couponRulesBuilder.append(discountValue).append("元");
                } else if (ObjectUtil.equal(CouponActivityTypeEnum.DISCOUNT.getCode(), type)) {
                    BigDecimal discountMax = p.getDiscountMax();
                    couponRulesBuilder.append(discountValue).append("%折");
                    if (ObjectUtil.isNotNull(discountMax) && discountMax.compareTo(BigDecimal.ZERO) > 0) {
                        couponRulesBuilder.append("，最高减").append(discountMax).append("元");
                    }
                }
                p.setCouponRules(couponRulesBuilder.toString());
            });
        }
        return Result.success(page);
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

    public List<Long> getUserIdList(List<CouponActivityDTO> list) {
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

}
