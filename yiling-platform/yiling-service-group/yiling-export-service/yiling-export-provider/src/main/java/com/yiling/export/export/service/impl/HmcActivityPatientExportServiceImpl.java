package com.yiling.export.export.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.dict.bo.DictBO;
import com.yiling.export.export.bo.ExportHmcActivityDoctorBO;
import com.yiling.export.export.bo.ExportHmcActivityPatientBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.service.FileService;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.HmcActivityDocDTO;
import com.yiling.ih.user.dto.HmcActivityDocPatientDTO;
import com.yiling.ih.user.dto.request.QueryActivityDocPatientListRequest;
import com.yiling.ih.user.dto.request.QueryActivityDoctorListRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.*;

/**
 * 医带患活动-患者导出
 *
 * @author: fan.shen
 * @date: 2023-02-06
 */
@Slf4j
@Service("hmcActivityPatientExportService")
public class HmcActivityPatientExportServiceImpl implements BaseExportQueryDataService<QueryActivityDocPatientListRequest> {

    @DubboReference
    DoctorApi doctorApi;

    @Autowired
    FileService fileService;

    @DubboReference
    private DictApi dictApi;

    private static final String HMC_ACT_PATIENT_CER_STATE = "hmc_act_patient_cer_state";

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<String, String>() {
        {
            put("doctorId", "医生id");
            put("doctorName", "医生姓名");
            put("hospitalName", "第1执业医院");
            put("province", "所属省");
            put("patientName", "患者姓名");
            put("mobile", "手机号");
            put("gender", "性别");
            put("age", "年龄");
            put("diseaseTags", "病史标签");
            put("medicationTags", "药品标签");
            put("userState", "是否新用户");
            put("certificateState", "凭证状态");
            put("rejectReason", "驳回原因");
            put("createTimeStr", "绑定时间");
            put("arraignmentTimeStr", "提审时间");
            put("auditTimeStr", "审核时间");
        }
    };

    @Override
    public QueryExportDataDTO queryData(QueryActivityDocPatientListRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<HmcActivityDocPatientDTO> page = null;
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
        exportDataDTO.setSheetName("活动患者");
        exportDataDTO.setFieldMap(FIELD);
        exportDataDTO.setData(data);
        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }

    private Page<HmcActivityDocPatientDTO> handleData(QueryActivityDocPatientListRequest request, List<Map<String, Object>> data) {
        Page<HmcActivityDocPatientDTO> recordBOPage = doctorApi.queryActivityDocPatientPage(request);
        if (recordBOPage.getTotal() == 0) {
            return null;
        }
        List<HmcActivityDocPatientDTO> records = recordBOPage.getRecords();

        // HMC医带患患者凭证状态
        DictBO dictBO = dictApi.getDictByName(HMC_ACT_PATIENT_CER_STATE);

        records.forEach(item -> {
            ExportHmcActivityPatientBO export = PojoUtils.map(item, ExportHmcActivityPatientBO.class);
            // 绑定时间
            if (Objects.nonNull(item.getCreateTime())) {
                export.setCreateTimeStr(DateUtil.format(item.getCreateTime(), DatePattern.NORM_DATETIME_PATTERN));
            }
            // 提审时间
            if (Objects.nonNull(item.getArraignmentTime())) {
                export.setArraignmentTimeStr(DateUtil.format(item.getArraignmentTime(), DatePattern.NORM_DATETIME_PATTERN));
            }
            // 审核时间
            if (Objects.nonNull(item.getAuditTime())) {
                export.setAuditTimeStr(DateUtil.format(item.getAuditTime(), DatePattern.NORM_DATETIME_PATTERN));
            }
            // 是否新用户
            if (item.getUserState() == 1) {
                export.setUserState("是");
            }
            if (item.getUserState() == 2) {
                export.setUserState("不是");
            }
            Map<String, Object> dataPojo = BeanUtil.beanToMap(export);
            DictBO.DictData dictData = dictBO.getDataList().stream().filter(e -> e.getValue().equalsIgnoreCase(item.getCertificateState().toString())).findFirst().orElse(null);
            // 设置凭证状态
            if (Objects.nonNull(dictData)) {
                dataPojo.put("certificateState", dictData.getLabel());
            }
            data.add(dataPojo);
        });
        return recordBOPage;
    }

    @Override
    public QueryActivityDocPatientListRequest getParam(Map<String, Object> map) {
        return PojoUtils.map(map, QueryActivityDocPatientListRequest.class);
    }
}