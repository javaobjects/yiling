package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.couponactivity.dto.CouponActivityGiveDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGivePageRequest;
import com.yiling.marketing.couponactivityautogive.api.CouponActivityAutoGiveApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 已发放优惠券导出（自动发放）
 *
 * @author: houjie.sun
 * @date: 2021/12/17
 */
@Service("couponActivityAutoHasGiveExportService")
public class CouponActivityAutoHasGiveExportServiceImpl implements BaseExportQueryDataService<QueryCouponActivityGivePageRequest> {

    @DubboReference
    CouponActivityAutoGiveApi                          couponActivityAutoGiveApi;
    @DubboReference
    EnterpriseApi                                      enterpriseApi;
    @DubboReference
    UserApi                                            userApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("id", "优惠券ID");
        FIELD.put("eid", "获券企业id");
        FIELD.put("ename", "获券企业名称");
        FIELD.put("getTypeStr", "发放方式");
        FIELD.put("getTime", "发放时间");
    }

    @Override
    public QueryExportDataDTO queryData(QueryCouponActivityGivePageRequest request) {
        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        // 需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<CouponActivityGiveDTO> page;
        int current = 1;
        int size = 500;
        do {
            request.setCurrent(current);
            request.setSize(size);
            page = couponActivityAutoGiveApi.queryGiveListPage(request);
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
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
                // 发放方式，1-运营发放；2-自动发放；3-自主领取
                String getTypeStr = CouponGetTypeEnum.getByCode(p.getGetType()).getName();
                p.setGetTypeStr(StrUtil.isBlank(getTypeStr) ? "" : getTypeStr);

                data.add(BeanUtil.beanToMap(p));
            });

            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));

        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("已发放优惠券导出（自动发放）");
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
    public QueryCouponActivityGivePageRequest getParam(Map<String, Object> map) {
        QueryCouponActivityGivePageRequest request = PojoUtils.map(map, QueryCouponActivityGivePageRequest.class);
        request.setGetType(CouponGetTypeEnum.AUTO_GIVE.getCode());
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

    private Map<Long, EnterpriseDTO> getEnterpriseMapByIds(List<Long> eids) {
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
}
