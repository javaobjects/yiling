package com.yiling.user.enterprise.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.enterprise.bo.EnterpriseStatisticsBO;
import com.yiling.user.enterprise.dto.request.QueryContactorEnterprisePageListRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.entity.EnterpriseDO;

/**
 * <p>
 * 企业信息表 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-18
 */
@Repository
public interface EnterpriseMapper extends BaseMapper<EnterpriseDO> {

    /**
     * 企业数量统计
     *
     * @return
     */
    EnterpriseStatisticsBO quantityStatistics();

    /**
     * 企业分页列表
     *
     * @param page
     * @param request
     * @return
     */
    Page<EnterpriseDO> pageList(Page page, @Param("request") QueryEnterprisePageListRequest request);

    /**
     * 企业列表（不分页）
     *
     * @param request
     * @return
     */
    List<EnterpriseDO> pageList(@Param("request") QueryEnterprisePageListRequest request);


    /**
     * 查询商务联系人，下面的企业客户信息(按照下单时间)
     * @param page
     * @param request
     * @return
     */
    Page<EnterpriseDO> myCustomerPageList(Page page, @Param("request") QueryContactorEnterprisePageListRequest request);


}
