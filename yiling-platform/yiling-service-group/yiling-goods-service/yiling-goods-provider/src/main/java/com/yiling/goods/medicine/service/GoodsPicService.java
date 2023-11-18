package com.yiling.goods.medicine.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.medicine.dto.request.SaveGoodsPicRequest;
import com.yiling.goods.medicine.entity.GoodsPicDO;

/**
 * <p>
 * 商品图片表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-15
 */
public interface GoodsPicService extends BaseService<GoodsPicDO> {

    /**
     * 批量添加商品图片信息
     * @param saveGoodsPicRequestList
     * @return
     */
    Boolean insertGoodsPic(List<SaveGoodsPicRequest> saveGoodsPicRequestList,Long goodsId);


}
