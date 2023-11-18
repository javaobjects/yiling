package com.yiling.marketing.common.util;

import java.util.List;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.yiling.user.shop.dto.AreaChildrenDTO;

import cn.hutool.core.collection.CollUtil;

/**
 * 活动-区域工具类
 *
 * @Description
 * @Author fan.shen
 * @Date 2021/12/22
 */
public class PromotionAreaUtil {

//    public static void main(String[] args) {
//        String area = "[{\"code\":\"620000\",\"name\":\"甘肃省\",\"children\":[{\"code\":\"620100\",\"name\":\"兰州市\",\"children\":[{\"code\":\"620102\",\"name\":\"城关区\",\"children\":[]},{\"code\":\"620103\",\"name\":\"七里河区\",\"children\":[]},{\"code\":\"620104\",\"name\":\"西固区\",\"children\":[]},{\"code\":\"620105\",\"name\":\"安宁区\",\"children\":[]},{\"code\":\"620111\",\"name\":\"红古区\",\"children\":[]},{\"code\":\"620121\",\"name\":\"永登县\",\"children\":[]},{\"code\":\"620122\",\"name\":\"皋兰县\",\"children\":[]},{\"code\":\"620123\",\"name\":\"榆中县\",\"children\":[]}]},{\"code\":\"620200\",\"name\":\"嘉峪关市\",\"children\":[{\"code\":\"620299\",\"name\":\"嘉峪关市\",\"children\":[]}]},{\"code\":\"620300\",\"name\":\"金昌市\",\"children\":[{\"code\":\"620302\",\"name\":\"金川区\",\"children\":[]},{\"code\":\"620321\",\"name\":\"永昌县\",\"children\":[]}]},{\"code\":\"620400\",\"name\":\"白银市\",\"children\":[{\"code\":\"620402\",\"name\":\"白银区\",\"children\":[]},{\"code\":\"620403\",\"name\":\"平川区\",\"children\":[]},{\"code\":\"620421\",\"name\":\"靖远县\",\"children\":[]},{\"code\":\"620422\",\"name\":\"会宁县\",\"children\":[]},{\"code\":\"620423\",\"name\":\"景泰县\",\"children\":[]}]},{\"code\":\"620500\",\"name\":\"天水市\",\"children\":[{\"code\":\"620502\",\"name\":\"秦州区\",\"children\":[]},{\"code\":\"620503\",\"name\":\"麦积区\",\"children\":[]},{\"code\":\"620521\",\"name\":\"清水县\",\"children\":[]},{\"code\":\"620522\",\"name\":\"秦安县\",\"children\":[]},{\"code\":\"620523\",\"name\":\"甘谷县\",\"children\":[]},{\"code\":\"620524\",\"name\":\"武山县\",\"children\":[]},{\"code\":\"620525\",\"name\":\"张家川回族自治县\",\"children\":[]}]},{\"code\":\"620600\",\"name\":\"武威市\",\"children\":[{\"code\":\"620602\",\"name\":\"凉州区\",\"children\":[]},{\"code\":\"620621\",\"name\":\"民勤县\",\"children\":[]},{\"code\":\"620622\",\"name\":\"古浪县\",\"children\":[]},{\"code\":\"620623\",\"name\":\"天祝藏族自治县\",\"children\":[]}]},{\"code\":\"620700\",\"name\":\"张掖市\",\"children\":[{\"code\":\"620702\",\"name\":\"甘州区\",\"children\":[]},{\"code\":\"620721\",\"name\":\"肃南裕固族自治县\",\"children\":[]},{\"code\":\"620722\",\"name\":\"民乐县\",\"children\":[]},{\"code\":\"620723\",\"name\":\"临泽县\",\"children\":[]},{\"code\":\"620724\",\"name\":\"高台县\",\"children\":[]},{\"code\":\"620725\",\"name\":\"山丹县\",\"children\":[]}]},{\"code\":\"620800\",\"name\":\"平凉市\",\"children\":[{\"code\":\"620802\",\"name\":\"崆峒区\",\"children\":[]},{\"code\":\"620821\",\"name\":\"泾川县\",\"children\":[]},{\"code\":\"620822\",\"name\":\"灵台县\",\"children\":[]},{\"code\":\"620823\",\"name\":\"崇信县\",\"children\":[]},{\"code\":\"620825\",\"name\":\"庄浪县\",\"children\":[]},{\"code\":\"620826\",\"name\":\"静宁县\",\"children\":[]},{\"code\":\"620881\",\"name\":\"华亭市\",\"children\":[]}]},{\"code\":\"620900\",\"name\":\"酒泉市\",\"children\":[{\"code\":\"620902\",\"name\":\"肃州区\",\"children\":[]},{\"code\":\"620921\",\"name\":\"金塔县\",\"children\":[]},{\"code\":\"620922\",\"name\":\"瓜州县\",\"children\":[]},{\"code\":\"620923\",\"name\":\"肃北蒙古族自治县\",\"children\":[]},{\"code\":\"620924\",\"name\":\"阿克塞哈萨克族自治县\",\"children\":[]},{\"code\":\"620981\",\"name\":\"玉门市\",\"children\":[]},{\"code\":\"620982\",\"name\":\"敦煌市\",\"children\":[]}]},{\"code\":\"621000\",\"name\":\"庆阳市\",\"children\":[{\"code\":\"621002\",\"name\":\"西峰区\",\"children\":[]},{\"code\":\"621021\",\"name\":\"庆城县\",\"children\":[]},{\"code\":\"621022\",\"name\":\"环县\",\"children\":[]},{\"code\":\"621023\",\"name\":\"华池县\",\"children\":[]},{\"code\":\"621024\",\"name\":\"合水县\",\"children\":[]},{\"code\":\"621025\",\"name\":\"正宁县\",\"children\":[]},{\"code\":\"621026\",\"name\":\"宁县\",\"children\":[]},{\"code\":\"621027\",\"name\":\"镇原县\",\"children\":[]}]},{\"code\":\"621100\",\"name\":\"定西市\",\"children\":[{\"code\":\"621102\",\"name\":\"安定区\",\"children\":[]},{\"code\":\"621121\",\"name\":\"通渭县\",\"children\":[]},{\"code\":\"621122\",\"name\":\"陇西县\",\"children\":[]},{\"code\":\"621123\",\"name\":\"渭源县\",\"children\":[]},{\"code\":\"621124\",\"name\":\"临洮县\",\"children\":[]},{\"code\":\"621125\",\"name\":\"漳县\",\"children\":[]},{\"code\":\"621126\",\"name\":\"岷县\",\"children\":[]}]},{\"code\":\"621200\",\"name\":\"陇南市\",\"children\":[{\"code\":\"621202\",\"name\":\"武都区\",\"children\":[]},{\"code\":\"621221\",\"name\":\"成县\",\"children\":[]},{\"code\":\"621222\",\"name\":\"文县\",\"children\":[]},{\"code\":\"621223\",\"name\":\"宕昌县\",\"children\":[]},{\"code\":\"621224\",\"name\":\"康县\",\"children\":[]},{\"code\":\"621225\",\"name\":\"西和县\",\"children\":[]},{\"code\":\"621226\",\"name\":\"礼县\",\"children\":[]},{\"code\":\"621227\",\"name\":\"徽县\",\"children\":[]},{\"code\":\"621228\",\"name\":\"两当县\",\"children\":[]}]},{\"code\":\"622900\",\"name\":\"临夏回族自治州\",\"children\":[{\"code\":\"622901\",\"name\":\"临夏市\",\"children\":[]},{\"code\":\"622921\",\"name\":\"临夏县\",\"children\":[]},{\"code\":\"622922\",\"name\":\"康乐县\",\"children\":[]},{\"code\":\"622923\",\"name\":\"永靖县\",\"children\":[]},{\"code\":\"622924\",\"name\":\"广河县\",\"children\":[]},{\"code\":\"622925\",\"name\":\"和政县\",\"children\":[]},{\"code\":\"622926\",\"name\":\"东乡族自治县\",\"children\":[]},{\"code\":\"622927\",\"name\":\"积石山县\",\"children\":[]}]},{\"code\":\"623000\",\"name\":\"甘南藏族自治州\",\"children\":[{\"code\":\"623001\",\"name\":\"合作市\",\"children\":[]},{\"code\":\"623021\",\"name\":\"临潭县\",\"children\":[]},{\"code\":\"623022\",\"name\":\"卓尼县\",\"children\":[]},{\"code\":\"623023\",\"name\":\"舟曲县\",\"children\":[]},{\"code\":\"623024\",\"name\":\"迭部县\",\"children\":[]},{\"code\":\"623025\",\"name\":\"玛曲县\",\"children\":[]},{\"code\":\"623026\",\"name\":\"碌曲县\",\"children\":[]},{\"code\":\"623027\",\"name\":\"夏河县\",\"children\":[]}]}]}]";
//        boolean check = check(area, "620102");
//        System.out.println(check);
//    }

    /**
     * 判断是否包含指定区域
     *
     * @param permittedAreaDetail
     * @param regionCode
     * @return
     */
    public static boolean check(String permittedAreaDetail, String regionCode) {
        if (StringUtils.isEmpty(permittedAreaDetail) || StringUtils.isEmpty(regionCode)) {
            return false;
        }
        List<AreaChildrenDTO> provinceList = JSONObject.parseArray(permittedAreaDetail, AreaChildrenDTO.class);
        for (AreaChildrenDTO province : provinceList) {
            List<AreaChildrenDTO> cityList = province.getChildren();
            for (AreaChildrenDTO city : cityList) {
                List<AreaChildrenDTO> areaList = city.getChildren();
                if (CollUtil.isNotEmpty(areaList)) {
                    if(areaList.stream().anyMatch(item -> item.getCode().equals(regionCode))){
                        return true;
                    }
                }
            }
        }
        //
        //        provinceList.stream().map(provinceDTO -> provinceDTO.getChildren().stream().map(cityDto -> {
        //            System.out.println(provinceDTO);
        //            System.out.println(cityDto);
        //            List<AreaChildrenDTO> children = cityDto.getChildren();
        //            if (CollUtil.isEmpty(children)) {
        //                return false;
        //            }
        //            return children.stream().anyMatch(item -> item.getCode().equals(regionCode));
        //        }));
        return false;
    }
}
