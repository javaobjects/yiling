package com.yiling.admin.b2b.promotion.handler;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.yiling.user.shop.dto.AreaChildrenDTO;

import cn.hutool.core.collection.CollUtil;

/**
 * @Description
 * @Author fan.shen
 * @Date 2021/12/22
 */
public class PromotionAreaUtil {

    /**
     * 获取区域描述信息
     * @param areaJsonString
     * @return
     */
    public static String getDescription(String areaJsonString){
        //解析json得出区域描述
        StringBuilder sb = new StringBuilder();
        List<AreaChildrenDTO> provinceList = JSONObject.parseArray(areaJsonString, AreaChildrenDTO.class);

        //城市数量
        Integer citySize = provinceList.stream().map(areaChildrenDTO -> {
            if(CollUtil.isNotEmpty(areaChildrenDTO.getChildren())){
                return areaChildrenDTO.getChildren().size();
            }
            return 0;
        }).reduce(0, Integer::sum);
        //区县数量
        Integer sumAreaSize = provinceList.stream().map(areaChildrenDTO -> areaChildrenDTO.getChildren().stream().map(cityDto -> {
            if (CollUtil.isNotEmpty(cityDto.getChildren())) {
                System.out.println(JSONObject.toJSONString(cityDto.getChildren()));
                return cityDto.getChildren().size();
            }
            return 0;
        }).reduce(0, Integer::sum)).reduce(0, Integer::sum);

        sb.append(provinceList.size());
        sb.append("个省");
        sb.append(citySize);
        sb.append("个市");
        sb.append(sumAreaSize);
        sb.append("个区域");

        return sb.toString();
    }
}
