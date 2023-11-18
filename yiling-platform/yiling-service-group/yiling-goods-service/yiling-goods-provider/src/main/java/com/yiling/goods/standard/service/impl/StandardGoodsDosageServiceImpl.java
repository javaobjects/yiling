package com.yiling.goods.standard.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.goods.standard.dao.StandardGoodsDosageMapper;
import com.yiling.goods.standard.entity.StandardGoodsDosageDO;
import com.yiling.goods.standard.service.StandardGoodsDosageService;

/**
 * <p>
 * 商品剂型标准库 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Service
public class StandardGoodsDosageServiceImpl extends BaseServiceImpl<StandardGoodsDosageMapper, StandardGoodsDosageDO> implements StandardGoodsDosageService {

    /**
     * 获取第一级剂型
     *
     * @return
     */
    @Override
    public List<StandardGoodsDosageDO> getFirstDosage() {
        QueryWrapper<StandardGoodsDosageDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardGoodsDosageDO :: getGdfParentId,0);
        return this.list(wrapper);
    }
}
