package com.yiling.user.enterprise.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.enterprise.entity.EnterpriseSalesAreaDetailDO;

/**
 * <p>
 * 企业销售区域详情 Dao 接口
 * </p>
 *
 * @author zhouxuan
 * @date 2021-10-29
 */
@Repository
public interface EnterpriseSalesAreaDetailMapper extends BaseMapper<EnterpriseSalesAreaDetailDO> {

    /**
     * 新增企业区域详情
     * @param list
     * @return
     */
    int addEnterpriseAreaCodeList(List<EnterpriseSalesAreaDetailDO> list);

}
