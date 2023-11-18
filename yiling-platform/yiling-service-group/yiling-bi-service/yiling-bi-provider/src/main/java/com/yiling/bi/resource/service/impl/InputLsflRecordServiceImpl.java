package com.yiling.bi.resource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.bi.resource.entity.InputLsflRecordDO;
import com.yiling.bi.resource.dao.InputLsflRecordMapper;
import com.yiling.bi.resource.entity.UploadResourceLogDO;
import com.yiling.bi.resource.service.InputLsflRecordService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 零售部计算返利对应的备案表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-09-22
 */
@Service
public class InputLsflRecordServiceImpl extends BaseServiceImpl<InputLsflRecordMapper, InputLsflRecordDO> implements InputLsflRecordService {

    @Override
    public InputLsflRecordDO getByNameAndGoodsAndYear(String name, String goods, String year) {
        QueryWrapper<InputLsflRecordDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InputLsflRecordDO::getBzCode,name).eq(InputLsflRecordDO::getWlCode,goods).eq(InputLsflRecordDO::getDyear,year);
        return this.getOne(queryWrapper);
    }
}
