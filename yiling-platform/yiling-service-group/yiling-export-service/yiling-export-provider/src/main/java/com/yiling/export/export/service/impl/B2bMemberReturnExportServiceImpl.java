package com.yiling.export.export.service.impl;

import java.util.ArrayList;
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
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.payment.enums.PaySourceEnum;
import com.yiling.user.enterprise.api.PaymentMethodApi;
import com.yiling.user.member.api.MemberReturnApi;
import com.yiling.user.member.dto.MemberReturnDTO;
import com.yiling.user.member.dto.MemberReturnExportDTO;
import com.yiling.user.member.dto.request.QueryMemberReturnPageRequest;
import com.yiling.user.member.enums.MemberReturnAuthStatusEnum;
import com.yiling.user.member.enums.MemberReturnStatusEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 运营后台B2B-会员退款导出
 * @author: lun.yu
 * @date: 2022-04-18
 */
@Service("b2bMemberReturnExportService")
public class B2bMemberReturnExportServiceImpl implements BaseExportQueryDataService<QueryMemberReturnPageRequest> {

    @DubboReference
    MemberReturnApi memberReturnApi;
    @DubboReference
    PaymentMethodApi paymentMethodApi;
    @DubboReference
    UserApi userApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();
    static {
        FIELD.put("authStatusName", "审核状态");
        FIELD.put("orderNo", "订单号");
        FIELD.put("returnReason", "退款原因");
        FIELD.put("eid", "终端ID");
        FIELD.put("ename", "终端名称");
        FIELD.put("payMethodName", "支付方式");
        FIELD.put("payTime", "支付时间");
        FIELD.put("applyUserName", "申请人");
        FIELD.put("applyTime", "申请时间");
        FIELD.put("returnRemark", "备注信息");
        FIELD.put("payAmount", "支付金额");
        FIELD.put("returnAmount", "退款金额");
        FIELD.put("returnStatusName", "退款状态");
        FIELD.put("opInfo","操作信息");
    }

    @Override
    public QueryExportDataDTO queryData(QueryMemberReturnPageRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<MemberReturnDTO> page;
        int current = 1;

        do {
            request.setCurrent(current);
            request.setSize(500);
            page = memberReturnApi.queryMemberReturnPage(request);
            if ( Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }
            Page<MemberReturnExportDTO> exportDTOPage = PojoUtils.map(page, MemberReturnExportDTO.class);

            List<Long> allUserList = ListUtil.toList();
            List<Long> applyUserList = exportDTOPage.getRecords().stream().map(MemberReturnExportDTO::getApplyUser).collect(Collectors.toList());
            allUserList.addAll(applyUserList);
            List<Long> updateUserList = exportDTOPage.getRecords().stream().map(MemberReturnExportDTO::getUpdateUser).collect(Collectors.toList());
            allUserList.addAll(updateUserList);
            Map<Long, String> userMap = userApi.listByIds(allUserList).stream().collect(Collectors.toMap(BaseDTO::getId, UserDTO::getName));

            exportDTOPage.getRecords().forEach(memberReturnExportDTO -> {

                // 支付方式名称
                if (StrUtil.isNotEmpty(memberReturnExportDTO.getPayChannel()) && PaySourceEnum.getBySource(memberReturnExportDTO.getPayChannel()) != null) {
                    memberReturnExportDTO.setPayMethodName(Objects.requireNonNull(PaySourceEnum.getBySource(memberReturnExportDTO.getPayChannel())).getName());
                }
                // 审核状态
                MemberReturnAuthStatusEnum authStatusEnum = MemberReturnAuthStatusEnum.getByCode(memberReturnExportDTO.getAuthStatus());
                memberReturnExportDTO.setAuthStatusName(authStatusEnum != null ? authStatusEnum.getName() : null);
                // 退款状态
                MemberReturnStatusEnum returnStatusEnum = MemberReturnStatusEnum.getByCode(memberReturnExportDTO.getReturnStatus());
                memberReturnExportDTO.setReturnStatusName(returnStatusEnum != null ? returnStatusEnum.getName() : null);

                // 申请人名称
                memberReturnExportDTO.setApplyUserName(userMap.get(memberReturnExportDTO.getApplyUser()));
                // 操作信息
                String time = DateUtil.format(memberReturnExportDTO.getUpdateTime(), "yyyy-MM-dd HH:mm:ss");
                memberReturnExportDTO.setOpInfo(userMap.get(memberReturnExportDTO.getUpdateUser()) + " " + time);

                data.add(BeanUtil.beanToMap(memberReturnExportDTO));
            });
            current = current + 1;

        } while (CollectionUtils.isNotEmpty(page.getRecords()));


        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("已购买会员记录导出");
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
    public QueryMemberReturnPageRequest getParam(Map<String, Object> map) {

        return PojoUtils.map(map, QueryMemberReturnPageRequest.class);
    }

}
