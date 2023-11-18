package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponActivityStatusEnum;
import com.yiling.marketing.common.enums.CouponActivityTypeEnum;
import com.yiling.marketing.common.enums.CouponBearTypeEnum;
import com.yiling.marketing.common.enums.CouponPayMethodTypeEnum;
import com.yiling.marketing.coupon.api.CouponApi;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityRequest;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderCouponUseApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import ch.qos.logback.core.util.TimeUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 优惠券信息导出（优惠券活动）
 *
 * @author: shixing.sun
 * @date: 2022/07/25
 */
@Slf4j
@Service("CouponActivityInfoExportService")
public class CouponActivityInfoExportServiceImpl implements BaseExportQueryDataService<QueryCouponActivityRequest> {

    @DubboReference
    CouponActivityApi couponActivityApi;
    @DubboReference
    CouponApi couponApi;
    @DubboReference
    OrderCouponUseApi orderCouponUseApi;
    @DubboReference
    UserApi userApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("name", "优惠券名称");
        FIELD.put("id", "优惠券ID");
        FIELD.put("typeStr", "优惠券类型");
        FIELD.put("effectiveTime", "有效期");
        FIELD.put("createUserName", "创建人");
        FIELD.put("createTime", "创建时间");
        FIELD.put("sponsorTypeStr", "活动分类");
        FIELD.put("bearStr", "承担方");
        FIELD.put("platformSelected", "使用平台");
        FIELD.put("payMethodSelected", "可用支付方式");
        FIELD.put("coexistPromotion", "是否与哪些促销可共用");
        FIELD.put("enterpriseLimitStr", "可用商家");
        FIELD.put("goodsLimitStr", "可用商品");
        FIELD.put("totalCount", "优惠券数量");
        FIELD.put("giveCount", "已发放数量");
        FIELD.put("useCount", "已使用数量");
        FIELD.put("statusStr", "优惠券状态");
        FIELD.put("budgetCode", "预算编号");
        FIELD.put("updateUserName", "修改人");
        FIELD.put("updateTime", "修改时间");
    }

    @Override
    public QueryExportDataDTO queryData(QueryCouponActivityRequest request) {
        log.info("QueryCouponActivityRequest：{}", JSON.toJSON(request));
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();

        // 查询优惠券活动信息（商家后台）


        Page<CouponActivityDTO> page;
        int current = 1;
        int size = 500;
        do {
            // 查询已使用优惠券
            request.setCurrent(current);
            request.setSize(size);
            request.setMemberType(1);
            page = couponActivityApi.queryListForMarketing(request);
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            List<Long> idList = page.getRecords().stream().filter(couponActivity -> ObjectUtil.isNotNull(couponActivity.getId())).map(CouponActivityDTO::getId).collect(Collectors.toList());
            // 创建人、修改人id
            List<Long> userIdList = this.getUserIdList(page.getRecords());
            Map<Long, UserDTO> userMap = this.getUserMapByIds(userIdList);
            // 查询优惠券卡包，已发放
            List<CouponDTO> hasGiveCouponList = couponApi.getHasGiveCountByCouponActivityList(idList);

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
                // 总数量
                int totalCount = ObjectUtil.isNull(couponActivity.getTotalCount()) ? 0 : couponActivity.getTotalCount();
                // 已发放数量
                int giveCount = ObjectUtil.isEmpty(giveCountList) ? 0 : giveCountList;
                couponActivity.setGiveCount(giveCount);
                // 已使用数量
                couponActivity.setUseCount(ObjectUtil.isNull(useCount) ? 0 : useCount.intValue());
                String createUserName = this.getUserNameById(userMap, couponActivity.getCreateUser());
                String updateUserName = this.getUserNameById(userMap, couponActivity.getUpdateUser());
                couponActivity.setCreateUserName(createUserName);
                couponActivity.setUpdateUserName(updateUserName);
                couponActivity.setTypeStr(CouponActivityTypeEnum.getByCode(couponActivity.getType()).getName());
                couponActivity.setBearStr(CouponBearTypeEnum.getByCode(couponActivity.getBear()).getName());
                couponActivity.setSponsorTypeStr(couponActivity.getSponsorType() == 1 ? "平台活动" : "商家活动");
                if (couponActivity.getPlatformLimit() == 1) {
                    couponActivity.setPlatformSelected("全部平台");
                } else if (couponActivity.getPlatformLimit() == 2) {
                    List<Integer> platformSelectedList = Convert.toList(Integer.class, couponActivity.getPlatformSelected());
                    StringBuilder stringBuilder = new StringBuilder();
                    platformSelectedList.forEach(item->{
                        String platformSelected = item.intValue() == 1 ? "B2B" : "销售助手";
                        stringBuilder.append(platformSelected+" ") ;
                    });
                    couponActivity.setPlatformSelected(stringBuilder.toString());
                }

                if (couponActivity.getPayMethodLimit() == 1) {
                    couponActivity.setPayMethodSelected("全部支付方式");
                } else if (couponActivity.getPayMethodLimit() == 2) {
                    List<Integer> platformSelectedList = Convert.toList(Integer.class, couponActivity.getPayMethodSelected());
                    StringBuilder stringBuilder = new StringBuilder();
                    platformSelectedList.forEach(item->{
                        stringBuilder.append(CouponPayMethodTypeEnum.getByCode(item).getName()+" ") ;
                    });
                    couponActivity.setPayMethodSelected(stringBuilder.toString());
                }
                String coexistPromotion = StringUtils.isEmpty(couponActivity.getCoexistPromotion()) ? "" : "满赠";
                couponActivity.setCoexistPromotion(coexistPromotion);
                String goodsLimitStr = couponActivity.getGoodsLimit()==1 ? "全部商品" : "部分商品";
                couponActivity.setGoodsLimitStr(goodsLimitStr);
                String enterpriseLimitStr = couponActivity.getEnterpriseLimit()==1 ? "全部供应商" : "部分供应商";
                couponActivity.setEnterpriseLimitStr(enterpriseLimitStr);
                if (couponActivity.getUseDateType() == 1) {
                    String beginTime = DateUtil.format(couponActivity.getBeginTime(), "yyyy-MM-dd HH:mm:ss");
                    String endTime = DateUtil.format(couponActivity.getEndTime(), "yyyy-MM-dd HH:mm:ss");
                    couponActivity.setEffectiveTime(beginTime+"-"+endTime);
                } else if (couponActivity.getUseDateType() == 2) {
                    couponActivity.setEffectiveTime("领券后"+couponActivity.getExpiryDays()+"天失效");
                }
                couponActivity.setStatusStr(CouponActivityStatusEnum.getByCode(couponActivity.getStatus()).getName());
                if(ObjectUtil.isNotNull(request.getEid())){
                    couponActivity.setSponsorTypeStr(null);
                    couponActivity.setBearStr(null);
                    couponActivity.setEnterpriseLimitStr(null);
                    couponActivity.setBudgetCode(null);
                }
                data.add(BeanUtil.beanToMap(couponActivity));
            }
            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));


        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("优惠券活动信息");
        // 页签字段
        exportDataDTO.setFieldMap(FIELD);
        // 页签数据
        exportDataDTO.setData(data);

        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }



    @Override
    public QueryCouponActivityRequest getParam(Map<String, Object> map) {
        QueryCouponActivityRequest request = PojoUtils.map(map, QueryCouponActivityRequest.class);
        return request;
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
