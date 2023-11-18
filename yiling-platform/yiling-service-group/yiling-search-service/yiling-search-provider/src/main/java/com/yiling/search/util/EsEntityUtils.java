package com.yiling.search.util;

import java.util.List;

import com.google.common.collect.Lists;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.framework.common.enums.SortEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.search.flow.dto.EsScrollDTO;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchScrollHits;


/**
 * @author shichen
 * @类名 EsEntityUtils
 * @描述
 * @创建时间 2022/8/25
 * @修改人 shichen
 * @修改时间 2022/8/25
 **/
public class EsEntityUtils {

    public static <Source, Target> EsAggregationDTO<Target> toEsAggregation(SearchHits<Source> searchHits, Class<Target> targetClass
            , Integer current, Integer size) {
        if (null == searchHits) {
            return null;
        }
        List<Source> list = Lists.newArrayList();
        searchHits.getSearchHits().forEach(hit -> {
            list.add(hit.getContent());
        });
        EsAggregationDTO<Target> esAggregation = new EsAggregationDTO<>();
        esAggregation.setTotal(searchHits.getTotalHits());
        esAggregation.setData(PojoUtils.map(list, targetClass));
        esAggregation.setCurrent(current);
        esAggregation.setSize(size);
        return esAggregation;
    }

    public static <Source, Target> EsScrollDTO<Target> toEsScroll(SearchScrollHits<Source> searchHits, Class<Target> targetClass) {
        if (null == searchHits) {
            return null;
        }
        List<Source> list = Lists.newArrayList();
        searchHits.getSearchHits().forEach(hit -> {
            list.add(hit.getContent());
        });
        EsScrollDTO<Target> esAggregation = new EsScrollDTO<>();
        esAggregation.setTotal(searchHits.getTotalHits());
        esAggregation.setData(PojoUtils.map(list, targetClass));
        esAggregation.setScrollId(searchHits.getScrollId());
        return esAggregation;
    }

    public static FieldSortBuilder searchSort(String sortField, SortEnum sortEnum) {
        FieldSortBuilder sortBuilder;
        if (StringUtils.isNotBlank(sortField)) {
            sortBuilder = SortBuilders.fieldSort(sortField);
        } else {
            sortBuilder = SortBuilders.fieldSort("_score");
        }
        if (null == sortEnum || SortEnum.DESC.equals(sortEnum)) {
            sortBuilder.order(SortOrder.DESC);
        } else {
            sortBuilder.order(SortOrder.ASC);
        }
        return sortBuilder;
    }
}
