package com.yiling.open.erp.bo;

/**
 * Created by xiongqi on 2018/8/20.
 */
public interface ErpEntity {

    /**
     * 获取实体对象主键
     * @return
     */
    Long getPrimaryKey();

    /**
     * 设置实体对象主键
     * @param primaryKey
     */
    void setPrimaryKey(Long primaryKey);

    /**
     * 获取供应商ERP系统主键
     * @return
     */
    String getErpPrimaryKey();

    /**
     * 增量同步时，判断供应商ERP系统记录是否已删除
     * @return
     */
    String getTaskNo();

    /**
     * 对记录签名
     * @return
     */
    String sign();

    /**
     * 获取记录md5签名
     * @return
     */
    String getDataMd5();

}


