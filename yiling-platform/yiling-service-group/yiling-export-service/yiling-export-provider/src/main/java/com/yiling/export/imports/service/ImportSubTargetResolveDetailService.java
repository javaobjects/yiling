package com.yiling.export.imports.service;

import java.io.InputStream;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.sale.api.SaleDepartmentSubTargetResolveDetailApi;
import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.service.BaseExcelImportService;
import com.yiling.export.imports.listener.ImportSubTargetResolveDetailListener;
import com.yiling.export.imports.listener.model.ImportSubTargetResolveDetailData;
import com.yiling.export.imports.listener.model.ImportSubTargetResolveDetailFailModel;
import com.yiling.export.imports.util.EasyExcelUtils;

/**
 * 导入销售指标分解
 *
 * @author: gxl
 * @date: 2023/4/17
 */
@Service
public class ImportSubTargetResolveDetailService implements BaseExcelImportService {
    @DubboReference(timeout = 6000)
    SaleDepartmentSubTargetResolveDetailApi saleDepartmentSubTargetResolveDetailApi;

    @Override
    public ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String, Object> param) throws Exception {
        ImportSubTargetResolveDetailListener listener = new ImportSubTargetResolveDetailListener();
        listener.setSaleDepartmentSubTargetResolveDetailApi(saleDepartmentSubTargetResolveDetailApi);
        return EasyExcelUtils.importExcelMore(inputstream, ImportSubTargetResolveDetailData.class, listener);
    }
}