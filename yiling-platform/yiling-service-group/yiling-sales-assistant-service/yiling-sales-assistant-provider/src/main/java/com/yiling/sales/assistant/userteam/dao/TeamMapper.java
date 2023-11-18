package com.yiling.sales.assistant.userteam.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.sales.assistant.userteam.entity.TeamDO;

/**
 * 销售助手-团队Dao
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/26
 */
@Repository
public interface TeamMapper extends BaseMapper<TeamDO> {

    /**
     * 减少团队人员数量
     * @param id
     * @param opUserId
     * @return
     */
    int updateReduceMemberNum(@Param("id") Long id , @Param("opUserId") Long opUserId);

    /**
     * 增加团队人员数量
     * @param id
     * @param opUserId
     * @return
     */
    int updateAddMemberNum(@Param("id") Long id , @Param("opUserId") Long opUserId);
}
