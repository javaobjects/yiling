package com.yiling.sales.assistant.task.dto.app;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @author ray
 */
@Data
public class MyTaskProgressDTO implements Serializable {


    private static final long serialVersionUID = -6516605418103219131L;
    private String finishValue;

    private Integer finishGoods;

    private Integer taskGoodsTotal;

    private String goal;

    private String percent;

    private List<MyTaskProgressStepDTO> percentList;

    /*private Boolean eachCompute;*/

    private String unfinish;

}
