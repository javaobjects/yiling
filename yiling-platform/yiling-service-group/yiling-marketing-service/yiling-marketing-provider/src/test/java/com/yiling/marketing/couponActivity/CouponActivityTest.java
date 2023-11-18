package com.yiling.marketing.couponActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.BaseTest;
import com.yiling.marketing.common.enums.CouponActivityGiveRecordStatusEnum;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.common.enums.CouponPlatformTypeEnum;
import com.yiling.marketing.common.enums.CouponUsedStatusEnum;
import com.yiling.marketing.common.util.CouponUtil;
import com.yiling.marketing.coupon.api.CouponApi;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.coupon.dto.request.QueryCouponCanUseListRequest;
import com.yiling.marketing.coupon.dto.request.QueryCouponListPageRequest;
import com.yiling.marketing.coupon.dto.request.QueryHasGiveCouponAutoRequest;
import com.yiling.marketing.coupon.dto.request.UseMemberCouponRequest;
import com.yiling.marketing.coupon.entity.CouponDO;
import com.yiling.marketing.coupon.service.CouponService;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.bo.CouponActivityRulesBO;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanAndOwnDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanUseDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanUseDetailDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityForMemberResultDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityHasGetDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityMyCouponDTO;
import com.yiling.marketing.couponactivity.dto.OrderUseCouponBudgetDTO;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityExistFlagDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityExistFlagRequest;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityReceiveRequest;
import com.yiling.marketing.couponactivity.dto.request.OrderUseCouponBudgetRequest;
import com.yiling.marketing.couponactivity.dto.request.OrderUseCouponRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityCanReceiveRequest;
import com.yiling.marketing.couponactivity.event.UpdateCouponGiveNumEvent;
import com.yiling.marketing.couponactivity.service.CouponActivityForEnterpriseService;
import com.yiling.marketing.couponactivity.service.CouponActivityForGoodsService;
import com.yiling.marketing.couponactivity.service.CouponActivityForPurchaseOrderService;
import com.yiling.marketing.couponactivity.service.CouponActivityService;
import com.yiling.marketing.couponactivityautogive.dto.request.UpdateAutoGiveCountRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.UpdateAutoGiveRecordStatusRequest;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveRecordDO;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveRecordService;
import com.yiling.marketing.couponactivityautogive.service.CouponActivityAutoGiveService;
import com.yiling.marketing.promotion.enums.PromotionPlatformEnum;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2021/11/17
 */
@Slf4j
public class CouponActivityTest extends BaseTest {

    @Autowired
    CouponActivityForPurchaseOrderService       couponActivityForPurchaseOrderService;
    @DubboReference
    CouponApi                                   couponApi;
    @Autowired
    private CouponActivityAutoGiveRecordService autoGiveRecordService;
    @Autowired
    private CouponActivityAutoGiveService       autoGiveService;
    @Autowired
    private CouponActivityForGoodsService       forGoodsService;
    @Autowired
    private CouponService                       couponService;
    @Autowired
    private CouponActivityForEnterpriseService  forEnterpriseService;

    @Autowired
    private CouponActivityService couponActivityService;

    @DubboReference
    CouponActivityApi couponActivityApi;

    @Autowired
    CouponActivityForGoodsService couponActivityForGoodsService;

    @Test
    public void test1() {
        String json = "{\"currentEid\":316,\"goodsDetailList\":[{\"eid\":22,\"goodsAmount\":150.000000,\"goodsId\":2154,\"opTime\":1639550120322,\"paymentMethod\":4,\"shopCouponId\":858,\"shopDiscountAmount\":20.00}],\"opTime\":1639550120335,\"platform\":2}";
        QueryCouponCanUseListRequest request = JSON.parseObject(json, QueryCouponCanUseListRequest.class);
        CouponActivityCanUseDTO result = couponActivityForPurchaseOrderService.getCouponCanUseList(request);
        System.out.println(">>>>> result:" + JSONObject.toJSONString(result));
    }

    @Test
    public void test2() {
        QueryCouponListPageRequest request = new QueryCouponListPageRequest();
        request.setEid(57L);
        request.setCurrentUserId(0L);
        request.setUsedStatusType(1);
        Page<CouponActivityMyCouponDTO> page = couponApi.getCouponListPageByEid(request);
        System.out.println(">>>>> result:" + JSONObject.toJSONString(page));
    }

    @Test
    public void saveAutoGiveRecordWithWaitStatusTest() {
        // 待保存发放记录
        List<CouponActivityAutoGiveRecordDO> newAutoGiveRecordList = new ArrayList<>();
        CouponActivityAutoGiveRecordDO newRecord1 = new CouponActivityAutoGiveRecordDO();
        newRecord1.setCouponActivityId(1L);
        newRecord1.setCouponActivityName("测试待发放1");
        newRecord1.setEid(1L);
        newRecord1.setEname("asdfasd111");
        newRecord1.setGiveNum(1);
        newRecord1.setGetType(CouponGetTypeEnum.GIVE.getCode());
        newRecord1.setStatus(CouponActivityGiveRecordStatusEnum.WAIT.getCode());
        newRecord1.setCreateUser(1L);
        newRecord1.setCreateTime(new Date());
        newRecord1.setOwnEid(0L);
        newRecord1.setOwnEname("0");
        newRecord1.setBatchNumber(CouponUtil.getMillisecondTime());

        CouponActivityAutoGiveRecordDO newRecord2 = new CouponActivityAutoGiveRecordDO();
        newRecord2.setCouponActivityId(2L);
        newRecord2.setCouponActivityName("测试待发放2");
        newRecord2.setEid(2L);
        newRecord2.setEname("asdfasd222");
        newRecord2.setGiveNum(1);
        newRecord2.setGetType(CouponGetTypeEnum.GIVE.getCode());
        newRecord2.setStatus(CouponActivityGiveRecordStatusEnum.WAIT.getCode());
        newRecord2.setCreateUser(1L);
        newRecord2.setCreateTime(new Date());
        newRecord2.setOwnEid(0L);
        newRecord2.setOwnEname("0");
        newRecord2.setBatchNumber(CouponUtil.getMillisecondTime());
        newAutoGiveRecordList.add(newRecord1);
        newAutoGiveRecordList.add(newRecord2);

        // 保存发放记录，状态待发放
        autoGiveRecordService.saveAutoGiveRecordWithWaitStatus(newAutoGiveRecordList);
        System.out.println(newAutoGiveRecordList);

        // 更新发放记录，已发放
        List<UpdateAutoGiveRecordStatusRequest> updateRecordStatusList = new ArrayList<>();
        UpdateAutoGiveRecordStatusRequest recordStatusRequest;
        for (CouponActivityAutoGiveRecordDO record : newAutoGiveRecordList) {
            recordStatusRequest = new UpdateAutoGiveRecordStatusRequest();
            recordStatusRequest.setId(record.getId());
            recordStatusRequest.setStatus(CouponActivityGiveRecordStatusEnum.SUCCESS.getCode());
            updateRecordStatusList.add(recordStatusRequest);
        }
        autoGiveRecordService.updateRecordStatus(updateRecordStatusList);

    }

    @Test
    public void updateGiveCountByIdListTest() {
        UpdateAutoGiveCountRequest request = new UpdateAutoGiveCountRequest();
        request.setIds(Arrays.asList(2L));
        request.setOpUserId(1L);
        request.setOpTime(new Date());
        autoGiveService.updateGiveCountByIdList(request);
    }

    @Test
    public void receiveByCouponActivityIdTest() {
        String json = "{\"platformType\":2,\"couponActivityId\":124,\"currentMember\":1,\"eid\":74,\"ename\":\"以岭药业呀\",\"etype\":7,\"opTime\":1649660383000,\"userId\":1,\"userName\":\"测试001\"}";
        CouponActivityReceiveRequest request = JSON.parseObject(json, CouponActivityReceiveRequest.class);
        forGoodsService.receiveByCouponActivityId(request);
    }

    @Test
    public void returnTest() {
        LambdaQueryWrapper<CouponDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CouponDO::getId, Arrays.asList(1L,2L));
        CouponDO entity = new CouponDO();
        entity.setUsedStatus(CouponUsedStatusEnum.NOT_USED.getCode());
        entity.setOpTime(new Date());
        couponService.update(entity, queryWrapper);
    }

    @Test
    public void orderUseCouponShareAmountBudgetTest() {
        String json = "{\"currentEid\":214,\"goodsSkuDetailList\":[{\"couponId\":904,\"eid\":26,\"goodsId\":2570,\"goodsSkuAmount\":26.640000,\"goodsSkuId\":576,\"payMethod\":1}],\"opTime\":1639980717832,\"platform\":2}";
        OrderUseCouponBudgetRequest request = JSON.parseObject(json, OrderUseCouponBudgetRequest.class);
        OrderUseCouponBudgetDTO dto = couponActivityForPurchaseOrderService.orderUseCouponShareAmountBudget(request);
        System.out.println(">>>>> 优惠券分摊结果："+ JSONObject.toJSONString(dto));
    }

    @Test
    public void orderUseCouponTest() {
        String json = "{\"couponIdList\":[],\"eid\":214,\"opTime\":1638516622314,\"opUserId\":421,\"platformCouponId\":332}";
        OrderUseCouponRequest request = JSON.parseObject(json, OrderUseCouponRequest.class);
        couponActivityForPurchaseOrderService.orderUseCoupon(request);
    }

    @Test
    public void getCouponActivityListByEidTest() {
        QueryCouponActivityCanReceiveRequest request = new QueryCouponActivityCanReceiveRequest();
        request.setEid(212L);
        request.setLimit(10);
        request.setCurrentEid(220L);
        request.setPlatformType(CouponPlatformTypeEnum.B2B.getCode());
        List<CouponActivityHasGetDTO> list = forEnterpriseService.getCouponActivityListByEid(request);
        System.out.println(">>>>> list:"+list);
    }

    @Test
    public void getCanAndOwnListByEidTest() {
        Long currentEid = 74L;
        Long eid = 40L;
        QueryCouponActivityCanReceiveRequest request = new QueryCouponActivityCanReceiveRequest();
        request.setCurrentEid(currentEid);
        request.setEid(eid);
        request.setPlatformType(PromotionPlatformEnum.SALES_ASSIST.getType());
        CouponActivityCanAndOwnDTO result = forGoodsService.getCanAndOwnListByEid(request);
        System.out.println(">>>>> result:"+ JSON.toJSONString(result));
    }

    @Test
    public void query(){
        List<CouponActivityCanUseDetailDTO> list = couponActivityService.queryByCouponActivityIdList(Arrays.asList(299L));
        System.out.println(list);
    }

    @Test
    public void getCouponActivityRulesByIdTest(){
        Long couponActivityId = 122L;
        CouponActivityRulesBO bo = couponActivityApi.getCouponActivityRulesById(couponActivityId);
        System.out.println(">>>>> bo:" + JSON.toJSONString(bo));
    }

    @Test
    public void useMemberCoupon(){
        UseMemberCouponRequest useMemberCouponRequest = new UseMemberCouponRequest();
        //useMemberCouponRequest.setMemberId(723L);
        useMemberCouponRequest.setCurrentEid(306055L);
        useMemberCouponRequest.setCurrentUserId(666L);
        useMemberCouponRequest.setId(723L);
        Boolean aBoolean = couponActivityApi.useMemberCoupon(useMemberCouponRequest);
        System.out.println(aBoolean);
    }

    @Test
    public void myAvailableMemberCouponList(){
        QueryCouponListPageRequest request = new QueryCouponListPageRequest();
        request.setEid(306055L);
        request.setPlatformType(CouponPlatformTypeEnum.B2B.getCode());
        request.setMemberId(2L);
        CouponActivityForMemberResultDTO couponActivityForMemberResultDTO = couponApi.myAvailableMemberCouponList(request);
        System.out.println(couponActivityForMemberResultDTO);
    }

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Test
    public void couponUpdateGiveNumPublish(){
        List<CouponActivityAutoGiveRecordDO> objects = new ArrayList<>();
        CouponActivityAutoGiveRecordDO newRecord = new CouponActivityAutoGiveRecordDO();
        newRecord.setCouponActivityId(206L);
        newRecord.setGiveNum(1);
        objects.add(newRecord);
        UpdateCouponGiveNumEvent updateCouponGiveNumEvent = new UpdateCouponGiveNumEvent(this,objects);
        this.applicationEventPublisher.publishEvent(updateCouponGiveNumEvent);
    }
    @Test
    public void getHasGiveCountByCouponActivityList(){
        List<Long> objects = new ArrayList<>();
        objects.add(207L);
        objects.add(206L);
        List<CouponDTO> hasGiveCountByCouponActivityList = couponService.getHasGiveCountByCouponActivityList(objects);
        List<Map<String, Long>> countByCouponActivityId = couponService.getGiveCountByCouponActivityId(objects);
        System.out.println("");
    }

    @Test
    public void getHasGiveCountByCouponAct1ivityList(){
        QueryHasGiveCouponAutoRequest request = new QueryHasGiveCouponAutoRequest();
        request.setGetType(CouponGetTypeEnum.AUTO_GET.getCode());
        List<Long> integers = Arrays.asList(491L, 490L, 489L, 488L, 487L, 486L, 485L, 484L, 483L, 482L, 481L);
        request.setAutoGetIds(integers);
        request.setBusinessType(1);
        List<CouponDTO> hasGiveCountByCouponActivityList = couponService.getHasGiveCountByCouponActivityIdList(request);;
        System.out.println(hasGiveCountByCouponActivityList);
    }

    @Test
    public void getByCouponActivityAutoId(){
        List<CouponDTO> couponList = couponService.getByCouponActivityAutoId(491L);
        System.out.println(couponList);
    }

    @Test
    public void getCouponActivityExistFlag(){
        CouponActivityExistFlagRequest request = new CouponActivityExistFlagRequest();
        List<CouponActivityExistFlagDetailRequest> detailList=new ArrayList<>();
        CouponActivityExistFlagDetailRequest couponActivityExistFlagDetailRequest = new CouponActivityExistFlagDetailRequest();
        couponActivityExistFlagDetailRequest.setEid(224809l);
        couponActivityExistFlagDetailRequest.setGoodsId(106212l);
        CouponActivityExistFlagDetailRequest a = new CouponActivityExistFlagDetailRequest();
        a.setEid(145l);
        a.setGoodsId(106284l);
        CouponActivityExistFlagDetailRequest b = new CouponActivityExistFlagDetailRequest();
        b.setEid(7l);
        b.setGoodsId(106278l);
        CouponActivityExistFlagDetailRequest c = new CouponActivityExistFlagDetailRequest();
        c.setEid(146l);
        c.setGoodsId(106280l);
        CouponActivityExistFlagDetailRequest d = new CouponActivityExistFlagDetailRequest();
        d.setEid(13504l);
        d.setGoodsId(106281l);
        CouponActivityExistFlagDetailRequest e = new CouponActivityExistFlagDetailRequest();
        e.setEid(10407l);
        e.setGoodsId(106283l);
        CouponActivityExistFlagDetailRequest f= new CouponActivityExistFlagDetailRequest();
        f.setEid(86l);
        f.setGoodsId(106279l);
        detailList.add(couponActivityExistFlagDetailRequest);
        detailList.add(b);detailList.add(c);detailList.add(d);detailList.add(e);detailList.add(f);detailList.add(a);
        request.setDetailList(detailList);

        Map<Long, List<Integer>> couponActivityExistFlag = couponActivityForGoodsService.getCouponActivityExistFlag(request);

        System.out.println(couponActivityExistFlag);
    }


}
