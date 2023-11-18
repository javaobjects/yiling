package com.yiling.dataflow.sale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.sale.service.SaleDepartmentSubTargetResolveDetailService;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2023/4/20
 */
@Slf4j
public class TargetResolveTest extends BaseTest {
    @Autowired
     FileService fileService;
    @Autowired
    private SaleDepartmentSubTargetResolveDetailService  saleDepartmentSubTargetResolveDetailService;


    @Test
    public void test1(){
        String url = fileService.getUrl("dev/saleTargetResolveFile/2023/04/25/dee641e3244f4db2b3c137814174b5f3.zip", FileTypeEnum.SALE_TARGET_RESOLVE_FILE);
        System.out.println(url);
    }
}