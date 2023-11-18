package com.yiling.export.imports.service;

import java.io.InputStream;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.excel.util.ExeclImportUtils;
import com.yiling.export.imports.handler.ImportFlowEnterpriseSupplierMappingHandler;
import com.yiling.export.imports.model.ImportFlowEnterpriseCustomerMappingModel;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 ImportFlowEnterpriseSupplierMappingService
 * @描述
 * @创建时间 2023/6/1
 * @修改人 shichen
 * @修改时间 2023/6/1
 **/
@Service("importFlowEnterpriseSupplierMappingService")
@Slf4j
public class ImportFlowEnterpriseSupplierMappingService implements BaseExcelImportService {
    @Autowired
    private ImportFlowEnterpriseSupplierMappingHandler importFlowEnterpriseSupplierMappingHandler;

    @DubboReference
    private FlowMonthWashControlApi flowMonthWashControlApi;

    @DubboReference
    private SjmsUserDatascopeApi sjmsUserDatascopeApi;


    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        JSONObject jsonParam = JSONUtil.parseObj(param.get("param"));
        String currentUserCode = jsonParam.getOrDefault(CurrentSjmsUserInfo.CURRENT_USER_CODE,"").toString();
        if(StringUtils.isEmpty(currentUserCode)){
            throw new BusinessException(ResultCode.FORBIDDEN,"操作人工号为空");
        }
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(currentUserCode);
        log.info("供应商对照导入:userDatascopeBO={}",userDatascopeBO);
        importFlowEnterpriseSupplierMappingHandler.setUserDatascopeBO(userDatascopeBO);
        FlowMonthWashControlDTO controlDTO = flowMonthWashControlApi.getWashStatus();
        if(null == controlDTO){
            throw new BusinessException(ResultCode.FAILED,"未开启月流向清洗，无法操作");
        }
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setVerifyHandler(importFlowEnterpriseSupplierMappingHandler);
        params.setKeyIndex(0);
        params.setReadRows(readRows);

        return ExeclImportUtils.importExcelMore(inputstream, ImportFlowEnterpriseCustomerMappingModel.class, params, importFlowEnterpriseSupplierMappingHandler, param);
    }
}
