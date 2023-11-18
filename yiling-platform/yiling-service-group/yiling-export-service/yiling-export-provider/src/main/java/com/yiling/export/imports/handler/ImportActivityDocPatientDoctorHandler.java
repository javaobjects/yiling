package com.yiling.export.imports.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.yiling.export.excel.handler.BaseImportHandler;
import com.yiling.export.imports.model.ImportActivityDocPatientDoctorExcel;
import com.yiling.hmc.activity.api.HMCActivityApi;
import com.yiling.hmc.activity.dto.ActivityDocToPatientDTO;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.DoctorAppInfoDTO;
import com.yiling.ih.user.dto.HmcActivityImportDocResDTO;
import com.yiling.ih.user.dto.request.HMCImportActivityDocRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author fan.shen
 * @类名 ImportActivityDocPatientDoctorExcel
 * @描述 导入活动医生
 * @创建时间 2023-02-03
 * @修改人 fan.shen
 * @修改时间 2023-02-03
 **/
@Component
@Slf4j
public class ImportActivityDocPatientDoctorHandler extends BaseImportHandler<ImportActivityDocPatientDoctorExcel> {

    @DubboReference
    HMCActivityApi hmcActivityApi;

    @DubboReference
    DoctorApi doctorApi;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportActivityDocPatientDoctorExcel excel) {
        log.info("[ImportActivityDocPatientDoctorHandler.verifyHandler]....");
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

        // 获取活动信息
        ActivityDocToPatientDTO activityDocToPatientDTO = hmcActivityApi.queryActivityById(excel.getId());
        if (Objects.isNull(activityDocToPatientDTO)) {
            return this.error("根据活动ID未获取到活动信息");
        }

        // 获取医生信息
        DoctorAppInfoDTO doctor = doctorApi.getDoctorInfoByDoctorId(excel.getDoctorId());
        if (Objects.isNull(doctor)) {
            return this.error("根据医生ID未获取到医生信息");
        }
        return result;
    }

    @Override
    public List<ImportActivityDocPatientDoctorExcel> execute(List<ImportActivityDocPatientDoctorExcel> list, Map<String, Object> paramMap) {
        log.info("[ImportActivityDocPatientDoctorHandler.execute]....");
        List<HMCImportActivityDocRequest> importList = Lists.newArrayList();
        for (int i = 0; i < list.size(); i++) {
            String qrCodeUrl = hmcActivityApi.getQrCodeUrl(list.get(i).getId(), list.get(i).getDoctorId());
            HMCImportActivityDocRequest request = new HMCImportActivityDocRequest();
            request.setQrcodeUrl(qrCodeUrl);
            request.setDoctorId(list.get(i).getDoctorId());
            request.setActivityId(list.get(i).getId());
            request.setIndex(list.get(i).getRowNum());
            importList.add(request);
        }

        List<HmcActivityImportDocResDTO> importDocRes = doctorApi.importActivityDoc(importList);
        log.info("[ImportActivityDocPatientDoctorHandler.execute]....导入参数：{},返回结果:{}", JSONUtil.toJsonStr(importList), JSONUtil.toJsonStr(importDocRes));
        if (CollUtil.isNotEmpty(importDocRes)) {
            for (ImportActivityDocPatientDoctorExcel form : list) {
                Optional<HmcActivityImportDocResDTO> first = importDocRes.stream().filter(item -> item.getIndex().equals(form.getRowNum())).findFirst();
                if (first.isPresent()) {
                    form.setErrorMsg(first.get().getFailureReason());
                    form.setStatus("失败");
                }
            }
        }
        log.info("[ImportActivityDocPatientDoctorHandler.execute]....结果list:{}", JSONUtil.toJsonStr(list));
        return list;
    }
}
