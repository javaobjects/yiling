package com.yiling.user.esb;

import com.yiling.user.BaseTest;
import com.yiling.user.esb.service.EsbBackUpService;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

public class EsbBackUpTest extends BaseTest {
    @Resource
    private EsbBackUpService esbBackUpService;
   @Test
    public void backTest(){
       List<String> esb = esbBackUpService.getTableNames("esb");
   }
}
