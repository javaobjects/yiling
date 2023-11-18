package com.yiling.dataflow.spda.service;

import com.yiling.dataflow.spda.dto.SpdaEnterpriseDataDTO;
import com.yiling.dataflow.spda.entity.SpdaEnterpriseDataDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 药监局企业数据 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-01-06
 */
public interface SpdaEnterpriseDataService extends BaseService<SpdaEnterpriseDataDO> {

     SpdaEnterpriseDataDTO getSpdaEnterpriseDataByName(String name);
}
