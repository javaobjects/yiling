package com.yiling.sjms.gb.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.sjms.gb.dto.request.UpdateMainInfoReviewTypeRequest;
import com.yiling.sjms.gb.entity.GbMainInfoDO;

/**
 * <p>
 * 团购信息 服务类
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */
public interface GbMainInfoService extends BaseService<GbMainInfoDO> {
    /**
     * 根据表单id获取
     * @param gbId
     * @return
     */
    GbMainInfoDO getOneByGbId(Long gbId);

    /**
     * 根据表单id获取
     * @param gbIds
     * @return
     */
    List<GbMainInfoDO> listByGbIds(List<Long> gbIds);

    /**
     *根据表单id修改核实团购性质和是否地级市下机构
     * @param request
     * @return
     */
    Boolean updateByGbId(UpdateMainInfoReviewTypeRequest request);
}
