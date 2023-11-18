package com.yiling.export.imports.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.wash.api.FlowInTransitOrderApi;
import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.excel.util.ExeclImportUtils;
import com.yiling.export.imports.handler.ImportFlowGoodsBatchTerminalHandler;
import com.yiling.export.imports.model.ImportFlowGoodsBatchTerminalModel;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 终端库存导入
 *
 * @author: houjie.sun
 * @date: 2023/3/10
 */
@Slf4j
@Service("importFlowGoodsBatchTerminalService")
public class ImportFlowGoodsBatchTerminalService implements BaseExcelImportService {

    @Autowired
    private ImportFlowGoodsBatchTerminalHandler importFlowGoodsBatchTerminalHandler;

    @DubboReference
    SjmsUserDatascopeApi sjmsUserDatascopeApi;
    @DubboReference
    FlowInTransitOrderApi flowInTransitOrderApi;

    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        JSONObject jsonParam = JSONUtil.parseObj(param.get("param"));
        String currentUserCode = jsonParam.getOrDefault(CurrentSjmsUserInfo.CURRENT_USER_CODE,"").toString();
        if(StrUtil.isBlank(currentUserCode)){
            throw new BusinessException(ResultCode.FORBIDDEN,"操作人工号为空");
        }

        // 数据权限
        Map<String, List<String>> map = flowInTransitOrderApi.buildUserDatascope(currentUserCode, "终端库存-导入");
        String datascopeType = map.get("datascopeType").get(0);
        if (ObjectUtil.equal("1", datascopeType)) {
            List<Long> crmIdList = Convert.toList(Long.class, map.get("crmIds"));
            List<String> crmProvinceCodeList = map.get("provinceCodes");
            importFlowGoodsBatchTerminalHandler.setCrmIds(crmIdList);
            importFlowGoodsBatchTerminalHandler.setCrmProvinceCodeList(crmProvinceCodeList);
        }



        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setVerifyHandler(importFlowGoodsBatchTerminalHandler);
        params.setKeyIndex(0);
        params.setReadRows(readRows);

        return ExeclImportUtils.importExcelMore(inputstream, ImportFlowGoodsBatchTerminalModel.class, params, importFlowGoodsBatchTerminalHandler, param);
    }
}
