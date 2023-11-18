package com.yiling.bi.resource.service;

import com.yiling.bi.resource.entity.InputLsflCoverDO;
import com.yiling.bi.resource.entity.InputLsflRecordDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-10-09
 */
public interface InputLsflCoverService extends BaseService<InputLsflCoverDO> {

    InputLsflCoverDO getByNameAndGoodsAndYear(String name, String goods, String year);
}
