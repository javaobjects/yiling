package com.yiling.sales.assistant.task.dto.request;

import java.util.List;

import lombok.Data;

/**
 * @author: gxl
 * @date: 2022/10/26
 */
@Data
public class TaskGoodsMatchListDTO implements java.io.Serializable{
    private static final long serialVersionUID = 1869208812867160318L;
    /**
     * 配送商id
     */
    private Long eid;

    /**
     * 商品列表
     */
    private List<TaskGoodsMatchDTO> goodsMatchList;
}