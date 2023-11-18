package com.yiling.export.imports.listener;

import java.util.Map;

import com.yiling.export.excel.handler.EasyExcelVerifyHandlerResult;
import com.yiling.export.excel.listener.AbstractImportReaderListener;
import com.yiling.export.imports.listener.model.DemoTestModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoTestListener extends AbstractImportReaderListener<DemoTestModel> {

    @Override
   public void saveData(Map<String, Object> paramMap ) {
       log.info("{}条数据，开始存储数据库！",cachedDataList.size());
        log.info("存储数据库成功！");
    }

    @Override
    protected EasyExcelVerifyHandlerResult verify(DemoTestModel model) {
        return null;
    }
}
