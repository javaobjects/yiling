package com.yiling.goods.medicine.dto;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MatchNameDTO extends BaseDTO {
    private static final long serialVersionUID = 2448172915830529703L;
    /**
     * 最佳匹配目标id
     */
    private Long           targetId;

    /**
     * 匹配的名称
     */
    private String            targetName;

    /**
     * 相似度per%
     */
    private double            per;

    /**
     * 推荐匹配项
     */
    private List<MatchNameDTO>   probably;

    public MatchNameDTO() {
        super();
    }

    public MatchNameDTO(Long targetId, String targetName) {
        super();
        this.targetId = targetId;
        this.targetName = targetName;
    }

    public MatchNameDTO(final String targetName) {
        super();
        this.targetName = targetName;
    }

    @Override
    public String toString() {
        return "MatchName{" + "targetId=" + targetId + ", per=" + per + ", targetName='" + targetName + '\'' + '}';
    }

    public static Comparator<MatchNameDTO> omparator = new Comparator<MatchNameDTO>() {
        @Override
        public int compare(MatchNameDTO o1, MatchNameDTO o2) {
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
