package com.yiling.goods.standard.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.standard.dto.StandardInstructionsHealthDTO;
import com.yiling.goods.standard.entity.StandardInstructionsHealthDO;

/**
 * <p>
 * 保健食品说明书 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
public interface StandardInstructionsHealthService extends BaseService<StandardInstructionsHealthDO> {

    /**
     * 根据StandardId找到保健食品说明书
     *
     * @param standardId
     * @return
     */
    StandardInstructionsHealthDO getInstructionsHealthByStandardId(Long standardId);

    /**
     * 根据StandardIds找到保健食品说明书
     * @param standardIds
     * @return
     */
    List<StandardInstructionsHealthDTO> getInstructionsHealthByStandardIdList(List<Long> standardIds);

    /**
     * 保存保健食品说明书信息
     *
     * @param one
     * @return
     */
    Boolean saveInstructionsHealthOne(StandardInstructionsHealthDO one );
}
