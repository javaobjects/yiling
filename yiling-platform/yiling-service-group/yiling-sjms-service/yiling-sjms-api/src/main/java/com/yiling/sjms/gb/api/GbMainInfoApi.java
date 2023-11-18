package com.yiling.sjms.gb.api;

import java.util.List;

import com.yiling.sjms.gb.dto.MainInfoDTO;
import com.yiling.sjms.gb.dto.request.UpdateMainInfoReviewTypeRequest;

/**
 * 团购主要信息
 *
 * @author: wei.wang
 * @date: 2022/11/29
 */
public interface GbMainInfoApi {
    /**
     * 根据表单id获取
     * @param gbId
     * @return
     */
    MainInfoDTO getOneByGbId(Long gbId);

    /**
     * 根据表单id获取
     * @param gbIds
     * @return
     */
    List<MainInfoDTO> listByGbIds(List<Long> gbIds);

    /**
     *修改运营部核实属性
     * @param request
     * @return
     */
    Boolean updateByGbId(UpdateMainInfoReviewTypeRequest request);
}
