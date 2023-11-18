package com.yiling.b2b.admin.coupon.controller;

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

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.admin.coupon.from.CouponActivityEnterpriseLimitQueryFrom;
import com.yiling.b2b.admin.coupon.from.CouponActivityIncreaseFrom;
import com.yiling.b2b.admin.coupon.from.DeleteCouponActivityEnterpriseLimitFrom;
import com.yiling.b2b.admin.coupon.from.DeleteCouponActivityGoodsLimitFrom;
import com.yiling.b2b.admin.coupon.from.QueryCouponActivityEnterpriseFrom;
import com.yiling.b2b.admin.coupon.from.QueryCouponActivityFrom;
import com.yiling.b2b.admin.coupon.from.QueryCouponActivityGivePageFrom;
import com.yiling.b2b.admin.coupon.from.QueryCouponActivityGoodsFrom;
import com.yiling.b2b.admin.coupon.from.QueryCouponActivityUsedPageFrom;
import com.yiling.b2b.admin.coupon.from.SaveCouponActivityBasicFrom;
import com.yiling.b2b.admin.coupon.from.SaveCouponActivityEnterpriseLimitFrom;
import com.yiling.b2b.admin.coupon.from.SaveCouponActivityGoodsLimitFrom;
import com.yiling.b2b.admin.coupon.from.SaveCouponActivityRulesFrom;
import com.yiling.b2b.admin.coupon.vo.CouponActivityDetailVO;
import com.yiling.b2b.admin.coupon.vo.CouponActivityEnterpriseLimitPageVO;
import com.yiling.b2b.admin.coupon.vo.CouponActivityEnterprisePageVO;
import com.yiling.b2b.admin.coupon.vo.CouponActivityGivePageVo;
import com.yiling.b2b.admin.coupon.vo.CouponActivityGoodsPageVO;
import com.yiling.b2b.admin.coupon.vo.CouponActivityPageVo;
import com.yiling.b2b.admin.coupon.vo.CouponActivityUsedPageVo;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.B2bGoodsDTO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.marketing.common.enums.CouponActivitySponsorTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityStatusEnum;
import com.yiling.marketing.common.enums.CouponBearTypeEnum;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.coupon.api.CouponApi;
import com.yiling.marketing.coupon.bo.CouponUseOrderBO;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.coupon.dto.request.QueryCouponPageRequest;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityGiveDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityGoodsDTO;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityIncreaseRequest;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityOperateRequest;
import com.yiling.marketing.couponactivity.dto.request.DeleteCouponActivityEnterpriseLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.DeleteCouponActivityGoodsLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityEnterpriseLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityEnterpriseRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGivePageRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGoodsRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityBasicRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityEnterpriseLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityGoodsLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityRulesRequest;
import com.yiling.marketing.couponactivity.enums.CouponActivityUseDateTypeEnum;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderCouponUseApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  B2B运营后台 优惠券活动
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Api(tags = "优惠券活动接口")
@RestController
@RequestMapping("/couponActivity")
public class CouponActivityController extends BaseController {

    @DubboReference
    CouponActivityApi couponActivityApi;
    @DubboReference
    CouponApi         couponApi;
    @DubboReference
    OrderCouponUseApi orderCouponUseApi;
    @DubboReference
    OrderApi          orderApi;
    @DubboReference
    EnterpriseApi     enterpriseApi;
    @DubboReference
    UserApi           userApi;
    @DubboReference
    GoodsApi          goodsApi;
    @DubboReference
    B2bGoodsApi       b2bgoodsApi;
    @DubboReference
    InventoryApi      inventoryApi;

    @ApiOperation(value = "列表", httpMethod = "POST")
    @PostMapping("/queryListPage")
    public Result<Page<CouponActivityPageVo>> queryListPage(@CurrentUser CurrentStaffInfo staffInfo,
                                                            @RequestBody @Valid QueryCouponActivityFrom form) {
        Page<CouponActivityPageVo> page = new Page();
        QueryCouponActivityRequest request = PojoUtils.map(form, QueryCouponActivityRequest.class);
        // 运营后台默认eid=0
        request.setCurrentEid(staffInfo.getCurrentEid());
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setMemberType(1);
        Page<CouponActivityDTO> dtoPage = couponActivityApi.queryListForMarketing(request);
        // 设置操作标识
        if (ObjectUtil.isNotNull(dtoPage) && CollUtil.isNotEmpty(dtoPage.getRecords())) {
            // 设置已发放数量、已使用数量、优惠规则、设置操作标识
            setCountAndCanOperateFlag(dtoPage, request.getCurrentEid());
            page = PojoUtils.map(dtoPage, CouponActivityPageVo.class);
        }
        return Result.success(page);
    }

    private void setCountAndCanOperateFlag(Page<CouponActivityDTO> page, Long currentEid) {
        // 优惠券活动id
        List<Long> idList = page.getRecords().stream().filter(couponActivity -> ObjectUtil.isNotNull(couponActivity.getId()))
                .map(CouponActivityDTO::getId).collect(Collectors.toList());
        // 创建人、修改人id
        List<Long> userIdList = this.getUserIdList(page.getRecords());
        Map<Long, UserDTO> userMap = this.getUserMapByIds(userIdList);
        // 查询优惠券卡包，已发放
        List<CouponDTO> hasGiveCouponList = couponApi.getHasGiveCountByCouponActivityList(idList);

        long nowTime = System.currentTimeMillis();
        Map<Long, Integer> giveCountMap = new HashMap<>();
        Map<Long, Integer> orderUseCountMap = new HashMap<>();

        if (CollUtil.isNotEmpty(hasGiveCouponList)) {
            giveCountMap = hasGiveCouponList.stream().collect(Collectors.toMap(CouponDTO::getCouponActivityId,CouponDTO::getNum));
            // 查询订单使用优惠券记录
            List<Map<String, Long>> countByCouponActivityId = orderCouponUseApi.getCountByCouponActivityId(idList);
            for (Map<String, Long> orderMap : countByCouponActivityId) {
                orderUseCountMap.put(orderMap.get("couponActivityId").longValue(), orderMap.get("useCount").intValue());
            }
        }

        for (CouponActivityDTO couponActivity : page.getRecords()) {
            Long id = couponActivity.getId();
            Integer giveCountList = giveCountMap.get(id);
            Integer useCount = orderUseCountMap.get(id);
            // 优惠规则
            String couponRules = couponActivityApi.buildCouponRules(couponActivity);
            couponActivity.setCouponRules(couponRules);
            // 总数量
            int totalCount = ObjectUtil.isNull(couponActivity.getTotalCount()) ? 0 : couponActivity.getTotalCount();
            // 已发放数量
            int giveCount = ObjectUtil.isNull(giveCountList) ? 0 : giveCountList;
            couponActivity.setGiveCount(giveCount);
            // 已使用数量
            couponActivity.setUseCount(ObjectUtil.isNull(useCount) ? 0 : useCount.intValue());

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
            if (giveCount > 0) {
                updateFlag = false;
            }
            if(ObjectUtil.equal(CouponActivityUseDateTypeEnum.FIXED.getCode(), couponActivity.getUseDateType())){
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

            if(ObjectUtil.isNotNull(couponActivity.getBeginTime()) && ObjectUtil.isNotNull(couponActivity.getEndTime())){
                // 活动状态
                if (couponActivity.getBeginTime().getTime() > nowTime) {
                    couponActivity.setActivityStatus(1);
                } else if (couponActivity.getBeginTime().getTime() <= nowTime && couponActivity.getEndTime().getTime() > nowTime) {
                    couponActivity.setActivityStatus(2);
                } else if (couponActivity.getEndTime().getTime() <= nowTime) {
                    couponActivity.setActivityStatus(3);
                }
            }
            // 发放后生效规则，按发放/领取XX天过期
            if(ObjectUtil.equal(CouponActivityUseDateTypeEnum.AFTER_GIVE.getCode(), couponActivity.getUseDateType())){
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

    @Log(title = "优惠券活动-新增",businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "列表-保存/编辑基本信息", httpMethod = "POST")
    @PostMapping("/saveOrUpdateBasic")
    public Result<String> saveOrUpdateBasic(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SaveCouponActivityBasicFrom form) {
        SaveCouponActivityBasicRequest request = PojoUtils.map(form, SaveCouponActivityBasicRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setEid(staffInfo.getCurrentEid());
        request.setSponsorType(CouponActivitySponsorTypeEnum.BUSINESS.getCode());
        request.setBear(CouponBearTypeEnum.BUSINESS.getCode());
        Long id = couponActivityApi.saveOrUpdateBasic(request);
        return Result.success(ObjectUtil.isNull(id) ? "" : id.toString());
    }

    @Log(title = "优惠券活动-修改使用规则",businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation(value = "列表-保存/编辑使用规则", httpMethod = "POST")
    @PostMapping("/saveOrUpdateRules")
    public Result<String> saveOrUpdateRules(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SaveCouponActivityRulesFrom form) {
        SaveCouponActivityRulesRequest request = PojoUtils.map(form, SaveCouponActivityRulesRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setEid(staffInfo.getCurrentEid());
        Long id = couponActivityApi.saveOrUpdateRules(request);
        return Result.success(ObjectUtil.isNull(id) ? "" : id.toString());
    }

    @ApiOperation(value = "列表-查看详情", httpMethod = "GET")
    @GetMapping("/getCouponActivity")
    public Result<CouponActivityDetailVO> getCouponActivity(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("id") Long id) {
        QueryCouponActivityDetailRequest request = new QueryCouponActivityDetailRequest();
        request.setId(id);
        request.setEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        CouponActivityDetailVO couponActivityDetail = PojoUtils.map(couponActivityApi.getCouponActivityById(request), CouponActivityDetailVO.class);
        return Result.success(couponActivityDetail);
    }

    @Log(title = "优惠券活动-复制",businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "列表-复制", httpMethod = "GET")
    @GetMapping("/copy")
    public Result<String> copy(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("id") Long id) {
        CouponActivityOperateRequest request = new CouponActivityOperateRequest();
        request.setId(id);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setEid(staffInfo.getCurrentEid());
        CouponActivityDetailVO couponActivityDetail = PojoUtils.map(couponActivityApi.copy(request), CouponActivityDetailVO.class);
        Long newId = couponActivityDetail.getId();
        return Result.success(ObjectUtil.isNull(newId) ? "" : newId.toString());
    }

    @Log(title = "优惠券活动-停用",businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation(value = "列表-停用", httpMethod = "GET")
    @GetMapping("/stop")
    public Result<Boolean> stop(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("id") Long id) {
        CouponActivityOperateRequest request = new CouponActivityOperateRequest();
        request.setId(id);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setEid(staffInfo.getCurrentEid());
        return Result.success(couponActivityApi.stop(request));
    }

    @Log(title = "优惠券活动-作废",businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation(value = "列表-作废", httpMethod = "GET")
    @GetMapping("/scrap")
    public Result<Boolean> scrap(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("id") Long id) {
        CouponActivityOperateRequest request = new CouponActivityOperateRequest();
        request.setId(id);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setEid(staffInfo.getCurrentEid());
        return Result.success(couponActivityApi.scrap(request));
    }

    @Log(title = "优惠券活动-增券",businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "列表-增券", httpMethod = "POST")
    @PostMapping("/increase")
    public Result<Boolean> increase(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody CouponActivityIncreaseFrom from) {
        CouponActivityIncreaseRequest request = PojoUtils.map(from, CouponActivityIncreaseRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setEid(staffInfo.getCurrentEid());
        return Result.success(couponActivityApi.increase(request));
    }

    @ApiOperation(value = "设置商家-查询供应商", httpMethod = "POST")
    @PostMapping("/queryEnterpriseListPage")
    public Result<Page<CouponActivityEnterprisePageVO>> queryEnterpriseListPage(@CurrentUser CurrentStaffInfo staffInfo,
                                                                                @RequestBody QueryCouponActivityEnterpriseFrom from) {
        QueryCouponActivityEnterpriseRequest request = PojoUtils.map(from, QueryCouponActivityEnterpriseRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
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
        enterprisePageListRequest.setMallFlag(1);
        List<Integer> notInTypeList = new ArrayList<>();
        notInTypeList.add(EnterpriseTypeEnum.BUSINESS.getCode());
        enterprisePageListRequest.setNotInTypeList(notInTypeList);
        if (ObjectUtil.isNotNull(request.getEid()) && request.getEid() != 0) {
            enterprisePageListRequest.setId(request.getEid());
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
                list.add(index, couponActivityEnterprise);
                index++;
            }
        }
        page = PojoUtils.map(enterpriseDTOPage, CouponActivityEnterprisePageVO.class);
        page.setRecords(list);
        return Result.success(page);
    }

    @ApiOperation(value = "设置商家-查询已添加供应商", httpMethod = "POST")
    @PostMapping("/queryEnterpriseLimitListPage")
    public Result<Page<CouponActivityEnterpriseLimitPageVO>> queryEnterpriseLimitListPage(@CurrentUser CurrentStaffInfo staffInfo,
                                                                                          @RequestBody CouponActivityEnterpriseLimitQueryFrom from) {
        QueryCouponActivityEnterpriseLimitRequest request = PojoUtils.map(from, QueryCouponActivityEnterpriseLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        Page<CouponActivityEnterpriseLimitPageVO> page = PojoUtils.map(couponActivityApi.queryEnterpriseLimitListPage(request),
            CouponActivityEnterpriseLimitPageVO.class);
        return Result.success(page);
    }

    @Log(title = "优惠券活动-添加可用供应商",businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "设置商家-添加供应商", httpMethod = "POST")
    @PostMapping("/saveEnterpriseLimit")
    public Result<Boolean> saveEnterpriseLimit(@CurrentUser CurrentStaffInfo staffInfo,
                                               @RequestBody @Valid SaveCouponActivityEnterpriseLimitFrom form) {
        SaveCouponActivityEnterpriseLimitRequest request = PojoUtils.map(form, SaveCouponActivityEnterpriseLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(couponActivityApi.saveEnterpriseLimit(request));
    }

    @Log(title = "优惠券活动-删除可用供应商",businessType = BusinessTypeEnum.DELETE)
    @ApiOperation(value = "设置商家-删除供应商", httpMethod = "POST")
    @PostMapping("/deleteEnterpriseLimit")
    public Result<Boolean> deleteEnterpriseLimit(@CurrentUser CurrentStaffInfo staffInfo,
                                                 @RequestBody @Valid DeleteCouponActivityEnterpriseLimitFrom form) {
        DeleteCouponActivityEnterpriseLimitRequest request = PojoUtils.map(form, DeleteCouponActivityEnterpriseLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setCurrentEid(staffInfo.getCurrentEid());
        request.setPlatformType(2);
        return Result.success(couponActivityApi.deleteEnterpriseLimit(request));
    }

    @ApiOperation(value = "设置商品-查询商品", httpMethod = "POST")
    @PostMapping("/queryGoodsListPage")
    public Result<Page<CouponActivityGoodsPageVO>> queryGoodsListPage(@CurrentUser CurrentStaffInfo staffInfo,
                                                                      @RequestBody QueryCouponActivityGoodsFrom from) {
        QueryCouponActivityGoodsRequest request = PojoUtils.map(from, QueryCouponActivityGoodsRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setEid(staffInfo.getCurrentEid());
        Page<CouponActivityGoodsPageVO> page = PojoUtils.map(this.queryGoodsListPage(request), CouponActivityGoodsPageVO.class);
        return Result.success(page);
    }

    public Page<CouponActivityGoodsDTO> queryGoodsListPage(QueryCouponActivityGoodsRequest request) {
        if (ObjectUtil.isNull(request)) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        Page<CouponActivityGoodsDTO> page = request.getPage();

        // 查询商品信息
        QueryGoodsPageListRequest goodsRequest = new QueryGoodsPageListRequest();
        goodsRequest.setCurrent(request.getCurrent());
        goodsRequest.setSize(request.getSize());
        // isAvailableQty不传就表示不显示库存
        goodsRequest.setGoodsStatus(request.getGoodsStatus());
        if (!Integer.valueOf(0).equals(request.getYilingGoodsFlag())) {
            goodsRequest.setIncludeEids(enterpriseApi.listSubEids(Constants.YILING_EID));
        }
        goodsRequest.setYilingGoodsFlag(request.getYilingGoodsFlag());
        // 商家后台只能添加自己的商品（目前没有父子级店铺）
        List<Long> eidList = new ArrayList<>();
        eidList.add(request.getEid());
        goodsRequest.setEidList(eidList);
        if (ObjectUtil.isNotNull(request.getGoodsId())) {
            goodsRequest.setGoodsId(request.getGoodsId());
        }
        if (StrUtil.isNotBlank(request.getGoodsName())) {
            goodsRequest.setName(request.getGoodsName());
        }
        //goodsRequest.setGoodsStatus(GoodsStatusEnum.UP_SHELF.getCode());
        Page<GoodsListItemBO> goodsPage = b2bgoodsApi.queryB2bGoodsPageList(goodsRequest);
        if (ObjectUtil.isNull(goodsPage) || CollUtil.isEmpty(goodsPage.getRecords())) {
            return page;
        }

        List<CouponActivityGoodsDTO> list = new ArrayList<>();
        // 查询库存
        List<Long> goodsIdList = goodsPage.getRecords().stream().map(GoodsListItemBO::getId).collect(Collectors.toList());
        Map<Long, Long> inventoryMap = inventoryApi.getAvailableQtyByGoodsIds(goodsIdList);
        CouponActivityGoodsDTO couponActivityGoods;
        int index = 0;
        Map<Long, Long> goodsMap = goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(goodsIdList, enterpriseApi.listSubEids(Constants.YILING_EID));
        for (GoodsListItemBO goods : goodsPage.getRecords()) {
            couponActivityGoods = new CouponActivityGoodsDTO();
            // todo coupon B2B查询商品接口 goodsApi.queryB2bGoodsPageList，（因搜索条件可能只有商品名称）需提供查es接口
            couponActivityGoods.setGoodsStatus(goods.getGoodsStatus());
            couponActivityGoods.setEid(goods.getEid());
            couponActivityGoods.setYilingGoodsFlag(2);
            if(goodsMap.get(goods.getId())!=null&&goodsMap.get(goods.getId())>0){
                couponActivityGoods.setYilingGoodsFlag(1);
            }
            couponActivityGoods.setEname(goods.getEname());
            couponActivityGoods.setGoodsId(goods.getId());
            couponActivityGoods.setGoodsName(goods.getName());
            couponActivityGoods.setGoodsType(goods.getGoodsType());
            couponActivityGoods.setSellSpecifications(goods.getSellSpecifications());
            couponActivityGoods.setPrice(goods.getPrice());
            couponActivityGoods.setSellUnit(goods.getSellUnit());
            // 商品库存
            Long inventory = inventoryMap.get(goods.getId());
            if (ObjectUtil.isNull(inventory) || inventory.longValue() < 0) {
                inventory = 0L;
            }
            couponActivityGoods.setGoodsInventory(inventory);

            list.add(index, couponActivityGoods);
            index++;
        }
        page = PojoUtils.map(goodsPage, CouponActivityGoodsDTO.class);
        page.setRecords(list);
        return page;
    }

    @ApiOperation(value = "设置商品-查询已添加商品", httpMethod = "POST")
    @PostMapping("/queryGoodsLimitListPage")
    public Result<Page<CouponActivityGoodsPageVO>> queryGoodsLimitListPage(@CurrentUser CurrentStaffInfo staffInfo,
                                                                           @RequestBody QueryCouponActivityGoodsFrom from) {
        QueryCouponActivityGoodsRequest request = PojoUtils.map(from, QueryCouponActivityGoodsRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        Page<CouponActivityGoodsDTO> goodsDTOPage = couponActivityApi.queryGoodsLimitListPage(request);
        buildGoodsLimitPage(goodsDTOPage);
        Page<CouponActivityGoodsPageVO> page = PojoUtils.map(goodsDTOPage, CouponActivityGoodsPageVO.class);
        return Result.success(page);
    }

    public void buildGoodsLimitPage(Page<CouponActivityGoodsDTO> page) {
        List<Long> goodsIdList = page.getRecords().stream().map(CouponActivityGoodsDTO::getGoodsId).collect(Collectors.toList());

        // 根据goodsIds批量查询 商品基础信息
        Map<Long, GoodsInfoDTO> goodsInfoMap = new HashMap<>();
        List<GoodsInfoDTO> goodsInfoList = b2bgoodsApi.batchQueryInfo(goodsIdList);
        if (CollUtil.isNotEmpty(goodsInfoList)) {
            goodsInfoMap = goodsInfoList.stream().collect(Collectors.toMap(g -> g.getId(), g -> g, (v1, v2) -> v1));
        }
        // 商品id集合查询商品状态、价格
        Map<Long, B2bGoodsDTO> goodsStatusPriceMap = new HashMap<>();
        List<B2bGoodsDTO> goodsStatusPriceList = b2bgoodsApi.getB2bGoodsListByGoodsIds(goodsIdList);
        if (CollUtil.isNotEmpty(goodsStatusPriceList)) {
            goodsStatusPriceMap = goodsStatusPriceList.stream().collect(Collectors.toMap(g -> g.getGoodsId(), g -> g, (v1, v2) -> v1));
        }
        // 统计商品库存
        Map<Long, Long> inventoryMap = inventoryApi.getAvailableQtyByGoodsIds(goodsIdList);

        // 获取商品基本信息、状态、价格、统计商品库存
        for (CouponActivityGoodsDTO entity : page.getRecords()) {
            GoodsInfoDTO goodsInfo = goodsInfoMap.get(entity.getGoodsId());
            B2bGoodsDTO goodsStatusPrice = goodsStatusPriceMap.get(entity.getGoodsId());
            Long inventory = 0L;
            if (ObjectUtil.isNotNull(goodsInfo)) {
                entity.setGoodsName(goodsInfo.getName());
                entity.setGoodsType(goodsInfo.getGoodsType());
                entity.setSellSpecifications(goodsInfo.getSellSpecifications());
                entity.setSellUnit(goodsInfo.getSellUnit());
                entity.setPrice(goodsInfo.getPrice());
                // 商品库存
                inventory = inventoryMap.get(entity.getGoodsId());
                if (ObjectUtil.isNull(inventory) || inventory.longValue() < 0) {
                    inventory = 0L;
                }
            }
            entity.setGoodsInventory(inventory);
            if (ObjectUtil.isNotNull(goodsStatusPrice)) {
                entity.setGoodsStatus(goodsStatusPrice.getGoodsStatus());
            }
        }
    }

    @Log(title = "优惠券活动-添加可用商品",businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "设置商品-添加商品", httpMethod = "POST")
    @PostMapping("/saveGoodsLimit")
    public Result<Boolean> saveGoodsLimit(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SaveCouponActivityGoodsLimitFrom form) {
        SaveCouponActivityGoodsLimitRequest request = PojoUtils.map(form, SaveCouponActivityGoodsLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setBusinessType(2);
        return Result.success(couponActivityApi.saveGoodsLimit(request));
    }

    @Log(title = "优惠券活动-删除可用商品",businessType = BusinessTypeEnum.DELETE)
    @ApiOperation(value = "设置商品-删除商品", httpMethod = "POST")
    @PostMapping("/deleteGoodsLimit")
    public Result<Boolean> deleteGoodsLimit(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid DeleteCouponActivityGoodsLimitFrom form) {
        DeleteCouponActivityGoodsLimitRequest request = PojoUtils.map(form, DeleteCouponActivityGoodsLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setCurrentEid(staffInfo.getCurrentEid());
        request.setPlatformType(2);
        return Result.success(couponActivityApi.deleteGoodsLimit(request));
    }

    @ApiOperation(value = "列表-已发放优惠券查看", httpMethod = "POST")
    @PostMapping("/queryGiveListPage")
    public Result<Page<CouponActivityGivePageVo>> queryGiveListPage(@CurrentUser CurrentStaffInfo staffInfo,
                                                                    @RequestBody @Valid QueryCouponActivityGivePageFrom form) {
        QueryCouponActivityGivePageRequest request = PojoUtils.map(form, QueryCouponActivityGivePageRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        request.setGetType(CouponGetTypeEnum.GIVE.getCode());
        Page<CouponActivityGiveDTO> page = couponActivityApi.queryGiveListPage(request);
        if(ObjectUtil.isNotNull(page) && CollUtil.isNotEmpty(page.getRecords())){
            List<Long> userIdList = page.getRecords().stream().map(CouponActivityGiveDTO::getGetUserId).distinct().collect(Collectors.toList());
            Map<Long, UserDTO> userMap = this.getUserMapByIds(userIdList);
            page.getRecords().forEach(p -> {
                String getUserName = this.getUserNameById(userMap, p.getGetUserId());
                p.setGetUserName(getUserName);
            });
        }
        return Result.success(PojoUtils.map(page, CouponActivityGivePageVo.class));
    }

    @ApiOperation(value = "列表-已使用优惠券查看", httpMethod = "POST")
    @PostMapping("/queryUseListPage")
    public Result<Page<CouponActivityUsedPageVo>> queryUseListPage(@CurrentUser CurrentStaffInfo staffInfo,
                                                                   @RequestBody @Valid QueryCouponActivityUsedPageFrom form) {
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
        QueryCouponPageRequest couponUseRequest = PojoUtils.map(form, QueryCouponPageRequest.class);
        Page<CouponUseOrderBO> couponUsePage = couponApi.getOrderCountUsePageByActivityId(couponUseRequest);
        if (ObjectUtil.isNull(couponUsePage) || CollUtil.isEmpty(couponUsePage.getRecords())) {
            return Result.success(page);
        }
        // 获券企业名称
        List<Long> eidList = couponUsePage.getRecords().stream().map(CouponUseOrderBO::getEid).distinct().collect(Collectors.toList());
        Map<Long, String> enameMap = getEnameMap(eidList);
        // 组装优惠券信息
        page = PojoUtils.map(couponUsePage, CouponActivityUsedPageVo.class);
        for (CouponActivityUsedPageVo couponUsed : page.getRecords()) {// couponUsed.setId(couponUsed.getCouponId());
            couponUsed.setCouponRules(couponRules);
            couponUsed.setCouponName(name);
            couponUsed.setEname(enameMap.get(couponUsed.getEid()));
            couponUsed.setEffectiveTime(effectiveTime);
        }

        // 查询订单信息
        List<Long> orderIds = page.getRecords().stream().map(CouponActivityUsedPageVo::getOrderId).collect(Collectors.toList());
        List<OrderDTO> orderList = orderApi.listByIds(orderIds);
        if (CollUtil.isEmpty(orderList)) {
            return Result.success(page);
        }
        Map<Long, OrderDTO> orderMap = orderList.stream().collect(Collectors.toMap(OrderDTO::getId, Function.identity()));
        // 组装订单信息
        for (CouponActivityUsedPageVo couponUsed : page.getRecords()) {
            OrderDTO order = orderMap.get(couponUsed.getOrderId());
            if (ObjectUtil.isNotNull(order)) {
                couponUsed.setOrderId(order.getId());
                couponUsed.setOrderNo(order.getOrderNo());
                couponUsed.setOrderAmount(order.getTotalAmount());
            }
        }
        return Result.success(page);
    }

    private Map<Long, String> getEnameMap(List<Long> eidList) {
        Map<Long, String> enameMap = new HashMap<>();
        if(CollUtil.isEmpty(eidList)){
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
        if(ObjectUtil.equal(CouponActivityUseDateTypeEnum.FIXED.getCode(), useDateType)){
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
        if(CollUtil.isEmpty(list)){
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
