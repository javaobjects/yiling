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
import com.yiling.user.enterprise.api.EnterprisePromotionDownloadRecordApi;
import com.yiling.user.enterprise.api.PaymentMethodApi;
import com.yiling.user.enterprise.bo.EnterprisePromotionDownloadRecordBO;
import com.yiling.user.enterprise.dto.EnterprisePromotionDownloadRecordExportBO;
import com.yiling.user.enterprise.dto.request.QueryPromotionDownloadRecordPageRequest;
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
 * 运营后台中台-企业推广下载记录导出
 *
 * @author: lun.yu
 * @date: 2023-06-02
 */
@Service("enterprisePromotionDownloadRecordExportService")
public class EnterprisePromotionDownloadRecordExportServiceImpl implements BaseExportQueryDataService<QueryPromotionDownloadRecordPageRequest> {

    @DubboReference
    EnterprisePromotionDownloadRecordApi promotionDownloadRecordApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();
    static {
        FIELD.put("promoterId", "推广方ID");
        FIELD.put("promoterName", "推广方名称");
        FIELD.put("downloadTimeStr", "下载时间");
        FIELD.put("promoterAddress", "推广方地址");
    }

    @Override
    public QueryExportDataDTO queryData(QueryPromotionDownloadRecordPageRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<EnterprisePromotionDownloadRecordBO> page;
        int current = 1;

        do {
            request.setCurrent(current);
            request.setSize(500);
            page = promotionDownloadRecordApi.queryListPage(request);
            if ( Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }

            Page<EnterprisePromotionDownloadRecordExportBO> recordExportBOPage = PojoUtils.map(page, EnterprisePromotionDownloadRecordExportBO.class);

            recordExportBOPage.getRecords().forEach(promotionDownloadRecordBO -> {

                // 推广方地址
                promotionDownloadRecordBO.setPromoterAddress(promotionDownloadRecordBO.getProvinceName() + "-" + promotionDownloadRecordBO.getCityName() + "-" + promotionDownloadRecordBO.getRegionName());
                // 操作信息
                String time = DateUtil.format(promotionDownloadRecordBO.getDownloadTime(), "yyyy-MM-dd HH:mm:ss");
                promotionDownloadRecordBO.setDownloadTimeStr(time);

                data.add(BeanUtil.beanToMap(promotionDownloadRecordBO));
            });
            current = current + 1;

        } while (CollectionUtils.isNotEmpty(page.getRecords()));


        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("企业推广下载记录导出");
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
    public QueryPromotionDownloadRecordPageRequest getParam(Map<String, Object> map) {

        return PojoUtils.map(map, QueryPromotionDownloadRecordPageRequest.class);
    }

}
