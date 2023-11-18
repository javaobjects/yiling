package com.yiling.user.enterprise.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.enterprise.entity.EnterpriseTagRelDO;

/**
 * <p>
 * 企业关联的标签信息 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-10-14
 */
@Repository
public interface EnterpriseTagRelMapper extends BaseMapper<EnterpriseTagRelDO> {

    /**
     * 批量添加企业标签信息
     *
     * @param list
     * @return
     */
    int addEnterpriseTags(@Param("list") List<EnterpriseTagRelDO> list);

}
