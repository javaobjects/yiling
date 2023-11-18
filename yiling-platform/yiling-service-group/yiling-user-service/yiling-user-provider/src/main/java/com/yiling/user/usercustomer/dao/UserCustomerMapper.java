package com.yiling.user.usercustomer.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.usercustomer.dto.UserCustomerDTO;
import com.yiling.user.usercustomer.dto.request.QueryUserCustomerRequest;
import com.yiling.user.usercustomer.entity.UserCustomerDO;

/**
 * 销售助手-客户管理Dao
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/17
 */
@Repository
public interface UserCustomerMapper extends BaseMapper<UserCustomerDO> {

    /**
     * 分页列表
     *
     * @param page
     * @param request
     * @return
     */
    Page<UserCustomerDTO> queryPageList(Page page, @Param("request") QueryUserCustomerRequest request);

    /**
     * 列表查询
     *
     * @param request
     * @return
     */
    List<UserCustomerDTO> queryPageList(@Param("request") QueryUserCustomerRequest request);

}
