package com.yiling.sjms.gb.api;

import java.util.List;

import com.yiling.sjms.gb.dto.BaseInfoDTO;

/**
 * 团购基础信息
 *
 * @author: wei.wang
 * @date: 2022/11/28
 */
public interface GbBaseInfoApi {
    /**
     * 根据表单id获取
     * @param gbId
     * @return
     */
    BaseInfoDTO getOneByGbId(Long gbId);

    /**
     * 根据表单id获取
     * @param gbIds
     * @return
     */
    List<BaseInfoDTO> listByGbIds(List<Long> gbIds);
}
