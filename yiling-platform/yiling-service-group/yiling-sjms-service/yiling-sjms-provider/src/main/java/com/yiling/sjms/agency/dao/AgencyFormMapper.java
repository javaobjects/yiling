package com.yiling.sjms.agency.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.sjms.agency.dto.request.QueryAgencyFormPageRequest;
import com.yiling.sjms.agency.entity.AgencyFormDO;

/**
 * <p>
 * 机构新增修改表单 Dao 接口
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-22
 */
@Repository
public interface AgencyFormMapper extends BaseMapper<AgencyFormDO> {

    /**
     * 机构新增修改表单 分页查询
     *
     * @param page 分页信息
     * @param request 查询条件
     * @return 机构新增修改表单信息
     */
    Page<AgencyFormDO> pageList(Page<AgencyFormDO> page, @Param("request") QueryAgencyFormPageRequest request);
}
