package com.yiling.bi.resource.service;

import com.yiling.bi.resource.entity.InputLsflRecordDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 零售部计算返利对应的备案表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-09-22
 */
public interface InputLsflRecordService extends BaseService<InputLsflRecordDO> {

    InputLsflRecordDO getByNameAndGoodsAndYear(String name,String goods,String year);
}
