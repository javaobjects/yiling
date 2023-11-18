package com.yiling.f2b.admin.flow.form;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/4/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "查询ERP企业连锁纯销流向信息分页参数")
public class QueryFlowShopSalePageForm extends QueryPageListForm {

    private static final String SPACE_REGEX = "\\s*|\t|\r|\n";

    /**
     * 总店商业代码（商家eid）
     */
    @ApiModelProperty(value = "总店商家eid")
    private Long eid;

    /**
     * 门店商业代码（商家eid）
     */
    @ApiModelProperty(value = "门店商家eid")
    private Long shopEid;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品批准文号
     */
    @ApiModelProperty(value = "商品批准文号")
    private String license;

    /**
     * 销售时间-开始
     */
    @NotNull(message = "开始日期不能为空")
    @ApiModelProperty(value = "销售时间-开始")
    private Date startTime;

    /**
     * 销售时间-结束
     */
//    @NotNull(message = "结束日期不能为空")
    @ApiModelProperty(value = "销售时间-结束")
    private Date endTime;

    /**
     * 所属省份编码
     */
    @ApiModelProperty(value = "省份编码")
    private String provinceCode;

    /**
     * 所属城市编码
     */
    @ApiModelProperty(value = "城市编码")
    private String cityCode;

    /**
     * 所属区域编码
     */
    @ApiModelProperty(value = "区域编码")
    private String regionCode;

    /**
     * 商品生产厂家
     */
    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    /**
     * 企业标签id列表
     */
    @ApiModelProperty(value = "企业标签id列表")
    private List<Long> enterpriseTagIdList;

    /**
     * 商品规格ID
     */
    @ApiModelProperty(value = "商品规格ID")
    private Long sellSpecificationsId;

    /**
     * 有无标准库规格关系：0-无, 1-有
     */
    @ApiModelProperty(value = "有无标准库规格关系：0-无, 1-有")
    private Integer specificationIdFlag;

    /**
     * 查询时间类型： 0-6个月以内 1-6个月以前
     */
    @ApiModelProperty(value = "查询时间类型： 0-6个月以内 1-6个月以前")
    private Integer timeType;


    public void setGoodsName(String goodsName) {
        this.goodsName = clearAllSpace(goodsName);
    }

    public void setLicense(String license) {
        this.license = clearAllSpace(license);
    }

    /**
     * 去除字符串中的全部空格、回车、换行符、制表符
     *
     * @param source
     * @return
     */
    public static String clearAllSpace(String source) {
        if(StrUtil.isBlank(source)){
            return null;
        }
        Pattern p = Pattern.compile(SPACE_REGEX);
        Matcher m = p.matcher(source);
        return m.replaceAll("");
    }

}
