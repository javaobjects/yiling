package com.yiling.cms.content.dao;

import com.yiling.cms.content.entity.QaDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 问答表 Dao 接口
 * </p>
 *
 * @author fan.shen
 * @date 2023-03-13
 */
@Repository
public interface QaMapper extends BaseMapper<QaDO> {

    Integer getQaCount(@Param("contentId") Long contentId);
}
