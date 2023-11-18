package com.yiling.open.ftp.api;

import java.util.Date;
import java.util.List;

import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.ftp.dto.LocalCompareDTO;

/**
 * @author: shuang.zhang
 * @date: 2022/3/4
 */
public interface FlowFtpApi {
    /**
     * 读取excel流向
     */
    void readFlowExcel();

    /**
     * 数据缓存对比
     * @param localCompareDTO
     */
    void localCompare(LocalCompareDTO localCompareDTO);
}
