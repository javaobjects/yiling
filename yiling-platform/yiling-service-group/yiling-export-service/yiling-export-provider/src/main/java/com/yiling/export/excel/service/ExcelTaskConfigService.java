package com.yiling.export.excel.service;

import com.yiling.export.excel.dto.ExcelTaskConfigDTO;
import com.yiling.export.excel.entity.ExcelTaskConfigDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-17
 */
public interface ExcelTaskConfigService extends BaseService<ExcelTaskConfigDO> {

    ExcelTaskConfigDTO findExcelTaskConfigByCode(String code);

    ExcelTaskConfigDTO findExcelTaskConfigById(Long id);
}
