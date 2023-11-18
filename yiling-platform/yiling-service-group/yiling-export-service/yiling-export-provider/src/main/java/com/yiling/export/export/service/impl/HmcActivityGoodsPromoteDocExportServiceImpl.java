package com.yiling.export.export.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportHmcActivityDoctorBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.wechat.enums.HmcActivitySourceEnum;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.HmcActivityDocDTO;
import com.yiling.ih.user.dto.request.QueryActivityDoctorListRequest;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.dto.request.QueryActivityDoctorUserCountRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * HMC 商品推广活动-医生导出
 *
 * @author: fan.shen
 * @date: 2023-02-15
 */
@Slf4j
@Service("hmcActivityGoodsPromoteDocExportService")
public class HmcActivityGoodsPromoteDocExportServiceImpl implements BaseExportQueryDataService<QueryActivityDoctorListRequest> {

    @DubboReference
    DoctorApi doctorApi;

    @Autowired
    FileService fileService;

    @DubboReference
    HmcUserApi hmcUserApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<String, String>() {
        {
            put("doctorId", "医生id");
            put("name", "医生姓名");
            put("hospitalName", "第1执业医院");
            put("caseCount", "邀请病例数");
            put("qrcodeUrl", "活动码");
            put("createTimeStr", "参与时间");
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
        exportDataDTO.setSheetName("商品推广活动医生");
        exportDataDTO.setFieldMap(FIELD);
        exportDataDTO.setData(data);
        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }

    private Page<HmcActivityDocDTO> handleData(QueryActivityDoctorListRequest request, List<Map<String, Object>> data) {
        Page<HmcActivityDocDTO> recordBOPage = doctorApi.queryActivityBaZiPage(request);
        if (recordBOPage.getTotal() == 0) {
            return null;
        }
        List<HmcActivityDocDTO> records = recordBOPage.getRecords();
        List<Integer> doctorIdList = records.stream().map(HmcActivityDocDTO::getDoctorId).collect(Collectors.toList());

        // 查询活动医生邀请用户数量
        QueryActivityDoctorUserCountRequest queryDoctorRequest = new QueryActivityDoctorUserCountRequest();
        queryDoctorRequest.setActivitySource(HmcActivitySourceEnum.BA_ZI_BU_SHEN.getType());
        queryDoctorRequest.setDoctorIdList(doctorIdList);
        List<Map<String, Long>> doctorInviteUserCountMap = hmcUserApi.queryActivityDoctorInviteUserCount(queryDoctorRequest);

        records.forEach(item -> {
            ExportHmcActivityDoctorBO export = PojoUtils.map(item, ExportHmcActivityDoctorBO.class);
            export.setQrcodeUrl(fileService.getUrl(export.getQrcodeUrl(), FileTypeEnum.ACTIVITY_DOCTOR_QRCODE));

            export.setCaseCount(0L);
            // 赋值邀请用户数量
            Optional<Map<String, Long>> res = doctorInviteUserCountMap.stream().filter(doctorInviteUserCount -> doctorInviteUserCount.get("doctorId").equals(item.getDoctorId().longValue())).findFirst();
            res.ifPresent(map -> export.setCaseCount(map.get("userCount")));

            // 绑定时间
            if (Objects.nonNull(item.getCreateTime())) {
                export.setCreateTimeStr(DateUtil.format(item.getCreateTime(), DatePattern.NORM_DATETIME_PATTERN));
            }

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