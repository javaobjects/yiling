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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportCouponAutoGivePageListBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponActivityAutoGiveTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityStatusEnum;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.coupon.api.CouponApi;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.coupon.dto.request.QueryHasGiveCouponAutoRequest;
import com.yiling.marketing.couponactivityautogive.api.CouponActivityAutoGiveApi;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveCouponDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGiveRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * 自动发放导出-页面已经屏蔽，不再使用
 * @author: houjie.sun
 * @date: 2021/12/16
 */
@Service("couponActivityAutoGivePageListExportService")
public class CouponActivityAutoGivePageListExportServiceImpl implements BaseExportQueryDataService<QueryCouponActivityAutoGiveRequest> {

    @DubboReference
    CouponActivityAutoGiveApi                          couponActivityAutoGiveApi;
    @DubboReference
    CouponApi                                          couponApi;
    @DubboReference
    EnterpriseApi                                      enterpriseApi;
    @DubboReference
    UserApi                                            userApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("id", "优惠券活动ID");
        FIELD.put("name", "优惠券名称");
        FIELD.put("type", "发券类型");
        FIELD.put("status", "启用状态");
        FIELD.put("activityStatus", "活动状态");
        FIELD.put("effectiveTime", "活动时间");
        FIELD.put("giveCount", "已发放数量");
        FIELD.put("createUserName", "创建人");
        FIELD.put("createTimeStr", "创建时间");
        FIELD.put("updateUserName", "修改人");
        FIELD.put("updateTimeStr", "修改时间");
    }

    @Override
    public QueryExportDataDTO queryData(QueryCouponActivityAutoGiveRequest request) {
        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        // 需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<ExportCouponAutoGivePageListBO> page;
        int current = 1;
        int size = 500;
        do {
            request.setCurrent(current);
            request.setSize(size);

            page = PojoUtils.map(couponActivityAutoGiveApi.queryListPage(request), ExportCouponAutoGivePageListBO.class);
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            // 创建人、修改人id
            List<Long> userIdList = this.getGiveUserIdList(page.getRecords());
            Map<Long, UserDTO> userMap = this.getUserMapByIds(userIdList);
            long nowTime = System.currentTimeMillis();
            Long currentEid = 0L;
            // 查询自动发放优惠券
            Map<Long, Integer> giveCountMap = buildGetCountMapMap(page);

            String format = "yyyy-MM-dd HH:mm:ss";
            for (ExportCouponAutoGivePageListBO p : page.getRecords()) {
                Long id = p.getId();
                // 发券类型: 1-订单累计金额；2-会员自动发券
                if (ObjectUtil.equal(CouponActivityAutoGiveTypeEnum.ORDER_ACCUMULATE_AMOUNT.getCode(), p.getType())) {
                    p.setTypeStr(CouponActivityAutoGiveTypeEnum.ORDER_ACCUMULATE_AMOUNT.getName());
                } else if (ObjectUtil.equal(CouponActivityAutoGiveTypeEnum.MEMBER_AUTO.getCode(), p.getType())) {
                    p.setTypeStr(CouponActivityAutoGiveTypeEnum.MEMBER_AUTO.getName());
                } else if (ObjectUtil.equal(CouponActivityAutoGiveTypeEnum.ENTERPRISE_POPULARIZE.getCode(), p.getType())) {
                    p.setTypeStr(CouponActivityAutoGiveTypeEnum.ENTERPRISE_POPULARIZE.getName());
                }
                // 已发放数量
                int giveCount = ObjectUtil.isNull(giveCountMap.get(id)) ? 0 : giveCountMap.get(id);
                p.setGiveCount(giveCount);
                // 启用状态: 1-启用；2-停用；3-废弃
                if (ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), p.getStatus())) {
                    p.setStatusStr(CouponActivityStatusEnum.ENABLED.getName());
                } else if (ObjectUtil.equal(CouponActivityStatusEnum.DISABLED.getCode(), p.getStatus())) {
                    p.setStatusStr(CouponActivityStatusEnum.DISABLED.getName());
                } else if (ObjectUtil.equal(CouponActivityStatusEnum.SCRAP.getCode(), p.getStatus())) {
                    p.setStatusStr(CouponActivityStatusEnum.SCRAP.getName());
                }
                // 活动状态: 1-未开始 2-进行中 3-已结束
                if (p.getBeginTime().getTime() > nowTime) {
                    p.setActivityStatus("未开始");
                } else if (p.getBeginTime().getTime() <= nowTime && p.getEndTime().getTime() > nowTime) {
                    p.setActivityStatus("进行中");
                } else if (p.getEndTime().getTime() <= nowTime) {
                    p.setActivityStatus("已结束");
                }
                // 停用、作废, 活动状态 = 已结束
                if (!ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), p.getStatus())) {
                    p.setActivityStatus("已结束");
                }
                // 活动时间
                String beginTime = DateUtil.format(p.getBeginTime(), format);
                String endTime = DateUtil.format(p.getEndTime(), format);
                p.setEffectiveTime(beginTime.concat(" - ").concat(endTime));
                // 操作人姓名
                String createUserName = this.getUserNameById(userMap, p.getCreateUser());
                String updateUserName = this.getUserNameById(userMap, p.getUpdateUser());
                p.setCreateUserName(createUserName);
                p.setUpdateUserName(updateUserName);
                // 操作时间
                String createTimeStr = DateUtil.format(p.getCreateTime(), format);
                String updateTimeStr = DateUtil.format(p.getUpdateTime(), format);
                p.setCreateTimeStr(createTimeStr);
                p.setUpdateTimeStr(updateTimeStr);

                data.add(BeanUtil.beanToMap(p));
            }

            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));

        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("自动发放导出");
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
    public QueryCouponActivityAutoGiveRequest getParam(Map<String, Object> map) {
        QueryCouponActivityAutoGiveRequest request = PojoUtils.map(map, QueryCouponActivityAutoGiveRequest.class);
        return request;
    }

    private Map<Long, UserDTO> getUserMapByIds(List<Long> userIds) {
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

    private List<Long> getGiveUserIdList(List<ExportCouponAutoGivePageListBO> list) {
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

    private String getUserNameById(Map<Long, UserDTO> userMap, Long userId) {
        if (MapUtil.isEmpty(userMap) || ObjectUtil.isNull(userId)) {
            return "";
        }
        UserDTO user = userMap.get(userId);
        if (ObjectUtil.isNotNull(user)) {
            return user.getName();
        }
        return "";
    }

    private Map<Long, Integer> buildGetCountMapMap(Page<ExportCouponAutoGivePageListBO> page) {
        Map<Long, Integer> getCountMap = new HashMap<>();
        if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
            return getCountMap;
        }
        List<Long> autoGiveIdList = page.getRecords().stream().map(ExportCouponAutoGivePageListBO::getId).collect(Collectors.toList());
        // 查询关联的优惠券
        List<CouponActivityAutoGiveCouponDTO> autoGiveCouponList = couponActivityAutoGiveApi.getAutoGiveCouponByAutoGiveIdList(autoGiveIdList);
        if (CollUtil.isEmpty(autoGiveCouponList)) {
            return getCountMap;
        }
        Map<Long, List<CouponActivityAutoGiveCouponDTO>> autoGetMap = autoGiveCouponList.stream()
            .collect(Collectors.groupingBy(CouponActivityAutoGiveCouponDTO::getCouponActivityAutoGiveId));
        List<Long> couponActivityIdList = autoGiveCouponList.stream().map(CouponActivityAutoGiveCouponDTO::getCouponActivityId).distinct()
            .collect(Collectors.toList());
        // 优惠券
        QueryHasGiveCouponAutoRequest getRequest = new QueryHasGiveCouponAutoRequest();
        getRequest.setGetType(CouponGetTypeEnum.AUTO_GIVE.getCode());
        getRequest.setCouponActivityIds(couponActivityIdList);
        List<CouponDTO> couponList = couponApi.getHasGiveListByCouponActivityIdList(getRequest);
        if (CollUtil.isEmpty(couponList)) {
            return getCountMap;
        }
        Map<Long, List<CouponDTO>> couponMap = couponList.stream().collect(Collectors.groupingBy(CouponDTO::getCouponActivityId));
        for (Map.Entry<Long, List<CouponActivityAutoGiveCouponDTO>> entry : autoGetMap.entrySet()) {
            Long autoGiveId = entry.getKey();
            List<CouponActivityAutoGiveCouponDTO> couponActivityList = entry.getValue();
            int getCount = 0;
            if (CollUtil.isNotEmpty(couponActivityList)) {
                for (CouponActivityAutoGiveCouponDTO autoGiveCoupon : couponActivityList) {
                    List<CouponDTO> couponDTOS = couponMap.get(autoGiveCoupon.getCouponActivityId());
                    if (CollUtil.isNotEmpty(couponDTOS)) {
                        getCount = getCount + couponDTOS.size();
                    }
                }
            }
            getCountMap.put(autoGiveId, getCount);
        }
        return getCountMap;
    }
}
