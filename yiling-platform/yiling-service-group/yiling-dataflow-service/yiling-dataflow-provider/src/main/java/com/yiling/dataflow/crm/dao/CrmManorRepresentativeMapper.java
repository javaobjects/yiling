package com.yiling.dataflow.crm.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.CrmManorRepresentativeDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorRepresentativePageRequest;
import com.yiling.dataflow.crm.entity.CrmManorRepresentativeDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 辖区代表 Dao 接口
 * </p>
 *
 * @author xueli.ji
 * @date 2023-05-12
 */
@Repository
public interface CrmManorRepresentativeMapper extends BaseMapper<CrmManorRepresentativeDO> {

    Page<CrmManorRepresentativeDTO> pageList(Page<CrmManorRepresentativeDTO> page, @Param("request") QueryCrmManorRepresentativePageRequest request);
}
