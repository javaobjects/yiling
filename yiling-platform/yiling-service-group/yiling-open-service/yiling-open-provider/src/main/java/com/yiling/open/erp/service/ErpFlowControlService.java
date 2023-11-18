package com.yiling.open.erp.service;

import java.util.Date;
import java.util.List;

import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.entity.ErpFlowControlDO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-09-22
 */
public interface ErpFlowControlService {

    boolean onlineData(BaseErpEntity baseErpEntity);

    void syncFlowControl();

    List<ErpFlowControlDO> synFlowControlPage();

    Date getInitDateBySuIdAndSuDeptNo(Long suId,String suDeptNo);
}
