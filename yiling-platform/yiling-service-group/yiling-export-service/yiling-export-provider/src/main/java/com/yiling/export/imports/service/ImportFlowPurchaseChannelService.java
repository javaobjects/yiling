package com.yiling.export.imports.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.excel.util.ExeclImportUtils;
import com.yiling.export.imports.handler.ImportFlowPurchaseChannelHandler;
import com.yiling.export.imports.model.ImportFlowPurchaseChannelModel;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 ImportFlowPurchaseChannelService
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
@Slf4j
@Service("importFlowPurchaseChannelService")
public class ImportFlowPurchaseChannelService implements BaseExcelImportService {

    @DubboReference
    private SjmsUserDatascopeApi sjmsUserDatascopeApi;

    @Autowired
    private ImportFlowPurchaseChannelHandler importFlowPurchaseChannelHandler;

    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        JSONObject jsonParam = JSONUtil.parseObj(param.get("param"));
        String currentUserCode = jsonParam.getOrDefault(CurrentSjmsUserInfo.CURRENT_USER_CODE,"").toString();
        if(StringUtils.isEmpty(currentUserCode)){
            throw new BusinessException(ResultCode.FORBIDDEN,"操作人工号为空");
        }
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(currentUserCode);
        log.info("采购渠道导入:userDatascopeBO={}",userDatascopeBO);
        importFlowPurchaseChannelHandler.setUserDatascopeBO(userDatascopeBO);
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setVerifyHandler(importFlowPurchaseChannelHandler);
        params.setKeyIndex(0);
        params.setReadRows(readRows);
        return ExeclImportUtils.importExcelMore(inputstream, ImportFlowPurchaseChannelModel.class, params, importFlowPurchaseChannelHandler, param);
    }
}
