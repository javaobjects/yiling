package com.yiling.bi.company.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.bi.company.entity.DimLsflChaincompanyDO;
import com.yiling.bi.company.dao.DimLsflChaincompanyMapper;
import com.yiling.bi.company.service.DimLsflChaincompanyService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-30
 */
@Service
public class DimLsflChaincompanyServiceImpl extends BaseServiceImpl<DimLsflChaincompanyMapper, DimLsflChaincompanyDO> implements DimLsflChaincompanyService {

    @Override
    public List<DimLsflChaincompanyDO> getByDbCodeAndChainCode(String dbCode, String chainCode) {
        LambdaQueryWrapper<DimLsflChaincompanyDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DimLsflChaincompanyDO::getDbCode, dbCode);
        wrapper.eq(DimLsflChaincompanyDO::getChainCode, chainCode);
        wrapper.ne(DimLsflChaincompanyDO::getDelLevel, "1");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<DimLsflChaincompanyDO> getByChainCode(String chainCode) {
        LambdaQueryWrapper<DimLsflChaincompanyDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DimLsflChaincompanyDO::getChainCode, chainCode);
        wrapper.ne(DimLsflChaincompanyDO::getDelLevel, "1");
        return baseMapper.selectList(wrapper);
    }
}
