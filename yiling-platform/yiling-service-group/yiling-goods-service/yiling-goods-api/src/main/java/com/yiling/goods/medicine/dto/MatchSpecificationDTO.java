package com.yiling.goods.medicine.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import com.yiling.goods.util.Levenshtein;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * 商品規格转换类，将字符串拆分后方便进行相似度比较
 * @Author: wanfei.zhang
 * @Email: wanfei.zhang@rograndec.com
 * @CreateDate: 2018年5月6日
 * @Version: 1.0
 */
@Slf4j
@Data
public class MatchSpecificationDTO {

    /**
     * 原始的品规字符串
     */
    private String             orgSpec;

    /**经过转换后的品规*/
    private String             newSpec;

    /**带计量单位的品规数组 例如 kg mg g ml L cm m mm*/
    private List<String>       unitSpec;

    /**不带单位的计量数组 例如 10粒 3版 5盒 = 10 3 5 */
    private List<BigDecimal>   unUnitSpec;

    /**不带单位的计量值*/
    private BigDecimal         unUnitMultiply;

    /** 存放大包装的单位数组 */
    private List<String>       bigPackUnit;

    /** 存放无法转换的字符串 */
    private List<String>       invalidUnit;

    /**
     * 进行商品规格的处理转换
     * @param orgSpec 原始的規則字符串
     */
    public MatchSpecificationDTO(String orgSpec) {
        if (StrUtil.isEmpty(orgSpec)) {
            this.orgSpec = "";
            this.newSpec = "";
            return;
        }

        this.orgSpec = orgSpec;
        this.newSpec = orgSpec;

        newSpec = Levenshtein.trimChineseSymbols(newSpec).replaceAll("千克", "kg").replaceAll("毫克", "mg")
            .replaceAll("克", "g").replaceAll("毫升", "ml").replaceAll("升", "l")
            .replace("只", "支").replace("厘米", "cm").replace("毫米", "mm").replace("米", "m")
            .replace("＊", "*").replaceAll("×", "*").replaceAll("x", "*").replaceAll("\\(", "*").replaceAll("\\)", "*");
        if (StrUtil.isEmpty(newSpec)) {
            newSpec = "";
            return;
        }
        unitSpec = new ArrayList<String>(); ////需要进行单位转换的有效单位
        unUnitSpec = new ArrayList<BigDecimal>(); //需要进行乘积处理的计量数量
        bigPackUnit = new ArrayList<String>(); //存放大包装的单位数组 
        invalidUnit = new ArrayList<String>(); //存放无法转换的字符串
        unUnitMultiply = new BigDecimal(1);

        String unitStr = newSpec.replaceAll("[^a-z^A-Z^0-9^.^*^:^/^盒^瓶^袋^支]", "").toLowerCase();
        if (StrUtil.isEmpty(unitStr)) {
            newSpec = "";
            return;
        }

        String[] unitArray = unitStr.split("\\*|:|/");
        int length = unitArray.length;
        if (length == 0) {
            newSpec = unitStr;
        }

        //对规格进行降噪处理 , 将不为有效成分的部分数值相乘得出包装数量值  例如 10mg*10粒*2板 -> 10mg*20
        try {
            StringBuffer result = new StringBuffer("");
            for (String s : unitArray) {
                if (s.contains("kg") || s.contains("mg") || s.contains("g") ||
                        s.contains("ml") || s.contains("l") || 
                        s.contains("cm") || s.contains("mm") ||s.contains("m")) {
                    String unitTrans = unitTrans(s);
                    if (StrUtil.isEmpty(unitTrans)) {
                        continue;
                    } else if (unitTrans.endsWith("mg") || unitTrans.endsWith("ml") || unitTrans.endsWith("mm")) { //单位转换成功
                        unitSpec.add(unitTrans);
                        result.append(unitTrans).append("*");
                    } else {
                        invalidUnit.add(unitTrans);
                        result.append(unitTrans).append("*");
                    }
                } else {
                    s = s.replaceAll("[a-zA-z]", "");
                    if (StrUtil.isEmpty(s)) {
                        continue;
                    }
                    // 大包装单位处理，需要剔除,盒,瓶>=50认为是大包装，袋，支>=100认为是大包装
                    if (s.endsWith("盒") || s.endsWith("瓶")) {
                        s = s.replaceAll("[a-zA-z盒瓶]", "");
                        if (StrUtil.isEmpty(s)) {
                            continue;
                        }
                        if (Levenshtein.isNumeric(s)) {
                            BigDecimal tmpBig = new BigDecimal(s);
                            if (tmpBig.compareTo(BigDecimal.valueOf(50)) >= 0) {
                                bigPackUnit.add(s);
                                continue;
                            }
                            unUnitSpec.add(tmpBig);
                            unUnitMultiply = tmpBig.multiply(unUnitMultiply);
                            result.append(tmpBig).append("*");
                            continue;
                        }
                    }
                    if (s.endsWith("袋") || s.endsWith("支")) {
                        s = s.replaceAll("[a-zA-z袋支]", "");
                        if (StrUtil.isEmpty(s)) {
                            continue;
                        }
                        if (Levenshtein.isNumeric(s)) {
                            BigDecimal tmpBig = new BigDecimal(s);
                            if (tmpBig.compareTo(BigDecimal.valueOf(500)) >= 0) {
                                bigPackUnit.add(s);
                                continue;
                            }
                            unUnitSpec.add(tmpBig);
                            unUnitMultiply = tmpBig.multiply(unUnitMultiply);
                            result.append(tmpBig).append("*");
                            continue;
                        }
                    }
                    if (!StrUtil.isEmpty(s) && Levenshtein.isNumeric(s)) {
                        BigDecimal tmpBig = new BigDecimal(s);
                        unUnitSpec.add(tmpBig);
                        unUnitMultiply = tmpBig.multiply(unUnitMultiply);
                        result.append(tmpBig).append("*");
                        continue;
                    } else {
                        invalidUnit.add(s);
                        result.append(s).append("*");
                        continue;
                    }
                }
            }

            if (result.length() > 0 && result.lastIndexOf("*") == (result.length() - 1)) {
                result.deleteCharAt(result.length() - 1);
            }
            newSpec = result.toString();
            return;
        } catch (Exception e) {
            log.error("进行规格转换时出错了，unit= " + newSpec, e);
        }
        newSpec = unitStr;
        return;

    }

    /**
     * 规格单位转换，将kg转换为mg,g转换为mg,l转换为ml,m转换为mm,cm转换为mm
     * @param unit
     * @return
     * @author wanfei.zhang
     * @date 2018年5月2日
     */
    private static String unitTrans(String unit) {
        if (StrUtil.isEmpty(unit)) {
            return "";
        }

        String result = "";

        // mg 不用转换
        int mgIndex = unit.indexOf("mg");
        if (mgIndex != -1) {
            result = unit.replaceAll("mg", "");
            if (!StrUtil.isEmpty(result) && Levenshtein.isNumeric(result)) {
                return result + "mg";
            } else {
                return unit;
            }
        }

        // kg 转换为 mg
        int kgIndex = unit.indexOf("kg");
        if (kgIndex != -1) {
            result = unit.replaceAll("kg", "");
            if (!StrUtil.isEmpty(result) && Levenshtein.isNumeric(result)) {
                BigDecimal kgDec = new BigDecimal(result);
                BigDecimal mgDec = kgDec.multiply(BigDecimal.valueOf(1000000)).setScale(0, BigDecimal.ROUND_FLOOR);
                return mgDec + "mg";
            } else {
                return unit;
            }
        }

        // g 转换为 mg
        int gIndex = unit.indexOf("g");
        if (gIndex != -1) { //含有g
            result = unit.replaceAll("g", "");
            if (!StrUtil.isEmpty(result) && Levenshtein.isNumeric(result)) {
                BigDecimal gDec = new BigDecimal(result);
                BigDecimal mgDec = gDec.multiply(BigDecimal.valueOf(1000)).setScale(0, BigDecimal.ROUND_FLOOR);
                return mgDec + "mg";
            } else {
                return unit;
            }
        }

        // ml 不用转换
        int mlIndex = unit.indexOf("ml");
        if (mlIndex != -1) {
            result = unit.replaceAll("ml", "");
            if (!StrUtil.isEmpty(result) && Levenshtein.isNumeric(result)) {
                return result + "ml";
            } else {
                return unit;
            }
        }

        // l 转换为 ml
        int lIndex = unit.indexOf("l");
        if (lIndex != -1) { //含有l
            result = unit.replaceAll("l", "");
            if (!StrUtil.isEmpty(result) && Levenshtein.isNumeric(result)) {
                BigDecimal lDec = new BigDecimal(result);
                BigDecimal mlDec = lDec.multiply(BigDecimal.valueOf(1000)).setScale(0, BigDecimal.ROUND_FLOOR);
                return mlDec + "ml";
            } else {
                return unit;
            }
        }
        
        // mm 不用转换
        int mmIndex = unit.indexOf("mm");
        if (mmIndex != -1) {
            result = unit.replaceAll("mm", "");
            if (!StrUtil.isEmpty(result) && Levenshtein.isNumeric(result)) {
                return result + "mm";
            } else {
                return unit;
            }
        }

        // cm 转换为 mm
        int cmIndex = unit.indexOf("cm");
        if (cmIndex != -1) { //含有l
            result = unit.replaceAll("cm", "");
            if (!StrUtil.isEmpty(result) && Levenshtein.isNumeric(result)) {
                BigDecimal lDec = new BigDecimal(result);
                BigDecimal mlDec = lDec.multiply(BigDecimal.valueOf(10)).setScale(0, BigDecimal.ROUND_FLOOR);
                return mlDec + "mm";
            } else {
                return unit;
            }
        }
        
        
        // m 转换为 mm
        int mIndex = unit.indexOf("m");
        if (mIndex != -1) { //含有l
            result = unit.replaceAll("m", "");
            if (!StrUtil.isEmpty(result) && Levenshtein.isNumeric(result)) {
                BigDecimal lDec = new BigDecimal(result);
                BigDecimal mlDec = lDec.multiply(BigDecimal.valueOf(1000)).setScale(0, BigDecimal.ROUND_FLOOR);
                return mlDec + "mm";
            } else {
                return unit;
            }
        }

        return unit;
    }

    /**
     * 得到两个規格的距离
     * @param orgMatchSpecification
     * @return
     */
    public double getSpecDis(MatchSpecificationDTO orgMatchSpecification) {
        if (orgMatchSpecification == null) {
            orgMatchSpecification = new MatchSpecificationDTO("");
        }
        double unitDIs = Levenshtein.getDistance(this.getNewSpec(), orgMatchSpecification.getNewSpec());
        if (unitDIs <= 0) {
            return 0D;
        }
        if (StrUtil.isEmpty(this.getNewSpec()) || StrUtil.isEmpty(orgMatchSpecification.getNewSpec())) {
            unitDIs = unitDIs / 2;
            return unitDIs;
        }
        if (this.getNewSpec().equals(orgMatchSpecification.getNewSpec())) {
            unitDIs = unitDIs / 10;
            return unitDIs;
        }

        // 不存在无效的单位转换时，此时只需要比较计量单位，计数单位，计数单位的乘积这几个是否相等或者包含
        if (this.getInvalidUnit().isEmpty() && orgMatchSpecification.getInvalidUnit().isEmpty()) {
            if (compare(this.getUnitSpec(), orgMatchSpecification.getUnitSpec())) { //计量单位相等
                if (compare(this.getUnUnitSpec(), orgMatchSpecification.getUnUnitSpec())) { // 计数单位相等
                    unitDIs = unitDIs / 50;
                    return unitDIs;
                } else if (this.getUnUnitMultiply().compareTo(orgMatchSpecification.getUnUnitMultiply()) == 0) {// 计数单位的乘积相等
                    unitDIs = unitDIs / 20;
                    return unitDIs;
                } else if (this.getUnUnitSpec().containsAll(orgMatchSpecification.getUnUnitSpec())
                           || orgMatchSpecification.getUnUnitSpec().containsAll(this.getUnUnitSpec())) { // 计数单位包含
                    unitDIs = unitDIs / 10;
                    return unitDIs;
                }
            }
        }

        if (this.getNewSpec().startsWith(orgMatchSpecification.getNewSpec())
            || orgMatchSpecification.getNewSpec().startsWith(this.getNewSpec())) {
            unitDIs = unitDIs / 5;
            return unitDIs;
        } else if (this.getNewSpec().contains(orgMatchSpecification.getNewSpec())
                   || orgMatchSpecification.getNewSpec().contains(this.getNewSpec())) {
            unitDIs = unitDIs / 3;
            return unitDIs;
        }

        return unitDIs;
    }

    /**
     * 获取两个商品规格的距离
     * @param str1
     * @param str2
     * @return
     */
    public static double getSpecDis(String str1, String str2) {
        MatchSpecificationDTO o1 = new MatchSpecificationDTO(str1);
        MatchSpecificationDTO o2 = new MatchSpecificationDTO(str2);
        return o1.getSpecDis(o2);
    }

    /**
     * 比较算法
     * @param a
     * @param b
     * @param <T>
     * @return
     */
    private static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a.size() != b.size()) {
            return false;
        }
        Collections.sort(a);
        Collections.sort(b);
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) {
                return false;
            }
        }
        return true;
    }
}
