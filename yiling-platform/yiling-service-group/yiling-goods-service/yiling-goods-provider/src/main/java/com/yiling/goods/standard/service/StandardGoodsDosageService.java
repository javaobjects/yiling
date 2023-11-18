package com.yiling.goods.standard.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.standard.entity.StandardGoodsDosageDO;

/**
 * <p>
 * 商品剂型标准库 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
public interface StandardGoodsDosageService extends BaseService<StandardGoodsDosageDO> {

    /**
     * 获取第一级剂型
     *
     * @return
     */
    List<StandardGoodsDosageDO> getFirstDosage();

}
