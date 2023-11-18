package com.yiling.export.export.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.settlement.yee.api.YeeSettleSyncRecordApi;
import com.yiling.settlement.yee.dto.YeeSettleSyncRecordDTO;
import com.yiling.settlement.yee.dto.request.ExportYeeSettleListByPageRequest;
import com.yiling.settlement.yee.dto.request.QueryYeeSettleListByPageRequest;
import com.yiling.settlement.yee.enums.YeeSettleStatusEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dexi.yao
 * @date 2021-11-04
 */
@Service("yeeMemberSettleExportServiceImpl")
@Slf4j
public class YeeMemberSettleExportServiceImpl implements BaseExportQueryDataService<ExportYeeSettleListByPageRequest> {

    @DubboReference
    YeeSettleSyncRecordApi yeeSettleSyncRecordApi;


    private static final LinkedHashMap<String, String> FIELD_SHEET1 = new LinkedHashMap<String, String>() {

        {
            put("summaryNo", "单号");
            put("createTime", "结算时间");
            put("settleAmount", "应结金额");
            put("realFee", "结算手续费");
            put("realAmount", "到账金额");
            put("statusStr", "结算状态");
        }
    };

    @Override
    public QueryExportDataDTO queryData(ExportYeeSettleListByPageRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        QueryYeeSettleListByPageRequest mainQueryRequest = PojoUtils.map(request, QueryYeeSettleListByPageRequest.class);

        //不同sheet数据
        List<Map<String, Object>> settlementData = new ArrayList<>();

        int mainCurrent = 1;
        Page<YeeSettleSyncRecordDTO> mainPage;
        //分页查询结算单列表
        do {
            mainQueryRequest.setCurrent(mainCurrent);
            mainQueryRequest.setSize(50);
            //查询结算单
            mainPage = yeeSettleSyncRecordApi.queryListByPage(mainQueryRequest);

            List<YeeSettleSyncRecordDTO> mainRecords = mainPage.getRecords();

            if (CollUtil.isEmpty(mainRecords)) {
                break;
            }

            //生成sheet1
            mainRecords.forEach(recordDTO -> {
                recordDTO.setRealAmount(recordDTO.getRealAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
                recordDTO.setSettleAmount(recordDTO.getSettleAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
                recordDTO.setRealFee(recordDTO.getRealFee().setScale(2, BigDecimal.ROUND_HALF_UP));
                Map<String, Object> dataPojo = BeanUtil.beanToMap(recordDTO);
                dataPojo.put("statusStr", YeeSettleStatusEnum.getByCode(recordDTO.getStatus()).getName());
                settlementData.add(dataPojo);
            });

            mainCurrent = mainCurrent + 1;
        } while (mainPage != null && CollectionUtils.isNotEmpty(mainPage.getRecords()));

        ExportDataDTO exportApplyDTO = new ExportDataDTO();
        exportApplyDTO.setSheetName("会员回款单明细");
        // 页签字段
        exportApplyDTO.setFieldMap(FIELD_SHEET1);
        // 页签数据
        exportApplyDTO.setData(settlementData);

        //封装excel
        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportApplyDTO);
        result.setSheets(sheets);
        return result;
    }

    @Override
    public ExportYeeSettleListByPageRequest getParam(Map<String, Object> map) {
        return PojoUtils.map(map, ExportYeeSettleListByPageRequest.class);
    }
}
