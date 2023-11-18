package com.yiling.sjms.gb.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.sjms.gb.entity.GbBaseInfoDO;

/**
 * <p>
 * 团购基本信息 服务类
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */
public interface GbBaseInfoService extends BaseService<GbBaseInfoDO> {

    /**
     * 根据表单id获取
     * @param gbId
     * @return
     */
    GbBaseInfoDO getOneByGbId(Long gbId);

    /**
     * 根据表单id获取
     * @param gbIds
     * @return
     */
    List<GbBaseInfoDO> listByGbIds(List<Long> gbIds);
}
