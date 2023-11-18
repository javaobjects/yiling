package com.yiling.search.word.util;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.search.word.bo.EsWordExpansionBO;
import com.yiling.search.word.dto.EsWordExpansionDTO;
import com.yiling.search.word.enums.EsWordErrorEnum;
import com.yiling.search.word.enums.EsWordTypeEnum;

import cn.hutool.core.collection.CollectionUtil;

/**
 * @author shichen
 * @类名 EsWordUtils
 * @描述
 * @创建时间 2022/4/20
 * @修改人 shichen
 * @修改时间 2022/4/20
 **/
public class EsWordUtils {

    /**
     * 扩展词库boList组装字符串
     * @param boMap
     * @return
     */
    public static List<String> boToEsStr(Map<EsWordTypeEnum,List<EsWordExpansionBO>> boMap){
        List<String> allStrList = Lists.newArrayList();
        Set<EsWordTypeEnum> typeEnums = boMap.keySet();

        if(CollectionUtil.isEmpty(typeEnums)){
            return allStrList;
        }
        for(EsWordTypeEnum typeEnum:typeEnums){
            List<EsWordExpansionBO> list = boMap.get(typeEnum);
            List<String> strList = Lists.newArrayList();
            switch (typeEnum){
                case EXPAND:
                case STOP:
                    strList=list.stream().map(EsWordExpansionBO::getWord).distinct().collect(Collectors.toList());
                    break;
                case ONE_WAY_SYNONYM:
                    strList=list.stream().map(bo->{
                        String mainWord = bo.getWord();
                        List<String> refWordList=bo.getRefWordList().stream().map(EsWordExpansionDTO::getWord).distinct().collect(Collectors.toList());
                        return StringUtils.join(StringUtils.join(refWordList,","),"=>",mainWord);
                    }).collect(Collectors.toList());
                    break;
                case TWO_WAY_SYNONYM:
                    strList=list.stream().map(bo->{
                        List<String> refWordList=bo.getRefWordList().stream().map(EsWordExpansionDTO::getWord).distinct().collect(Collectors.toList());
                        return StringUtils.join(refWordList,",");
                    }).collect(Collectors.toList());
                    break;
            }
            allStrList.addAll(strList);
        }

        return allStrList;
    }

    public static byte[] esStrToByte(List<String> stringList){
        byte[] bytes=null;
        try{
            StringWriter stringWriter = new StringWriter();
            for(String str:stringList){
                stringWriter.write(str+"\r\n");
            }
            bytes=stringWriter.toString().getBytes("UTF-8");
            stringWriter.close();
        }catch (Exception e){
            throw new BusinessException(EsWordErrorEnum.ERROR,e.getMessage());
        }
        return bytes;
    }
}
