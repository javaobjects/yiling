package com.yiling.user.common.util;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.yiling.user.common.util.bean.After;
import com.yiling.user.common.util.bean.Before;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.In;
import com.yiling.user.common.util.bean.Like;
import com.yiling.user.common.util.bean.Not;
import com.yiling.user.common.util.bean.NotIn;
import com.yiling.user.common.util.bean.Order;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * QueryWrapper查询工具类
 *
 * @author lun.yu
 * @date 2022-04-07
 */
@Slf4j
public class WrapperUtils {

    private static final PropertyNamingStrategy.SnakeCaseStrategy SNAKE_CASE_STRATEGY = new PropertyNamingStrategy.SnakeCaseStrategy();

    public static <T> QueryWrapper<T> getWrapper(Object obj) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                Object value = f.get(obj);
                String name = f.getName();
                name = SNAKE_CASE_STRATEGY.translate(name);
                Order order = f.getDeclaredAnnotation(Order.class);
                if (order != null) {
                    if (order.asc()) {
                        queryWrapper.orderByAsc(name);
                    } else {
                        queryWrapper.orderByDesc(name);
                    }
                }

                if (!StringUtils.isEmpty(value)) {
                    Like like = f.getDeclaredAnnotation(Like.class);
                    Before before = f.getDeclaredAnnotation(Before.class);
                    After after = f.getDeclaredAnnotation(After.class);
                    NotIn notIn = f.getDeclaredAnnotation(NotIn.class);
                    Not not = f.getDeclaredAnnotation(Not.class);
                    In in = f.getDeclaredAnnotation(In.class);
                    Eq eq = f.getDeclaredAnnotation(Eq.class);
                    if (like != null) {
                        queryWrapper.like(name, value);

                    } else if (before != null) {
                        if (f.getType().equals(Date.class)) {
                            queryWrapper.ge(before.name(), before.beginOfDay() ? DateUtil.beginOfDay((Date) value) : value);
                        }

                    } else if (after != null) {
                        if (f.getType().equals(Date.class)) {
                            queryWrapper.le(after.name(), after.endOfDay() ? DateUtil.endOfDay((Date) value) : value);
                        }

                    } else if (not != null) {
                        queryWrapper.ne(name, value);

                    } else if (notIn != null) {

                        if (f.getType().equals(List.class) && ((List) value).size() > 0) {

                            queryWrapper.notIn(StrUtil.isNotEmpty(notIn.name()) ? notIn.name() : name, (List) value);

                        } else if (f.getType().isArray() && CollectionUtils.arrayToList(value).size() > 0 ) {

                            queryWrapper.notIn(StrUtil.isNotEmpty(notIn.name()) ? notIn.name() : name, CollectionUtils.arrayToList(value));
                        }

                    } else if (in != null) {

                        if (f.getType().equals(List.class) && ((List) value).size() > 0) {

                            queryWrapper.in(StrUtil.isNotEmpty(in.name()) ? in.name() : name, (List) value);

                        } else if (f.getType().isArray() && CollectionUtils.arrayToList(value).size() > 0 ) {

                            queryWrapper.in(StrUtil.isNotEmpty(in.name()) ? in.name() : name, CollectionUtils.arrayToList(value));
                        }

                    } else if (eq != null) {
                        if (f.getType().equals(Long.class)) {
                            if (eq.notZero() && (Long) value == 0L) {
                                continue;
                            }
                        } else if (f.getType().equals(Integer.class)) {
                            if (eq.notZero() && (Integer) value == 0) {
                                continue;
                            }
                        }

                        queryWrapper.eq(name, value);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            log.error("", e);
        }
        return queryWrapper;
    }
}
