package com.yiling.sjms.gb.dto;

import java.util.List;

import lombok.Data;

@Data
public class GbFlowCompanyRelationDTO extends GbCompanyRelationDTO {
    /**
     * 商品数据
     */
    private List<GoodsInfoDTO> goodsList;
}
