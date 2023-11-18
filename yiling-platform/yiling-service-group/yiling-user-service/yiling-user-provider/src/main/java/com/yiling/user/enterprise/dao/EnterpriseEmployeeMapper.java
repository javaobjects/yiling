package com.yiling.user.enterprise.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.request.QueryEmployeePageListRequest;
import com.yiling.user.enterprise.entity.EnterpriseEmployeeDO;
import com.yiling.user.system.bo.MrBO;
import com.yiling.user.system.dto.request.QueryMrPageListRequest;

/**
 * <p>
 * 企业员工信息 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-26
 */
@Repository
public interface EnterpriseEmployeeMapper extends BaseMapper<EnterpriseEmployeeDO> {

    /**
     * 分页列表
     * @param page
     * @param request
     * @return
     */
    Page<EnterpriseEmployeeDTO> pageList(Page page, @Param("request") QueryEmployeePageListRequest request);

    /**
     * 查询企业医药代表分页列表
     *
     * @param page
     * @param request
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.yiling.user.system.bo.MrBO>
     * @author xuan.zhou
     * @date 2022/6/7
     **/
    Page<MrBO> mrPageList(Page page, @Param("request") QueryMrPageListRequest request);
}
