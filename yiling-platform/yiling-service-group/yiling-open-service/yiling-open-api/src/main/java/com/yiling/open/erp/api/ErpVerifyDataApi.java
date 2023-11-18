package com.yiling.open.erp.api;

import java.util.Map;

/**
 * 任务校验管理
 * @author shuan
 */
public interface ErpVerifyDataApi {

    /**
     * 获取编码获取详情
     * @param suId
     * @param methodNo
     * @return
     */
    Map<String,String> findMd5ByMethodNo(Long suId, String methodNo);
}
