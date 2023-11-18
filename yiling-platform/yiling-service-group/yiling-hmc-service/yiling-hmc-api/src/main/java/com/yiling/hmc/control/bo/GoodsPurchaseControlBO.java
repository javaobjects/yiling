package com.yiling.hmc.control.bo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 商品管控状态和管控渠道
 * @author: gxl
 * @date: 2022/4/18
 */
@Data
public class GoodsPurchaseControlBO implements Serializable {
    private static final long serialVersionUID = -9134505427137677124L;

    /**
     * 1管控 0不管控
     */
    private Integer controlStatus;

    /**
     * 管控渠道
     */
    private List<String> channelNameList;

    private Long sellSpecificationsId;
}