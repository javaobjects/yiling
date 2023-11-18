package com.yiling.sjms.flee.dao;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.flee.bo.SalesAppealFormBO;
import com.yiling.sjms.flee.dto.request.QuerySalesAppealPageRequest;
import com.yiling.sjms.flee.entity.SalesAppealFormDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 销量申诉表单 Dao 接口
 * </p>
 *
 * @author shixing.sun
 * @date 2023-03-14
 */
@Repository
public interface SalesAppealFormMapper extends BaseMapper<SalesAppealFormDO> {

    Page<SalesAppealFormBO> pageForm(Page<Object> objectPage,@Param("request") QuerySalesAppealPageRequest request);
}
