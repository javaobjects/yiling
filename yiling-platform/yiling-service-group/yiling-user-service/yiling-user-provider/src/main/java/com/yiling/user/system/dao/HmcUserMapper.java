package com.yiling.user.system.dao;

import java.util.List;
import java.util.Map;

import com.yiling.user.system.dto.request.QueryActivityDoctorUserCountRequest;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.system.bo.HmcUser;
import com.yiling.user.system.dto.request.QueryHmcUserPageListRequest;
import com.yiling.user.system.entity.HmcUserDO;

/**
 * <p>
 * 健康管理中心用户表 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-03-24
 */
@Repository
public interface HmcUserMapper extends BaseMapper<HmcUserDO> {

    /**
     * 用户分页列表
     *
     * @param page
     * @param request
     * @return
     */
    Page<HmcUser> pageList(Page page, @Param("request") QueryHmcUserPageListRequest request);

    /**
     * 批量根据ID获取用户信息
     *
     * @param ids 用户ID列表
     * @return
     */
    List<HmcUser> listByIds(@Param("ids") List<Long> ids);

    /**
     * 更新用户登录次数、最后登录时间
     *
     * @param userId 用户ID
     * @return int
     * @author xuan.zhou
     * @date 2022/3/28
     **/
    int updateLoginTime(@Param("userId") Long userId);

    /**
     * 查询指定活动类型下每个医生邀请的用户数量
     * @param request
     * @return
     */
    @MapKey("doctorId")
    List<Map<String, Long>> queryActivityDoctorInviteUserCount(@Param("request") QueryActivityDoctorUserCountRequest request);
}
