package com.yiling.dataflow.crm.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationManorDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorPageRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationManorDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 辖区机构品类关系 Dao 接口
 * </p>
 *
 * @author xueli.ji
 * @date 2023-05-09
 */
@Repository
public interface CrmEnterpriseRelationManorMapper extends BaseMapper<CrmEnterpriseRelationManorDO> {

    Page<CrmEnterpriseRelationManorDTO> pageExportList(@Param("page") Page<Object> page,@Param("request") QueryCrmManorPageRequest request);
}
