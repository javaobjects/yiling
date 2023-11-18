package com.yiling.export.sale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.sale.dto.request.QueryResolveDetailPageRequest;
import com.yiling.export.BaseTest;
import com.yiling.export.export.service.impl.TargetResolveDetailExportImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2023/5/11
 */
@Slf4j
public class SaleTargetResolveTest extends BaseTest {

    @Autowired
    private TargetResolveDetailExportImpl targetResolveDetailExport;

    @Test
    public void test(){
        QueryResolveDetailPageRequest request = new QueryResolveDetailPageRequest();
        request.setDepartId(13033L).setSaleTargetId(39L);
        targetResolveDetailExport.genTemplate(request);
    }
}