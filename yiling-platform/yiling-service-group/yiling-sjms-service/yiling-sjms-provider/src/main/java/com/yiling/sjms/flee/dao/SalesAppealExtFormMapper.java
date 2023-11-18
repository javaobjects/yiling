package com.yiling.sjms.flee.dao;

import java.util.List;

import com.yiling.sjms.flee.entity.SalesAppealExtFormDO;
import com.yiling.framework.common.base.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 销量申诉拓展表单 Dao 接口
 * </p>
 *
 * @author shixing.sun
 * @date 2023-03-14
 */
@Repository
public interface SalesAppealExtFormMapper extends BaseMapper<SalesAppealExtFormDO> {

    /**
     * 获取已提交/未清洗的数据
     * @return 获取已提交/未清洗的数据
     */
    List<Long> getSubmitUnCleaned();

}
