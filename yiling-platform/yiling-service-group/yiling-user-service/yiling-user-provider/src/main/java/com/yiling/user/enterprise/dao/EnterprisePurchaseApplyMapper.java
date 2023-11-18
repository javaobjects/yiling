package com.yiling.user.enterprise.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.enterprise.bo.EnterprisePurchaseApplyBO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePurchaseApplyPageRequest;
import com.yiling.user.enterprise.entity.EnterprisePurchaseApplyDO;

/**
 * <p>
 * 企业采购申请表 Dao 接口
 * </p>
 *
 * @author lun.yu
 * @date 2022-01-17
 */
@Repository
public interface EnterprisePurchaseApplyMapper extends BaseMapper<EnterprisePurchaseApplyDO> {

    /**
     * 企业采购申请分页列表
     *
     * @param page
     * @param request
     * @return
     */
    Page<EnterprisePurchaseApplyBO> pageList(Page page, @Param("request") QueryEnterprisePurchaseApplyPageRequest request);

    /**
     * 获取采购商的 企业关系申请信息
     * @param customerEid
     * @param eid
     * @return
     */
    EnterprisePurchaseApplyBO getByCustomerEid(@Param("customerEid") Long customerEid,@Param("eid") Long eid);

    /**
     * 获取供应商的 企业关系申请信息
     * @param customerEid
     * @param eid
     * @return
     */
    EnterprisePurchaseApplyBO getByEid(@Param("customerEid") Long customerEid,@Param("eid") Long eid);

}
