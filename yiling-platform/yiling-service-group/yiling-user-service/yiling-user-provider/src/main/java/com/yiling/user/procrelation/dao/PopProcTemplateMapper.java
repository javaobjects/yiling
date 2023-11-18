package com.yiling.user.procrelation.dao;

import com.yiling.user.procrelation.entity.PopProcTemplateDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * pop采购关系导入商品模板表 Dao 接口
 * </p>
 *
 * @author dexi.yao
 * @date 2023-06-19
 */
@Repository
public interface PopProcTemplateMapper extends BaseMapper<PopProcTemplateDO> {

    Long queryAutoIncrement(@Param("dbName") String dbName);
}
