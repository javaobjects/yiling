package com.yiling.dataflow.wash.bo;

import java.util.Date;

/**
 * @author fucheng.bai
 * @date 2023/3/7
 */
public interface FlowDataWashEntity {

    Long getId();

    String getName();

    Date getTime();

    String getGoodsName();

    String getSpecifications();

    Long getCrmEnterpriseId();

    Long getCrmGoodsCode();

    String getGoodsInSn();

    String getManufacturer();

    String getUnit();

    String getProvinceCode();

    String getProvinceName();

    void setWashStatus(Integer washStatus);

    void setCrmGoodsCode(Long crmGoodsCode);

    void setCrmGoodsName(String goodsName);

    void setCrmGoodsSpecifications(String goodsSpecification);

    void setErrorFlag(Integer errorFlag);

    void setErrorMsg(String errorMsg);

    void setProvinceCode(String provinceCode);

    void setProvinceName(String provinceName);

    void setCityCode(String cityCode);

    void setCityName(String cityName);

    void setRegionCode(String regionCode);

    void setRegionName(String regionName);

    void setEnterpriseCersId(Long enterpriseCersId);

}
