package com.yiling.user.member.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.member.bo.EnterpriseMemberBO;
import com.yiling.user.member.dto.request.QueryEnterpriseMemberRequest;
import com.yiling.user.member.entity.EnterpriseMemberDO;

/**
 * <p>
 * B2B企业会员表 Dao 接口
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/25
 */
@Repository
public interface EnterpriseMemberMapper extends BaseMapper<EnterpriseMemberDO> {

    /**
     * 查询企业会员分页列表
     *
     * @param page
     * @param request
     * @return
     */
    Page<EnterpriseMemberBO> queryEnterpriseMemberPage(Page page, @Param("request") QueryEnterpriseMemberRequest request);

}
