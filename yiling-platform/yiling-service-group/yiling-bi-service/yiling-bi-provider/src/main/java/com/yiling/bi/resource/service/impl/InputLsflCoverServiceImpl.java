package com.yiling.bi.resource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.bi.resource.entity.InputLsflCoverDO;
import com.yiling.bi.resource.dao.InputLsflCoverMapper;
import com.yiling.bi.resource.entity.InputLsflRecordDO;
import com.yiling.bi.resource.service.InputLsflCoverService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-10-09
 */
@Service
public class InputLsflCoverServiceImpl extends BaseServiceImpl<InputLsflCoverMapper, InputLsflCoverDO> implements InputLsflCoverService {

    @Override
    public InputLsflCoverDO getByNameAndGoodsAndYear(String name, String goods, String year) {
        QueryWrapper<InputLsflCoverDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InputLsflCoverDO::getBzCode,name).eq(InputLsflCoverDO::getWlBreed,goods).eq(InputLsflCoverDO::getDyear,year);
        return this.getOne(queryWrapper);
    }
}
