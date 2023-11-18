package com.yiling.export.export.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportHmcActivityDoctorBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.HmcActivityDocDTO;
import com.yiling.ih.user.dto.HmcActivityDoctorDTO;
import com.yiling.ih.user.dto.request.QueryActivityDoctorListRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 医带患活动-医生导出
 *
 * @author: fan.shen
 * @date: 2023-02-03
 */
@Slf4j
@Service("hmcActivityDocExportService")
public class HmcActivityDocExportServiceImpl implements BaseExportQueryDataService<QueryActivityDoctorListRequest> {

    @DubboReference
    DoctorApi doctorApi;

    @Autowired
    FileService fileService;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<String, String>() {
        {
            put("doctorId", "医生id");
            put("name", "医生姓名");
            put("hospitalName", "第1执业医院");
            put("provinceName", "所属省");
            put("reportCount", "报道总人数");
            put("newReportCount", "新用户报道人数");
            put("prescriptionCount", "处方审核通过人数");
        }
    };

    @Override
    public QueryExportDataDTO queryData(QueryActivityDoctorListRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<HmcActivityDocDTO> page = null;
        int current = 1;
        do {

            request.setCurrent(current);
            request.setSize(500);
            page = this.handleData(request, data);
            if (null != page && CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            current = current + 1;
        } while (null != page && page.getTotal() > 500 && CollUtil.isNotEmpty(page.getRecords()));
        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("活动医生");
        exportDataDTO.setFieldMap(FIELD);
        exportDataDTO.setData(data);
        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }

    private Page<HmcActivityDocDTO> handleData(QueryActivityDoctorListRequest request, List<Map<String, Object>> data) {
        Page<HmcActivityDocDTO> recordBOPage = doctorApi.queryActivityDocPage(request);
        if (recordBOPage.getTotal() == 0) {
            return null;
        }
        List<HmcActivityDocDTO> records = recordBOPage.getRecords();
        records.forEach(item -> {
            ExportHmcActivityDoctorBO export = PojoUtils.map(item, ExportHmcActivityDoctorBO.class);
            Map<String, Object> dataPojo = BeanUtil.beanToMap(export);
            data.add(dataPojo);
        });
        return recordBOPage;
    }

    @Override
    public QueryActivityDoctorListRequest getParam(Map<String, Object> map) {
        QueryActivityDoctorListRequest recordPageRequest = PojoUtils.map(map, QueryActivityDoctorListRequest.class);

        return recordPageRequest;
    }
}