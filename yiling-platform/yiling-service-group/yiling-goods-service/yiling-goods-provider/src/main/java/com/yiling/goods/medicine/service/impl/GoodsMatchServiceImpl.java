package com.yiling.goods.medicine.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import com.yiling.goods.medicine.dto.MatchGoodsDTO;
import com.yiling.goods.medicine.dto.MatchNameDTO;
import com.yiling.goods.medicine.dto.MatchSpecificationDTO;
import com.yiling.goods.medicine.dto.MatchedGoodsDTO;
import com.yiling.goods.medicine.service.GoodsMatchService;
import com.yiling.goods.util.Levenshtein;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 匹配算法
 *
 * @author shuan
 */
@Slf4j
@Service
public class GoodsMatchServiceImpl implements GoodsMatchService {

    public static final int PRE_SIZE = 3;


    /**
     * 普通字符串进行比较匹配
     *
     * @param orgName 原数据
     * @param targets 待匹配的列表
     * @return 相似度最高的目标对象
     */
    @Override
    public MatchNameDTO matchingName(String orgName, List<MatchNameDTO> targets) {

        if (targets == null || targets.size() == 0) {
            return null;
        }
        if (StrUtil.isEmpty(orgName)) {
            return null;
        }

        String goodsFactory = Levenshtein.getCnStr(Levenshtein.trimChineseSymbols(orgName));

        List<MatchNameDTO> list = new ArrayList<MatchNameDTO>();
        for (MatchNameDTO target : targets) {
            String targetFactoty = Levenshtein.getCnStr(target.getTargetName());

            double factDis = Levenshtein.getDistance(goodsFactory, targetFactoty);

            if (!StrUtil.isEmpty(goodsFactory) && !StrUtil.isEmpty(targetFactoty)) {
                if (targetFactoty.equals(goodsFactory)) {
                    factDis = factDis / 5;
                } else if (targetFactoty.contains(goodsFactory) || goodsFactory.contains(targetFactoty)) {
                    factDis = factDis / 2;
                }
            } else {
                factDis = factDis / 5;
            }


            int len = 0;
            int len1 = targetFactoty.length();
            int len2 = goodsFactory.length();
            if (len1 > len2) {
                len = len1;
            } else {
                len = len2;
            }

            BigDecimal resultPre = new BigDecimal((1 - (factDis / len)) * 100);
            if (BigDecimal.ZERO.compareTo(resultPre) >= 0) {
                resultPre = BigDecimal.ZERO;
            }

            resultPre = resultPre.setScale(2, BigDecimal.ROUND_HALF_UP);

            MatchNameDTO g = new MatchNameDTO();
            g.setTargetId(target.getTargetId());
            g.setTargetName(target.getTargetName());
            g.setPer(resultPre.doubleValue());

            list.add(g);
        }

        MatchNameDTO result = null;
        Collections.sort(list, MatchNameDTO.omparator);
        result = list.get(0);
        List<MatchNameDTO> probably = new ArrayList<MatchNameDTO>();

        int size = list.size() > PRE_SIZE ? PRE_SIZE : list.size();
        probably.addAll(list.subList(0, size));

        result.setProbably(probably);
        return result;
    }


    /**
     * 进行商品信息的匹配，比较商品规则
     *
     * @param matchGoods 原商品数据
     * @param targets    待匹配的商品列表
     * @return 相似度最高的目标对象
     */
    @Override
    public MatchedGoodsDTO matchingGoodsWithSpec(MatchGoodsDTO matchGoods, List<MatchGoodsDTO> targets) {
        if (targets == null || targets.size() == 0) {
            return null;
        }
        if (matchGoods == null) {
            return null;
        }
//        if (matchGoods.getId() == null) {
//            return null;
//        }

        String gname = Levenshtein.trimChineseSymbols(matchGoods.getName());
        String gfactory = Levenshtein.trimChineseSymbols(matchGoods.getManufacturer());
        String glicenseno = Levenshtein.trimChineseSymbols(matchGoods.getLicenseno());
        MatchSpecificationDTO gMatchSpecification = new MatchSpecificationDTO(matchGoods.getSpecification());
        String gunit = gMatchSpecification.getNewSpec();

        //获取里面所有的中文信息
        String goodsName = Levenshtein.replace(gname);
        String goodsFactory = Levenshtein.getCnStr(gfactory);
        String goodsLince = glicenseno.replaceAll("[^a-z^A-Z^0-9]", "");

        String s2 = gname + gunit + gfactory;
        String parenth = Levenshtein.getParenthesesCon(gname);

        List<MatchedGoodsDTO> list = new ArrayList<MatchedGoodsDTO>();
        for (MatchGoodsDTO target : targets) {

            String tname = Levenshtein.trimChineseSymbols(target.getName());
            String tcommonName = Levenshtein.trimChineseSymbols(target.getCommonName());
            String tfactory = Levenshtein.trimChineseSymbols(target.getManufacturer());
            String tlicenseno = Levenshtein.trimChineseSymbols(target.getLicenseno());
            MatchSpecificationDTO tMatchSpecification = new MatchSpecificationDTO(target.getSpecification());
            String tunit = tMatchSpecification.getNewSpec();

            double result = 0d;

            String targetName = Levenshtein.replace(tname);
            String targetComName = Levenshtein.replace(tcommonName);
            String targetFactoty = Levenshtein.getCnStr(tfactory);
            String targetLince = tlicenseno.replaceAll("[^a-z^A-Z^0-9]", "");
            String tParenth = Levenshtein.getParenthesesCon(tname);

            double nameDis = Levenshtein.getDistance(gname, tname);
            double comNameDis = Levenshtein.getDistance(gname, tcommonName);
            double factDis = Levenshtein.getDistance(goodsFactory, targetFactoty);
            double unitDIs = tMatchSpecification.getSpecDis(gMatchSpecification);
            if (!parenth.equals(tParenth) && !parenth.replaceAll("装", "").replaceAll("散装饮片", "").equals(tParenth)) {
                nameDis = nameDis + 1;
            }

            //原商品名称不为空
            if (!StrUtil.isEmpty(gname)) {
                if (tname.equals(gname)) {
                    nameDis = nameDis / 10;
                } else if (tname.contains(gname) || gname.contains(tname)) {
                    nameDis = nameDis / 5;
                }
            } else {
                nameDis = nameDis / 2;
            }

            if (!StrUtil.isEmpty(gname)) {
                if (targetComName.equals(gname)) {
                    comNameDis = comNameDis / 10;
                } else if (tcommonName.contains(gname) || gname.contains(tcommonName)) {
                    comNameDis = comNameDis / 5;
                }
            } else {
                comNameDis = comNameDis / 2;
            }

            String s1 = "";
            //商品名和通用名 取最小值
            if (nameDis > comNameDis && !StrUtil.isEmpty(tcommonName)) {
                nameDis = comNameDis;
                s1 = tcommonName + tunit + tfactory;
            } else {
                s1 = tname + tunit + tfactory;
            }

            if (!StrUtil.isEmpty(goodsFactory) && !StrUtil.isEmpty(targetFactoty)) {
                if (targetFactoty.equals(goodsFactory)) {
                    factDis = factDis / 10;
                } else if (targetFactoty.contains(goodsFactory) || goodsFactory.contains(targetFactoty)) {
                    factDis = factDis / 5;
                } else if (targetFactoty.contains("以岭") && goodsFactory.contains("以岭")) {
                    factDis = factDis / 2;
                }
            } else {
                factDis = factDis / 2;
            }

            result = nameDis + factDis + unitDIs;

            if (Levenshtein.clearBracket(gname).equals(Levenshtein.clearBracket(tname))) {
                result = result / 3;
            }else if(Levenshtein.clearBracket(gname).contains(Levenshtein.clearBracket(tname))||Levenshtein.clearBracket(tname).contains(Levenshtein.clearBracket(gname))){
                result = result / 2;
            }else{
                result = result + 1;
            }
//            if (!StrUtil.isEmpty(goodsLince) && goodsLince.equals(targetLince)) {
//                result = result / 5;
//            } else if (StrUtil.isEmpty(goodsLince)) {
//                result = result + 1;
//            } else {
//                result = result + 2;
//            }

            int len = 0;
            int len1 = s1.length();
            int len2 = s2.length();
            if (len1 > len2) {
                len = len1;
            } else {
                len = len2;
            }

            MatchedGoodsDTO g = new MatchedGoodsDTO();
            g.setOrgId(matchGoods.getId());
            g.setTargetId(target.getId());
            g.setName(target.getName());
            g.setManufacturer(target.getManufacturer());
            g.setLicenseNo(target.getLicenseno());
            g.setSpecification(target.getSpecification());
            g.setCommonName(target.getCommonName());

            BigDecimal b = new BigDecimal((1 - (result / len)) * 100);
            if (BigDecimal.ZERO.compareTo(b) >= 0) {
                b = BigDecimal.ZERO;
            }
            g.setPer(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            list.add(g);
        }
        return findProbably(list);
    }

    @Override
    public MatchedGoodsDTO matchingGoodsWithNameAndSpec(MatchGoodsDTO matchGoods, List<MatchGoodsDTO> targets) {
        if (targets == null || targets.size() == 0) {
            return null;
        }
        if (matchGoods == null) {
            return null;
        }
        if (matchGoods.getId() == null) {
            return null;
        }

        String gname = Levenshtein.trimChineseSymbols(matchGoods.getName());
        MatchSpecificationDTO gMatchSpecification = new MatchSpecificationDTO(matchGoods.getSpecification());
        String gunit = gMatchSpecification.getNewSpec();

        String goodsName = Levenshtein.replace(gname);
        String s2 = gname + gunit;
        String parenth = Levenshtein.getParenthesesCon(gname);

        List<MatchedGoodsDTO> list = new ArrayList<>();
        for (MatchGoodsDTO target : targets) {

            String tname = Levenshtein.trimChineseSymbols(target.getName());
            MatchSpecificationDTO tMatchSpecification = new MatchSpecificationDTO(target.getSpecification());
            String tunit = tMatchSpecification.getNewSpec();

            double result = 0d;

            String targetName = Levenshtein.replace(tname);
            String tParenth = Levenshtein.getParenthesesCon(tname);

            double nameDis = Levenshtein.getDistance(goodsName, targetName);
            double unitDIs = tMatchSpecification.getSpecDis(gMatchSpecification);
            if (!parenth.equals(tParenth) && !parenth.replaceAll("装", "").equals(tParenth)) {
                nameDis = nameDis + 1;
            }

            if (!StrUtil.isEmpty(goodsName)) {
                if (targetName.equals(goodsName)) {
                    nameDis = nameDis / 5;
                } else if (targetName.contains(goodsName) || goodsName.contains(targetName)) {
                    nameDis = nameDis / 2;
                }
            } else {
                nameDis = nameDis / 2;
            }

            String s1 = tname + tunit;
            result = nameDis + unitDIs;

            int len = 0;
            int len1 = s1.length();
            int len2 = s2.length();
            if (len1 > len2) {
                len = len1;
            } else {
                len = len2;
            }

            MatchedGoodsDTO g = new MatchedGoodsDTO();
            g.setOrgId(matchGoods.getId());
            g.setTargetId(target.getId());
            g.setName(target.getName());
            g.setManufacturer(target.getManufacturer());
            g.setLicenseNo(target.getLicenseno());
            g.setSpecification(target.getSpecification());
            g.setCommonName(target.getCommonName());

            BigDecimal b = new BigDecimal((1 - (result / len)) * 100);
            if (BigDecimal.ZERO.compareTo(b) >= 0) {
                b = BigDecimal.ZERO;
            }
            g.setPer(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            list.add(g);
        }
        return findProbably(list);
    }

    /**
     * 进行商品信息的匹配，不比較商品規格
     *
     * @param matchGoods 原商品数据
     * @param targets    待匹配的商品列表
     * @return 相似度最高的目标对象
     */
    @Override
    public MatchedGoodsDTO matching(MatchGoodsDTO matchGoods, List<MatchGoodsDTO> targets) {
        if (targets == null || targets.size() == 0) {
            return null;
        }
        if (matchGoods == null) {
            return null;
        }
//        if (matchGoods.getId() == null) {
//            return null;
//        }

        String gname = Levenshtein.trimChineseSymbols(matchGoods.getName());
        String gfactory = Levenshtein.trimChineseSymbols(matchGoods.getManufacturer());
        String glicenseno = Levenshtein.trimChineseSymbols(matchGoods.getLicenseno());

        String goodsName = Levenshtein.replace(gname);
        String goodsFactory = Levenshtein.getCnStr(gfactory);
        String goodsLince = glicenseno.replaceAll("[^a-z^A-Z^0-9]", "");

        String s2 = gname + gfactory;
        String parenth = Levenshtein.getParenthesesCon(gname);

        List<MatchedGoodsDTO> list = new ArrayList<MatchedGoodsDTO>();
        for (MatchGoodsDTO target : targets) {

            String tname = Levenshtein.trimChineseSymbols(target.getName());
            String tcommonName = Levenshtein.trimChineseSymbols(target.getCommonName());
            String tfactory = Levenshtein.trimChineseSymbols(target.getManufacturer());
            String tlicenseno = Levenshtein.trimChineseSymbols(target.getLicenseno());

            double result = 0d;

            String targetName = Levenshtein.replace(tname);
            String targetComName = Levenshtein.replace(tcommonName);
            String targetFactoty = Levenshtein.getCnStr(tfactory);
            String targetLince = tlicenseno.replaceAll("[^a-z^A-Z^0-9]", "");
            String tParenth = Levenshtein.getParenthesesCon(tname);

            double nameDis = Levenshtein.getDistance(goodsName, targetName);
            double comNameDis = Levenshtein.getDistance(goodsName, targetComName);
            double factDis = Levenshtein.getDistance(goodsFactory, targetFactoty);

            if (!parenth.equals(tParenth) && !parenth.replaceAll("装", "").equals(tParenth)) {
                nameDis = nameDis + 1;
            }

            if (!StrUtil.isEmpty(goodsName)) {
                if (targetName.equals(goodsName)) {
                    nameDis = nameDis / 5;
                } else if (targetName.contains(goodsName) || goodsName.contains(targetName)) {
                    nameDis = nameDis / 2;
                }
            } else {
                nameDis = nameDis / 2;
            }

            if (!StrUtil.isEmpty(goodsName)) {
                if (targetComName.equals(goodsName)) {
                    comNameDis = comNameDis / 5;
                } else if (targetComName.contains(goodsName) || goodsName.contains(targetComName)) {
                    comNameDis = comNameDis / 2;
                }
            } else {
                comNameDis = comNameDis / 2;
            }

            String s1 = "";
            //商品名和通用名 取最小值
            if (nameDis > comNameDis && !StrUtil.isEmpty(tcommonName)) {
                nameDis = comNameDis;
                s1 = tcommonName + tfactory;
            } else {
                s1 = tname + tfactory;
            }

            if (!StrUtil.isEmpty(goodsFactory) && !StrUtil.isEmpty(targetFactoty)) {
                if (targetFactoty.equals(goodsFactory)) {
                    factDis = factDis / 5;
                } else if (targetFactoty.contains(goodsFactory) || goodsFactory.contains(targetFactoty)) {
                    factDis = factDis / 2;
                }
            } else {
                factDis = factDis / 5;
            }

            result = nameDis + factDis;

            if (!StrUtil.isEmpty(goodsLince) && goodsLince.equals(targetLince)) {
                result = result / 2;
            } else if (StrUtil.isEmpty(goodsLince) && StrUtil.isEmpty(targetLince)) {
                result = result / 5;
            } else if (StrUtil.isEmpty(goodsLince)) {
                result = result + 1;
            } else {
                result = result + 2;
            }

            int len = 0;
            int len1 = s1.length();
            int len2 = s2.length();
            if (len1 > len2) {
                len = len1;
            } else {
                len = len2;
            }

            MatchedGoodsDTO g = new MatchedGoodsDTO();
            g.setOrgId(matchGoods.getId());
            g.setTargetId(target.getId());
            g.setName(target.getName());
            g.setManufacturer(target.getManufacturer());
            g.setLicenseNo(target.getLicenseno());
            g.setSpecification(target.getSpecification());
            g.setCommonName(target.getCommonName());

            BigDecimal b = new BigDecimal((1 - (result / len)) * 100);
            if (BigDecimal.ZERO.compareTo(b) >= 0) {
                b = BigDecimal.ZERO;
            }
            g.setPer(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            list.add(g);
        }
        return findProbably(list);
    }

    private static MatchedGoodsDTO findProbably(List<MatchedGoodsDTO> list) {
        MatchedGoodsDTO matchedGoods = null;
        Collections.sort(list, MatchedGoodsDTO.omparator);
        matchedGoods = list.get(0);
        List<MatchedGoodsDTO> probably = new ArrayList<MatchedGoodsDTO>();

        for (int i = 0; i < list.size(); i++) {
            if (i == PRE_SIZE) {
                break;
            }
            probably.add(list.get(i));
        }

        matchedGoods.setProbably(probably);

        return matchedGoods;
    }


}
