package com.yiling.export.imports.service;

import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.excel.util.ExeclImportUtils;
import com.yiling.export.imports.handler.ImportFlowGoodsRelationHandler;
import com.yiling.export.imports.model.ImportFlowGoodsRelationModel;

import cn.afterturn.easypoi.excel.entity.ImportParams;

/**
 * 商家品与以岭品对应关系导入
 *
 * @author: houjie.sun
 * @date: 2022/12/29
 */
@Service("importFlowGoodsRelationService")
public class ImportFlowGoodsRelationService implements BaseExcelImportService {

    @Autowired
    ImportFlowGoodsRelationHandler importFlowGoodsRelationHandler;

    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setVerifyHandler(importFlowGoodsRelationHandler);
        params.setKeyIndex(0);
        params.setReadRows(readRows);

        return ExeclImportUtils.importExcelMore(inputstream, ImportFlowGoodsRelationModel.class, params, importFlowGoodsRelationHandler, param);
    }
}
