package com.yiling.export.export.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmHospitalDrugstoreRelationApi;
import com.yiling.dataflow.crm.dto.CrmHospitalDrugstoreRelationDTO;
import com.yiling.dataflow.crm.dto.request.QueryHospitalDrugstoreRelationPageRequest;
import com.yiling.dataflow.crm.enums.CrmDrugstoreRelStatusEnum;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.model.CrmHospitalDrugstoreRelationModel;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/5/31
 */
@Slf4j
@Service("crmHospitalDrugstoreRelExportService")
public class CrmHospitalDrugstoreRelExportServiceImpl implements BaseExportQueryDataService<QueryHospitalDrugstoreRelationPageRequest> {

    @DubboReference
    private CrmHospitalDrugstoreRelationApi crmHospitalDrugstoreRelationApi;

    @DubboReference
    private UserApi userApi;

    @Override
    public QueryExportDataDTO queryData(QueryHospitalDrugstoreRelationPageRequest queryHospitalDrugstoreRelationPageRequest) {
        return null;
    }

    @Override
    public QueryHospitalDrugstoreRelationPageRequest getParam(Map<String, Object> map) {
        QueryHospitalDrugstoreRelationPageRequest request = PojoUtils.map(map, QueryHospitalDrugstoreRelationPageRequest.class);
        return request;
    }

    @Override
    public String getFileSuffix() {
        return "xlsx";
    }

    @Override
    public boolean isReturnData() {
        return false;
    }

    @Override
    public byte[] getExportByte(QueryHospitalDrugstoreRelationPageRequest request, String fileName) {
        if (request == null) {
            return null;
        }

        String tmpDir = FileUtil.getTmpDirPath() + File.separator + System.currentTimeMillis();
        String excelFileName = tmpDir + File.separator + fileName;
        File filePath = new File(tmpDir);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        ExcelWriter excelWriter = EasyExcel.write(excelFileName, CrmHospitalDrugstoreRelationModel.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("sheet1").build();

        int current = 1;
        int size = 2000;

        try {
            Map<Long, String> userNameMap = new HashMap<>();

            while (true) {
                request.setCurrent(current);
                request.setSize(size);
                Page<CrmHospitalDrugstoreRelationDTO> pageResult = crmHospitalDrugstoreRelationApi.listPage(request);

                List<CrmHospitalDrugstoreRelationModel> list = new ArrayList<>();
                for (CrmHospitalDrugstoreRelationDTO relationDTO : pageResult.getRecords()) {
                    CrmHospitalDrugstoreRelationModel model = PojoUtils.map(relationDTO, CrmHospitalDrugstoreRelationModel.class);

                    // 设置状态
                    if (model.getDisableFlag() == 1) {
                        model.setStatusDesc(CrmDrugstoreRelStatusEnum.DISABLE.getName());
                    } else {
                        Date now = DateUtil.beginOfDay(new Date());
                        if (now.before(model.getEffectStartTime())) {
                            model.setStatusDesc(CrmDrugstoreRelStatusEnum.NOT_EFFECT.getName());
                        } else if (DateUtil.compare(now, model.getEffectStartTime()) >= 0 && DateUtil.compare(now, model.getEffectEndTime()) <= 0) {
                            model.setStatusDesc(CrmDrugstoreRelStatusEnum.EFFECTING.getName());
                        } else if (now.after(model.getEffectEndTime())) {
                            model.setStatusDesc(CrmDrugstoreRelStatusEnum.EXPIRED.getName());
                        }
                    }

                    //  获取操作人姓名
                    if (model.getLastOpUser() != null && model.getLastOpUser() != 0) {
                        if (!userNameMap.containsKey(model.getLastOpUser())) {
                            UserDTO userDTO = userApi.getById(model.getLastOpUser());
                            userNameMap.put(model.getLastOpUser(), userDTO.getName());
                        }
                        String name = userNameMap.get(model.getLastOpUser());
                        if (StringUtils.isNotEmpty(name) && model.getDataSource() == 2) {
                            name = name + "（申请人）";
                        }
                        model.setLastOpUserName(name);
                    }
                    list.add(model);
                }

                // 写文件
                excelWriter.write(list, writeSheet);

                if (CollUtil.isEmpty(list)) {
                    break;
                }
                if (list.size() < size) {
                    break;
                }
                current++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }

        try {
            File file = new File(excelFileName);
            if (file.exists()) {
                return  FileUtil.readBytes(file);
            }
        } finally {
            FileUtil.del(tmpDir);
        }
        return null;
    }


}
