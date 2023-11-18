package com.yiling.export.excel.dao;

import com.yiling.export.excel.entity.ExcelTaskRecordDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 导入日志表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-17
 */
@Repository
public interface ExcelTaskRecordMapper extends BaseMapper<ExcelTaskRecordDO> {

        Integer updateExcelTaskRecord(@Param("id")Long id,@Param("oldStatus")Integer oldStatus,@Param("status")Integer status);
}
