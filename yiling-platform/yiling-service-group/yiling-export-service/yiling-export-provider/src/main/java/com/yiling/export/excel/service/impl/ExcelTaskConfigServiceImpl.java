package com.yiling.export.excel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.export.excel.dto.ExcelTaskConfigDTO;
import com.yiling.export.excel.entity.ExcelTaskConfigDO;
import com.yiling.export.excel.dao.ExcelTaskConfigMapper;
import com.yiling.export.excel.service.ExcelTaskConfigService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-17
 */
@Service
public class ExcelTaskConfigServiceImpl extends BaseServiceImpl<ExcelTaskConfigMapper, ExcelTaskConfigDO> implements ExcelTaskConfigService {

    @Override
    public ExcelTaskConfigDTO findExcelTaskConfigByCode(String code) {
        QueryWrapper<ExcelTaskConfigDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExcelTaskConfigDO::getExcelCode, code);
        return PojoUtils.map(this.getOne(queryWrapper),ExcelTaskConfigDTO.class);
    }

    @Override
    public ExcelTaskConfigDTO findExcelTaskConfigById(Long id) {
        return PojoUtils.map(this.getById(id),ExcelTaskConfigDTO.class);
    }
}
