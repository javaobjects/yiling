package com.yiling.user.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.system.entity.UserAttachmentDO;

/**
 * <p>
 * 用户相关附件表 Dao 接口
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-01-21
 */
@Repository
public interface UserAttachmentMapper extends BaseMapper<UserAttachmentDO> {

    int batchInsert(@Param("list") List<UserAttachmentDO> list);
}
