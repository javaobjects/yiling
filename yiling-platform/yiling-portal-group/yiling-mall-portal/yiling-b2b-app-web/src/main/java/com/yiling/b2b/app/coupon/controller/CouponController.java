package com.yiling.b2b.app.coupon.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.app.coupon.form.OrderUseCouponBudgetForm;
import com.yiling.b2b.app.coupon.form.QueryCouponGoodsSearchFrom;
import com.yiling.b2b.app.coupon.form.QueryMyCouponPageFrom;
import com.yiling.b2b.app.coupon.vo.CouponActivityCanAndOwnVO;
import com.yiling.b2b.app.coupon.vo.GetCouponActivityResultVO;
import com.yiling.b2b.app.coupon.vo.MyCouponCanUseGoodsListVO;
import com.yiling.b2b.app.coupon.vo.MyCouponPageVO;
import com.yiling.b2b.app.coupon.vo.MyMemberCouponResultVO;
import com.yiling.b2b.app.coupon.vo.OrderUseCouponBudgetVO;
import com.yiling.b2b.app.goods.utils.GoodsAssemblyUtils;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.marketing.common.enums.CouponPlatformTypeEnum;
import com.yiling.marketing.coupon.api.CouponApi;
import com.yiling.marketing.coupon.dto.request.QueryCouponListPageRequest;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanAndOwnDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEidOrGoodsIdDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityForMemberResultDTO;
import com.yiling.marketing.couponactivity.dto.GetCouponActivityResultDTO;
import com.yiling.marketing.couponactivity.dto.OrderUseCouponBudgetDTO;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityReceiveRequest;
import com.yiling.marketing.couponactivity.dto.request.OrderUseCouponBudgetRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityCanReceiveRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGoodsRequest;
import com.yiling.search.goods.api.EsGoodsSearchApi;
import com.yiling.search.goods.dto.request.EsActivityGoodsSearchRequest;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryCanBuyEidRequest;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.request.CurrentMemberForMarketingDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2021/11/9
 */
@Slf4j
@RestController
@Api(tags = "商品优惠券信息相关")
@RequestMapping("/coupon")
public class CouponController {

    @DubboReference
    CouponActivityApi  couponActivityApi;
    @DubboReference
    CouponApi          couponApi;
    @DubboReference
    UserApi            userApi;
    @DubboReference
    EnterpriseApi      enterpriseApi;
    @DubboReference
    MemberApi          memberApi;
    @DubboReference
    EsGoodsSearchApi   esGoodsSearchApi;
    @DubboReference
    CustomerApi customerApi;

    @Autowired
    GoodsAssemblyUtils goodsAssemblyUtils;


    @Log(title = "领取优惠券",businessType = BusinessTypeEnum.INSERT)
    @UserAccessAuthentication
    @ApiOperation(value = "立即领取", httpMethod = "GET")
    @GetMapping(path = "/get")
    public Result<GetCouponActivityResultVO> get(@CurrentUser CurrentStaffInfo staffInfo, @NotNull @RequestParam Long couponActivityId) {
        log.info("1getCoupon"+staffInfo.getCurrentEid()+"couponActivityId"+couponActivityId);
        Long currentEid = staffInfo.getCurrentEid();
        Long currentUserId = staffInfo.getCurrentUserId();
        CouponActivityReceiveRequest request = new CouponActivityReceiveRequest();
        request.setCouponActivityId(couponActivityId);
        request.setUserId(currentUserId);
        request.setEid(currentEid);
        request.setPlatformType(CouponPlatformTypeEnum.B2B.getCode());
        // 企业信息
        EnterpriseDTO enterprise = Optional.ofNullable(enterpriseApi.getById(staffInfo.getCurrentEid()))
                .orElseThrow(()->new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));
        request.setEtype(enterprise.getType());
        // 用户信息
        UserDTO user = Optional.ofNullable(userApi.getById(staffInfo.getCurrentUserId())).orElse(new UserDTO());
        request.setUserName(user.getUsername());
        // 会员信息
        CurrentMemberForMarketingDTO member = Optional.ofNullable(memberApi.getCurrentMemberForMarketing(staffInfo.getCurrentEid())).orElse(new CurrentMemberForMarketingDTO());
        request.setCurrentMember(member.getCurrentMember());
        GetCouponActivityResultDTO getCouponActivityResultDTO = couponActivityApi.receiveByCouponActivityIdForApp(request);
        getCouponActivityResultDTO.setId(couponActivityId);
        return Result.success(PojoUtils.map(getCouponActivityResultDTO, GetCouponActivityResultVO.class));
    }

    @UserAccessAuthentication
    @ApiOperation(value = "商品详情可领取、已领取优惠券活动列表列表", httpMethod = "GET")
    @GetMapping(path = "/getCouponActivityCanAndOwnList")
    public Result<CouponActivityCanAndOwnVO> getCouponActivityCanAndOwnList(@CurrentUser CurrentStaffInfo staffInfo, @NotNull @RequestParam @ApiParam(required = true, name = "eid", value = "店铺企业id") Long eid,@RequestParam(required = false) @ApiParam(required = false, name = "goodsId", value = "商品id") Long goodsId) {
        QueryCouponActivityCanReceiveRequest request = new QueryCouponActivityCanReceiveRequest();
        request.setCurrentEid(staffInfo.getCurrentEid());
        request.setEid(eid);
        request.setPlatformType(CouponPlatformTypeEnum.B2B.getCode());
        if(goodsId == null||goodsId.intValue()==0){
            request.setGoodsId(null);
        }else {
            request.setGoodsId(goodsId);
        }
        CouponActivityCanAndOwnDTO canAndOwnList = couponActivityApi.getCanAndOwnListByEid(request);
        CouponActivityCanAndOwnVO vo = PojoUtils.map(canAndOwnList, CouponActivityCanAndOwnVO.class);
        return Result.success(vo);
    }

//    @UserAccessAuthentication
//    @ApiOperation(value = "进货单-可使用优惠券列表", httpMethod = "GET")
//    @GetMapping(path = "/getCouponCanUseList")
//    public Result<CouponActivityCanUseVO> getCouponCanUseList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryCouponCanUseListForm form) {
//        QueryCouponCanUseListRequest request = PojoUtils.map(form, QueryCouponCanUseListRequest.class);
//        request.setOpUserId(staffInfo.getCurrentUserId());
//        request.setCurrentEid(staffInfo.getCurrentEid());
//        CouponActivityCanUseDTO couponActivityCanUseDTO = couponActivityApi.getCouponCanUseList(request);
//        if (couponActivityCanUseDTO == null) {
//            return Result.success();
//        }
//        List<CouponActivityCanUseDetailDTO> couponActivityCanUseDetailDTOList = Lists.newArrayList();
//        Optional.ofNullable(couponActivityCanUseDTO.getPlatformList()).ifPresent(e -> couponActivityCanUseDetailDTOList.addAll(e));
//        Optional.ofNullable(couponActivityCanUseDTO.getBusinessList()).ifPresent(e -> couponActivityCanUseDetailDTOList.addAll(e));
//
//        List<CouponActivityCanUseDetailVO> detailList = PojoUtils.map(couponActivityCanUseDetailDTOList, CouponActivityCanUseDetailVO.class);
//        CouponActivityCanUseVO vo = new CouponActivityCanUseVO();
//        vo.setCouponDetailList(detailList);
//        return Result.success(vo);
//    }

    @UserAccessAuthentication
    @ApiOperation(value = "去结算-优惠券分摊计算", httpMethod = "GET")
    @GetMapping(path = "/orderUseCouponShareAmountBudget")
    public Result<OrderUseCouponBudgetVO> orderUseCouponShareAmountBudget(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody OrderUseCouponBudgetForm form) {
        OrderUseCouponBudgetRequest request = PojoUtils.map(form, OrderUseCouponBudgetRequest.class);
        OrderUseCouponBudgetDTO dto = couponActivityApi.orderUseCouponShareAmountBudget(request);
        return Result.success(PojoUtils.map(dto, OrderUseCouponBudgetVO.class));
    }

    @ApiOperation(value = "我的优惠券列表", httpMethod = "POST")
    @PostMapping("/myCouponPage")
    @Log(title = "我的-优惠券列表",businessType = BusinessTypeEnum.OTHER)
    public Result<Page<MyCouponPageVO>> queryEnterpriseLimitListPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryMyCouponPageFrom form) {
        QueryCouponListPageRequest request = PojoUtils.map(form, QueryCouponListPageRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setCurrentUserId(staffInfo.getCurrentUserId());
        request.setPlatformType(CouponPlatformTypeEnum.B2B.getCode());
        Page<MyCouponPageVO> page = PojoUtils.map(couponApi.getCouponListPageByEid(request),MyCouponPageVO.class);

        return Result.success(page);
    }

    @ApiOperation(value = "可使用会员优惠券列表", httpMethod = "POST")
    @PostMapping("/myAvailableMemberCouponList")
    @Log(title = "获取当前用户可使用的会员优惠券列表",businessType = BusinessTypeEnum.OTHER)
    public Result<MyMemberCouponResultVO> myAvailableMemberCouponList(@CurrentUser CurrentStaffInfo staffInfo, @NotNull @RequestParam @ApiParam(required = true, name = "memberId", value = "会员规格id") Long memberId) {
        QueryCouponListPageRequest request = new QueryCouponListPageRequest();
        request.setEid(staffInfo.getCurrentEid());
        request.setPlatformType(CouponPlatformTypeEnum.B2B.getCode());
        request.setMemberId(memberId);
        CouponActivityForMemberResultDTO couponActivityForMemberResultDTO = couponApi.myAvailableMemberCouponList(request);
        MyMemberCouponResultVO resultVO = PojoUtils.map(couponActivityForMemberResultDTO, MyMemberCouponResultVO.class);
        return Result.success(resultVO);
    }

    @UserAccessAuthentication
    @ApiOperation(value = "我的-优惠券去使用商品列表", httpMethod = "POST")
    @PostMapping(path = "/activityGoodsSearch")
    public Result<MyCouponCanUseGoodsListVO> activityGoodsSearch(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryCouponGoodsSearchFrom form) {
        log.info("activityGoodsSearch, form -> {}", JSON.toJSONString(form));
        log.info("activityGoodsSearch, staffInfo -> {}", JSON.toJSONString(staffInfo));
        // 企业ID
        Long buyerEid = staffInfo != null ? staffInfo.getCurrentEid() : 0L;
        EsActivityGoodsSearchRequest request = PojoUtils.map(form, EsActivityGoodsSearchRequest.class);
        request.setMallFlag(1);
        request.setMallStatus(GoodsStatusEnum.UP_SHELF.getCode());
        QueryCouponActivityGoodsRequest couponActivityGoodsRequest = PojoUtils.map(form, QueryCouponActivityGoodsRequest.class);
        couponActivityGoodsRequest.setGoodsName(form.getKey());
        CouponActivityEidOrGoodsIdDTO couponActivityEidOrGoodsIdDTO = couponApi.geGoodsListPageByCouponId(form.getCouponId(),buyerEid,couponActivityGoodsRequest);
        log.info("activityGoodsSearch, couponActivityEidOrGoodsIdDTO -> {}", JSON.toJSONString(couponActivityEidOrGoodsIdDTO));
        if (couponActivityEidOrGoodsIdDTO == null|| StringUtils.isNotEmpty(couponActivityEidOrGoodsIdDTO.getNotAvailableMessage())) {
            MyCouponCanUseGoodsListVO myCouponCanUseGoodsListVO = new MyCouponCanUseGoodsListVO();
            myCouponCanUseGoodsListVO.setUnAvailableMessage(couponActivityEidOrGoodsIdDTO.getNotAvailableMessage());
            return Result.success(myCouponCanUseGoodsListVO);
        }
        if(CollectionUtil.isEmpty(couponActivityEidOrGoodsIdDTO.getGoodsIdList())&&CollectionUtil.isEmpty(couponActivityEidOrGoodsIdDTO.getEidList())&&!couponActivityEidOrGoodsIdDTO.getAllEidFlag()){
            MyCouponCanUseGoodsListVO myCouponCanUseGoodsListVO = new MyCouponCanUseGoodsListVO();
            return Result.success(myCouponCanUseGoodsListVO);
        }
        request.setAllEidFlag(couponActivityEidOrGoodsIdDTO.getAllEidFlag());
        request.setEidList(couponActivityEidOrGoodsIdDTO.getEidList());
        request.setGoodsIdList(couponActivityEidOrGoodsIdDTO.getGoodsIdList());
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        if(CollectionUtil.isNotEmpty(couponActivityEidOrGoodsIdDTO.getGoodsIdList())){
            request.setCurrent(1);
        }
        if(buyerEid > 0){
            QueryCanBuyEidRequest enterpriseRequest = new QueryCanBuyEidRequest();
            enterpriseRequest.setCustomerEid(buyerEid);
            enterpriseRequest.setLine(EnterpriseCustomerLineEnum.B2B.getCode());
            enterpriseRequest.setLimit(50);
            List<Long> sortEid = customerApi.getEidListByCustomerEid(enterpriseRequest);
            request.setSortEid(sortEid);
        }
        EsAggregationDTO data = esGoodsSearchApi.searchActivityGoods(request);
        log.info("activityGoodsSearch, data -> {}", JSON.toJSONString(data));
        List<GoodsInfoDTO> goodsAggDTOList = data.getData();

        if (CollUtil.isNotEmpty(goodsAggDTOList)) {
            // 商品ID集合
            List<Long> goodsIds = goodsAggDTOList.stream().map(e -> e.getId()).distinct().collect(Collectors.toList());
            data.setData(goodsAssemblyUtils.assembly(goodsIds, buyerEid));
        }
        MyCouponCanUseGoodsListVO myCouponCanUseGoodsListVO = PojoUtils.map(data, MyCouponCanUseGoodsListVO.class);
        return Result.success(myCouponCanUseGoodsListVO);
    }

}
