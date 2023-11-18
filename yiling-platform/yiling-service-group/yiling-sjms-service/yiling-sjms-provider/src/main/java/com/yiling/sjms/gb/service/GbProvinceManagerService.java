package com.yiling.sjms.gb.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.sjms.gb.entity.GbProvinceManagerDO;
import com.yiling.user.esb.bo.SimpleEsbEmployeeInfoBO;
import com.yiling.user.esb.dto.request.QueryProvinceManagerRequest;

/**
 * <p>
 * 省区负责人关系 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-11-28
 */
public interface GbProvinceManagerService extends BaseService<GbProvinceManagerDO> {

    /**
     * 获取市场运营部对应省区的省区经理信息
     *
     * @param request
     * @return com.yiling.user.esb.bo.SimpleEsbEmployeeInfoBO
     * @author xuan.zhou
     * @date 2022/11/28
     **/
    SimpleEsbEmployeeInfoBO getByProvinceName(QueryProvinceManagerRequest request);
}
