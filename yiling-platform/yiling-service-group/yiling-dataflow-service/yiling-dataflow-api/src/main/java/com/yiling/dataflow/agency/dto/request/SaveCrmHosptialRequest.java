package com.yiling.dataflow.agency.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/2/15 0015
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCrmHosptialRequest extends BaseRequest {

    private Long id;
    private Long crmEnterpriseId;
    /**
     * 医院性质 1公立 2私立 3厂办
     */
    private Integer medicalNature;

    /**
     * 医院类型 1-综合医院、2专科医院、3部队医院、4儿童医院、5县级医院、6厂矿职工医院、7中医医院、8社区卫生服务站、9乡镇卫生院、10村卫生室、11妇保院、12疾控中心、13私人终端、14社区卫生服务中心、15高校医院、16其他
     */
    private Integer medicalType;

    /**
     * 以岭级别 1省级三级、2市级三级、3专科医院三级、4部队三级、5其他二级医院、6县人民医院、7市级一级、8县级其他、9其他厂矿职工高校、10其他私人终端、11乡镇卫生院、12社区卫生服务中心、13社区卫生服务站、14市级其他、15县中医院、16村卫生室、17部队二级、18专科医院二级、19私人终端二级、20厂矿职工高校二级、21私人终端三级、22厂矿职工高校三级、23其他专科医院、24其他部队医院、25县级三级、26乡镇卫生院二级、27社区卫生服务中心二级、28社区卫生服务中心三级
     */
    private Integer ylLevel;

    /**
     * 国家等级 1三级甲等、2三级乙等、3三级丙等、4二级甲等、5二级乙等、6二级丙等、7一级甲等、8三级特等、9一级乙等、10一级丙等、11其他
     */
    private Integer nationalGrade;

    /**
     * 是否基药终端 1是 2否
     */
    private Integer baseMedicineFlag;

    /**
     * 总床位数
     */
    private Long totalBedNumber;

    /**
     * 是否承包 1是 2否
     */
    private Integer contractFlag;

    /**
     * 助记码
     */
    private String mnemonicCode;

    /**
     * 附加信息2
     */
    private String additionalInfoTwo;

    /**
     * 附加信息3
     */
    private String additionalInfoThree;

    /**
     * 附加信息4
     */
    private String additionalInfoFour;

    /**
     * 附加信息5
     */
    private String additionalInfoFive;

    /**
     * 县域客户代码
     */
    private String countyCustomerCode;

    /**
     * 县域客户名称
     */
    private String countyCustomerName;

    /**
     * 机构名称
     */
    private String name;
    private String licenseNumber;
    /**
     * 药品经营许可证编号
     */
    private String distributionLicenseNumber;

    /**
     * 医机构执业许可证
     */
    private String institutionPracticeLicense;

    /**
     * 以岭编码
     */
    private String ylCode;

    /**
     * 经营范围
     */
    private String businessScope;

    /**
     * 备注
     */
    private String businessRemark;

    /**
     * 曾用名
     */
    private String formerName;

    /**
     * 所属省份名称
     */
    private String provinceName;
    /**
     * 所属城市名称
     */
    private String cityName;
    /**
     * 所属区区域名称
     */
    private String regionName;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 电话
     */
    private String phone;

    /**
     * 传真
     */
    private String fax;

    /**
     * 邮编
     */
    private String postalCode;

    /**
     * 业务状态 1有效 2失效
     */
    private Integer businessCode;

    /**
     * 机构简称
     */
    private String shortName;
    //CRM编码
    private String code;

    private List<SaveCrmRelationshipRequest> crmRelationShip;

}
