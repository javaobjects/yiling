package com.yiling.hmc.wechat.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.hmc.insurance.bo.InsuranceRecordPayBO;
import com.yiling.hmc.insurance.dto.request.QueryInsuranceRecordPayPageRequest;
import com.yiling.hmc.wechat.entity.InsuranceRecordPayDO;

/**
 * <p>
 * C端参保记录支付流水表 Dao 接口
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-28
 */
@Repository
public interface InsuranceRecordPayMapper extends BaseMapper<InsuranceRecordPayDO> {


    /**
     * 分页查询缴费记录
     * @param page
     * @param request
     * @return
     */
    Page<InsuranceRecordPayBO> queryPage(Page page, @Param("p")QueryInsuranceRecordPayPageRequest request);
}
