package com.yiling.user.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.system.entity.UserSalesAreaDetailDO;

/**
 * <p>
 * 用户销售区域详情 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-09-28
 */
@Repository
public interface UserSalesAreaDetailMapper extends BaseMapper<UserSalesAreaDetailDO> {

    /**
     * 添加用户销售区域
     *
     * @param list
     * @return
     */
    int addUserSalesAreaDetail(@Param("list") List<UserSalesAreaDetailDO> list);
}
