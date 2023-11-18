package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportHmcActivityDoctorBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.HmcActivityDoctorDTO;
import com.yiling.ih.user.dto.request.QueryActivityDoctorListRequest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * 患教活动医生导出
 * @author: gxl
 * @date: 2022/9/8
 */
@Slf4j
@Service("hmcActivityDoctorExportService")
public class HmcActivityDoctorExportServiceImpl  implements BaseExportQueryDataService<QueryActivityDoctorListRequest> {
    @DubboReference
    DoctorApi doctorApi;

    @Autowired
    FileService fileService;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<String, String>() {
        {
            put("doctorId", "医生id");
            put("name", "医生姓名");
            put("hospitalName", "第1执业医院");
            put("caseCount", "邀请病历数");
            put("qrcodeUrl", "活动码");
            put("createTime", "参与时间");
        }
    };

    @Override
    public QueryExportDataDTO queryData(QueryActivityDoctorListRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<HmcActivityDoctorDTO> page = null;
        int current = 1;
        do {

            request.setCurrent(current);
            request.setSize(500);
            page = this.handleData(request, data);
            if (null != page && CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            current = current + 1;
        } while (null != page &&  page.getTotal()>500 && CollUtil.isNotEmpty(page.getRecords()));
        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("活动医生");
        exportDataDTO.setFieldMap(FIELD);
        exportDataDTO.setData(data);
        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }
    private Page<HmcActivityDoctorDTO> handleData(QueryActivityDoctorListRequest request, List<Map<String, Object>> data) {
        Page<HmcActivityDoctorDTO> recordBOPage = doctorApi.queryActivityDoctorList(request);
        if (recordBOPage.getTotal() == 0) {
            return null;
        }
        List<HmcActivityDoctorDTO> records = recordBOPage.getRecords();


        records.forEach(insuranceRecordBO -> {
            ExportHmcActivityDoctorBO export = new ExportHmcActivityDoctorBO();
            PojoUtils.map(insuranceRecordBO,export);
            export.setQrcodeUrl(fileService.getUrl(export.getQrcodeUrl(), FileTypeEnum.ACTIVITY_DOCTOR_QRCODE));
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