package com.yiling.export.export.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.payment.enums.PaySourceEnum;
import com.yiling.user.enterprise.api.PaymentMethodApi;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.api.MemberBuyRecordApi;
import com.yiling.user.member.api.MemberBuyStageApi;
import com.yiling.user.member.dto.MemberBuyRecordDTO;
import com.yiling.user.member.dto.MemberBuyRecordExportDTO;
import com.yiling.user.member.dto.request.QueryBuyRecordRequest;
import com.yiling.user.member.enums.MemberOpenTypeEnum;
import com.yiling.user.member.enums.MemberSourceEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;

/**
 * 运营后台B2B-会员购买记录导出
 *
 * @author: lun.yu
 * @date: 2021/11/18
 */
@Service("b2bMemberBuyRecordExportService")
public class B2bMemberBuyRecordExportServiceImpl implements BaseExportQueryDataService<QueryBuyRecordRequest> {

    @DubboReference
    MemberBuyRecordApi memberBuyRecordApi;
    @DubboReference
    MemberBuyStageApi memberBuyStageApi;
    @DubboReference
    UserApi userApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("orderNo", "订单编号");
        FIELD.put("memberName", "会员名称");
        FIELD.put("eid", "终端ID");
        FIELD.put("ename", "终端名称");
        FIELD.put("provinceName", "终端地址（省）");
        FIELD.put("cityName", "终端地址（市）");
        FIELD.put("regionName", "终端地址（区）");
        FIELD.put("contactorPhone", "企业管理员手机号");
        FIELD.put("buyRule", "购买规则");
        FIELD.put("createTime", "购买时间");
        FIELD.put("promoterId", "推广方ID");
        FIELD.put("promoterName", "推广方名称");
        FIELD.put("promoterUserId", "推广人ID");
        FIELD.put("promoterUserName", "推广人名称");
        FIELD.put("startTime", "会员开始时间");
        FIELD.put("endTime", "会员结束时间");
        FIELD.put("payMethodName", "支付方式");
        FIELD.put("originalPrice", "原价");
        FIELD.put("discountAmount", "优惠金额");
        FIELD.put("payAmount", "支付金额");
        FIELD.put("returnAmount", "退款金额");
        FIELD.put("returnFlagName", "是否退款");
        FIELD.put("expireFlagName", "是否过期");
        FIELD.put("openTypeName", "开通类型");
        FIELD.put("sourceName", "数据来源");
    }

    private static final LinkedHashMap<String, String> FIELD2 = new LinkedHashMap<>();

    static {
        FIELD2.put("orderNo", "订单编号");
        FIELD2.put("memberName", "会员名称");
        FIELD2.put("eid", "终端ID");
        FIELD2.put("ename", "终端名称");
        FIELD2.put("provinceName", "终端地址（省）");
        FIELD2.put("cityName", "终端地址（市）");
        FIELD2.put("regionName", "终端地址（区）");
        FIELD2.put("contactorPhone", "企业管理员手机号");
        FIELD2.put("buyRule", "购买规则");
        FIELD2.put("createTime", "购买时间");
        FIELD2.put("promoterId", "推广方ID");
        FIELD2.put("promoterName", "推广方名称");
        FIELD2.put("promoterUserId", "推广人ID");
        FIELD2.put("promoterUserName", "推广人名称");
        FIELD2.put("startTime", "会员开始时间");
        FIELD2.put("endTime", "会员结束时间");
        FIELD2.put("payMethodName", "支付方式");
        FIELD2.put("originalPrice", "原价");
        FIELD2.put("discountAmount", "优惠金额");
        FIELD2.put("payAmount", "支付金额");
        FIELD2.put("returnAmount", "退款金额");
        FIELD2.put("returnFlagName", "是否退款");
        FIELD2.put("expireFlagName", "是否过期");
        FIELD2.put("openTypeName", "开通类型");
        FIELD2.put("sourceName", "数据来源");
        FIELD2.put("cancelFlagName", "会员状态");
        FIELD2.put("updateUserName", "操作人");
        FIELD2.put("updateTime", "取消时间");
    }

    @Override
    public QueryExportDataDTO queryData(QueryBuyRecordRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<MemberBuyRecordDTO> page;
        int current = 1;

        do {
            request.setCurrent(current);
            request.setSize(500);
            page = memberBuyRecordApi.queryBuyRecordListPage(request);
            if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }
            Page<MemberBuyRecordExportDTO> exportDTOPage = PojoUtils.map(page, MemberBuyRecordExportDTO.class);
            // 根据会员订单号查询购买条件名称
            List<String> orderNoList = exportDTOPage.getRecords().stream().map(MemberBuyRecordExportDTO::getOrderNo).collect(Collectors.toList());
            Map<String, String> stageNameMap = memberBuyStageApi.getStageNameByOrderNo(orderNoList);

            List<Long> updateUserList = exportDTOPage.getRecords().stream().map(MemberBuyRecordExportDTO::getUpdateUser).distinct().collect(Collectors.toList());
            Map<Long, String> userMap = userApi.listByIds(updateUserList).stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));

            exportDTOPage.getRecords().forEach(memberBuyRecordDTO -> {

                // 支付方式名称
                if (StrUtil.isNotEmpty(memberBuyRecordDTO.getPayChannel()) && PaySourceEnum.getBySource(memberBuyRecordDTO.getPayChannel()) != null) {
                    memberBuyRecordDTO.setPayMethodName(Objects.requireNonNull(PaySourceEnum.getBySource(memberBuyRecordDTO.getPayChannel())).getName());
                }
                // 是否退款、是否过期、开通类型、数据来源
                memberBuyRecordDTO.setReturnFlagName(memberBuyRecordDTO.getReturnAmount().compareTo(BigDecimal.ZERO) == 0 ? "未退款" : "已退款");
                memberBuyRecordDTO.setExpireFlagName(memberBuyRecordDTO.getEndTime().compareTo(new Date()) > 0 ? "未过期" : "已过期");
                memberBuyRecordDTO.setSourceName(Objects.requireNonNull(MemberSourceEnum.getByCode(memberBuyRecordDTO.getSource())).getName());
                memberBuyRecordDTO.setOpenTypeName(Objects.requireNonNull(MemberOpenTypeEnum.getByCode(memberBuyRecordDTO.getOpenType())).getName());
                // 购买规则 = 购买规则 + 购买条件名称
                memberBuyRecordDTO.setBuyRule(memberBuyRecordDTO.getBuyRule() + stageNameMap.getOrDefault(memberBuyRecordDTO.getOrderNo(), ""));

                // 取消记录使用字段
                memberBuyRecordDTO.setCancelFlagName(memberBuyRecordDTO.getCancelFlag() == 1 ? "已取消" : "未取消");
                memberBuyRecordDTO.setUpdateUserName(userMap.get(memberBuyRecordDTO.getUpdateUser()));

                data.add(BeanUtil.beanToMap(memberBuyRecordDTO));
            });
            current = current + 1;

        } while (CollectionUtils.isNotEmpty(page.getRecords()));


        ExportDataDTO exportDataDTO = new ExportDataDTO();
        String sheetName = (request.getPageType() != null && request.getPageType() == 2) ? "取消会员记录导出" : "已购买会员记录导出";
        exportDataDTO.setSheetName(sheetName);
        // 页签字段
        exportDataDTO.setFieldMap((request.getPageType() != null && request.getPageType() == 2) ? FIELD2 : FIELD);
        // 页签数据
        exportDataDTO.setData(data);

        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }

    @Override
    public QueryBuyRecordRequest getParam(Map<String, Object> map) {
        QueryBuyRecordRequest request = PojoUtils.map(map, QueryBuyRecordRequest.class);
        if (request.getPageType() != null && request.getPageType() == 2) {
            request.setCancelFlag(1);
        } else {
            request.setCancelFlag(0);
        }

        String sourceListStr = map.getOrDefault("sourceList", "").toString();
        if (StrUtil.isEmpty(sourceListStr)) {
            return request;
        }

        List<Integer> sourceList = ListUtil.toList();
        if(sourceListStr.contains(",")){
            sourceList = Convert.toList(Integer.class, sourceListStr);
        } else {
            sourceList.add(Integer.parseInt(sourceListStr));
        }

        request.setSourceList(sourceList);

        return request;
    }

}

