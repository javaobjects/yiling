package com.yiling.dataflow.crm.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsInfoPageRequest;
import com.yiling.dataflow.crm.entity.CrmGoodsInfoDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-03
 */
@Repository
public interface CrmGoodsInfoMapper extends BaseMapper<CrmGoodsInfoDO> {
    /**
     * 商品分页 （弹窗页面用）
     * @param page
     * @param request
     * @return
     */
    Page<CrmGoodsInfoDO> getPopupPage(Page<CrmGoodsInfoDO> page,@Param("request") QueryCrmGoodsInfoPageRequest request);
}
