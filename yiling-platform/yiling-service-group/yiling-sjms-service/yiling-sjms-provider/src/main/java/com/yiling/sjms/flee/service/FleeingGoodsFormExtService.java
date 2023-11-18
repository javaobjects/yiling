package com.yiling.sjms.flee.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.sjms.flee.entity.FleeingGoodsFormExtDO;

/**
 * <p>
 * 窜货申诉拓展表单 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2023-03-14
 */
public interface FleeingGoodsFormExtService extends BaseService<FleeingGoodsFormExtDO> {

    /**
     * 根据formId查询附件信息
     *
     * @param formId 主表单id
     * @return 附件信息
     */
    FleeingGoodsFormExtDO queryExtByFormId(Long formId);
}
