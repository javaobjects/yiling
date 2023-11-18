package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponActivityStatusEnum;
import com.yiling.marketing.common.enums.CouponActivityTypeEnum;
import com.yiling.marketing.common.enums.CouponBearTypeEnum;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.common.enums.CouponPayMethodTypeEnum;
import com.yiling.marketing.coupon.api.CouponApi;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.CouponMemberExportInfoDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityRequest;
import com.yiling.marketing.couponactivity.enums.CouponActivityUseDateTypeEnum;
import com.yiling.order.order.api.OrderCouponUseApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.MemberOrderApi;
import com.yiling.user.member.dto.MemberOrderCouponDTO;
import com.yiling.user.member.dto.request.QueryMemberCouponPageRequest;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
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
@Service("MemberCouponUsedInfoExportService")
public class MemberCouponUsedInfoExportServiceImpl implements BaseExportQueryDataService<QueryCouponActivityRequest> {

    @DubboReference
    CouponActivityApi couponActivityApi;
    @DubboReference
    CouponApi couponApi;
    @DubboReference
    OrderCouponUseApi orderCouponUseApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    MemberOrderApi memberOrderApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("couponActivityId", "优惠券ID");
        FIELD.put("couponRules", "优惠规则");
        FIELD.put("eid", "获券企业ID");
        FIELD.put("ename", "获券企业名称");
        FIELD.put("getTypeStr", "发放方式");
        FIELD.put("userStatus", "使用状态");
        FIELD.put("getTime", "发放时间");
        FIELD.put("useTime", "使用时间");
        FIELD.put("memberName", "购买会员方案");
        FIELD.put("validTime", "会员时长");
        FIELD.put("discountAmount", "优惠金额");
        FIELD.put("payAmount", "实付金额");
    }

    @Override
    public QueryExportDataDTO queryData(QueryCouponActivityRequest request) {
        log.info("QueryCouponActivityRequest：{}", JSON.toJSON(request));
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();

        Page<MemberOrderCouponDTO> page = null;
        int current = 1;
        int size = 500;
        do {
            // 查询已使用优惠券
            request.setCurrent(current);
            request.setSize(size);
            request.setMemberType(1);
            QueryCouponActivityDetailRequest detailRequest = new QueryCouponActivityDetailRequest();
            detailRequest.setId(request.getCouponActivityId());
            CouponActivityDetailDTO couponActivity = couponActivityApi.getCouponActivityById(detailRequest);
            if (ObjectUtil.isNull(couponActivity)) {
                break;
            }
            // 优惠规则
            String name = couponActivity.getName();
            CouponActivityDTO dto = PojoUtils.map(couponActivity, CouponActivityDTO.class);
            String couponRules = couponActivityApi.buildCouponRules(dto);
            String effectiveTime = buildEffectiveTime(couponActivity);
            // 查询已使用优惠券
            QueryMemberCouponPageRequest queryMemberCouponPageRequest = new QueryMemberCouponPageRequest();
            queryMemberCouponPageRequest.setCouponActivityId(request.getCouponActivityId());
            queryMemberCouponPageRequest.setCurrent(current);
            page = memberOrderApi.queryMemberOrderPageByCoupon(queryMemberCouponPageRequest);
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            Page<CouponMemberExportInfoDTO> couponMemberExportInfoDTOPage = PojoUtils.map(page, CouponMemberExportInfoDTO.class);
            // 获券企业名称
            List<Long> eidList = page.getRecords().stream().map(MemberOrderCouponDTO::getEid).distinct().collect(Collectors.toList());
            Map<Long, String> enameMap = getEnameMap(eidList);
            // 组装优惠券信息
            List<Long> couponIds = page.getRecords().stream().map(MemberOrderCouponDTO::getCouponId).collect(Collectors.toList());
            List<CouponDTO> couponById = couponActivityApi.getCouponById(couponIds);
            if (CollUtil.isEmpty(couponById)) {
                break;
            }
            Map<Long, CouponDTO> couponDTOMap = couponById.stream().collect(Collectors.toMap(CouponDTO::getId, Function.identity()));
            for (CouponMemberExportInfoDTO couponUsed : couponMemberExportInfoDTOPage.getRecords()) {
                Long couponId = couponUsed.getCouponId();
                CouponDTO couponDTO = couponDTOMap.get(couponId);
                if (ObjectUtil.isNotNull(couponDTO)) {
                    couponUsed.setGetTypeStr(CouponGetTypeEnum.getByCode(couponDTO.getGetType()).getName());
                    couponUsed.setUseTime(couponDTO.getUseTime());
                    couponUsed.setGetTime(couponDTO.getGetTime());
                }
                couponUsed.setUserStatus("已使用");
                couponUsed.setCouponActivityId(request.getCouponActivityId());
                couponUsed.setCouponRules(couponRules);
                couponUsed.setCouponName(name);
                couponUsed.setEname(enameMap.get(couponUsed.getEid()));
                couponUsed.setEffectiveTime(effectiveTime);
                data.add(BeanUtil.beanToMap(couponUsed));
            }
            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));

        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("会员优惠券使用记录信息");
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

    private String buildEffectiveTime(CouponActivityDetailDTO couponActivity) {
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
}
