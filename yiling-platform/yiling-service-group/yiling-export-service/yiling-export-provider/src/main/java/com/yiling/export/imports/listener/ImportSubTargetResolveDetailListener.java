package com.yiling.export.imports.listener;


import com.yiling.dataflow.sale.api.SaleDepartmentSubTargetResolveDetailApi;
import com.yiling.dataflow.sale.dto.request.ImportSubTargetResolveDetailRequest;
import com.yiling.export.excel.listener.AbstractImportReaderListener;
import com.yiling.export.excel.handler.EasyExcelVerifyHandlerResult;
import com.yiling.export.imports.listener.model.ImportSubTargetResolveDetailData;
import com.yiling.framework.common.util.PojoUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class ImportSubTargetResolveDetailListener extends AbstractImportReaderListener<ImportSubTargetResolveDetailData> {
    private SaleDepartmentSubTargetResolveDetailApi saleDepartmentSubTargetResolveDetailApi;

    public SaleDepartmentSubTargetResolveDetailApi getSaleDepartmentSubTargetResolveDetailApi() {
        return saleDepartmentSubTargetResolveDetailApi;
    }

    public void setSaleDepartmentSubTargetResolveDetailApi(SaleDepartmentSubTargetResolveDetailApi saleDepartmentSubTargetResolveDetailApi) {
        this.saleDepartmentSubTargetResolveDetailApi = saleDepartmentSubTargetResolveDetailApi;
    }

    @Override
    public void saveData(Map<String, Object> paramMap) {
        List<ImportSubTargetResolveDetailRequest> request = PojoUtils.map(cachedDataList, ImportSubTargetResolveDetailRequest.class);
        saleDepartmentSubTargetResolveDetailApi.importSubTargetResolveDetail(request);
    }


    @Override
    protected EasyExcelVerifyHandlerResult verify(ImportSubTargetResolveDetailData model) {
        return null;
    }

}
