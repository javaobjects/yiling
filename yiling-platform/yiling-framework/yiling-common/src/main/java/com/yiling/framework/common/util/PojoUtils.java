package com.yiling.framework.common.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.ServiceException;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;

/**
 * POJO 工具类
 *
 * @author: xuan.zhou
 * @date: 2021/5/12
 */
public class PojoUtils {

    private static ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    /**
     * 属性复制
     *
     * @param source
     * @param destination
     */
    public static void map(Object source, Object destination) {
        modelMapper.map(source, destination);
    }

    /**
     * 属性复制-指定属性名
     * @param source
     * @param destination
     * @param fields
     */
    public static void mapByFields(Object source, Object destination,List<String> fields) {
        // 获取源对象的Class对象
        Class<?> sourceClass = source.getClass();
        // 获取目标对象的Class对象
        Class<?> targetClass = destination.getClass();

        fields.forEach(field->{
            // 获取源对象的属性值
            Field sourceField = null;
            try {
                sourceField = sourceClass.getDeclaredField(field);
            // 需要设置为可访问
            sourceField.setAccessible(true);
            // 将源对象的属性值赋值给目标对象的属性
            Field targetField = targetClass.getDeclaredField(field);
            targetField.setAccessible(true);
            targetField.set(destination,sourceField.get(source));
            } catch (Exception e) {
                throw new ServiceException(ResultCode.FAILED);
            }
        });
    }


    /**
     * 对象转换
     *
     * @param targetClass
     * @return
     */
    public static <Source, Target> Target map(Source source, Class<Target> targetClass) {
        if (source == null) {
            return null;
        }

        Target target = modelMapper.map(source, targetClass);
        return target;
    }

    /**
     * 批量对象转换
     *
     * @param sourceList
     * @param targetClass
     * @return
     */
    public static <Source, Target> List<Target> map(List<Source> sourceList, Class<Target> targetClass) {
        if (CollUtil.isEmpty(sourceList)) {
            return ListUtil.empty();
        }

        return sourceList.stream().map(e -> map(e, targetClass)).collect(Collectors.toList());
    }

    /**
     * 批量对象转换
     *
     * @param map
     * @param targetClass
     * @return
     */
    public static <Key, Source, Target> Map<Key, List<Target>> map(Map<Key, List<Source>> map, Class<Target> targetClass) {
        if (CollUtil.isEmpty(map)) {
            return MapUtil.empty();
        }

        Map<Key, List<Target>> newMap = MapUtil.newHashMap();
        for (Key key : map.keySet()) {
            newMap.put(key, map(map.get(key), targetClass));
        }

        return newMap;
    }

    /**
     * 分页列表转换
     *
     * @param sourcePage
     * @param targetClass
     * @return
     */
    public static <Source, Target> Page<Target> map(IPage<Source> sourcePage, Class<Target> targetClass) {
        List<Target> targetList = map(sourcePage.getRecords(), targetClass);

        Page<Target> newPage = new Page<>(sourcePage.getCurrent(), sourcePage.getSize());
        newPage.setTotal(sourcePage.getTotal());
        newPage.setRecords(targetList);
        return newPage;
    }
}
