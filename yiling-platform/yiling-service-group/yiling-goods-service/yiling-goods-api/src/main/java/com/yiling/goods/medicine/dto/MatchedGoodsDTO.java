package com.yiling.goods.medicine.dto;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 匹配成功的商品列表信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MatchedGoodsDTO extends BaseDTO {
    private static final long  serialVersionUID = -7700309739174607197L;

    /**
     * 原始的id
     */
    private Long orgId;
    /**
     * 匹配的目标id
     */
    private Long targetId;
    /**
     * 相似度per%
     */
    private double  per;

    /**
     * 匹配的商品名称
     */
    private String name;

    /**
     * 匹配的通用名
     */
    private String commonName;

    /**
     * 匹配的生产厂家
     */
    private String manufacturer;
    /**
     * 匹配的批准文号
     */
    private String licenseNo;
    /**
     * 匹配的规格
     */
    private String specification;

    /**
     * 推荐匹配项
     */
    private List<MatchedGoodsDTO> probably;

    @Override
    public String toString() {
        return "MatchedGoodsDTO [orgId=" + orgId + ", targetId=" + targetId + ", per=" + per + ", name=" + name
               + ", manufacturer=" + manufacturer + ", licenseNo=" + licenseNo + ", specification="
               + specification + "]";
    }

    public static Comparator<MatchedGoodsDTO> omparator = new Comparator<MatchedGoodsDTO>() {
        @Override
        public int compare(MatchedGoodsDTO o1, MatchedGoodsDTO o2) {
            if (o1.getPer() < o2.getPer()) {
                return 1;
            } else if (o1.getPer() > o2.getPer()) {
                return -1;
            } else {
                return 0;
            }
        }
    };
}
