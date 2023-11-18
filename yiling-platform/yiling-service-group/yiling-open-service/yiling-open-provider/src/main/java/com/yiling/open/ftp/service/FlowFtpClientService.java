package com.yiling.open.ftp.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpPurchaseFlowDTO;
import com.yiling.open.erp.dto.ErpSaleFlowDTO;
import com.yiling.open.ftp.dto.LocalCompareDTO;
import com.yiling.open.ftp.entity.FlowFtpClientDO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-02-23
 */
public interface FlowFtpClientService extends BaseService<FlowFtpClientDO> {

    /**
     * 读取excel文件
     */
     void readFlowExcel();

    /**
     * 数据缓存对比
     * @param localCompareDTO
     */
     void localCompare(LocalCompareDTO localCompareDTO);

    /**
     * 数据分组
     * @param list
     * @param index
     * @return
     */
     List<List<BaseErpEntity>> groupList(List<BaseErpEntity> list, Integer index);

    /**
     * 销售数据对比
     * @param suId
     * @param flowDay
     * @param erpSaleFlowList
     * @return
     * @throws Exception
     */
     boolean saleFlowCompare(Long suId,Integer flowDay, List<ErpSaleFlowDTO> erpSaleFlowList);

    /**
     * 采购数据对比
     * @param suId
     * @param flowDay
     * @param erpPurchaseFlowList
     * @return
     * @throws Exception
     */
     boolean purchaseFlowCompare(Long suId,Integer flowDay, List<ErpPurchaseFlowDTO> erpPurchaseFlowList) ;
}
