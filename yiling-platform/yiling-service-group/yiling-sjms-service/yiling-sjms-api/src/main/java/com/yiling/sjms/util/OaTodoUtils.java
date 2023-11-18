package com.yiling.sjms.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.gb.enums.GbFormBizTypeEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.net.URLEncoder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HtmlUtil;

/**
 * OA待办工具类
 *
 * @author: xuan.zhou
 * @date: 2023/2/15
 */
public class OaTodoUtils {

    /**
     * 生成团购业务OA待办事项标题 <br/>
     * 示例："团购提报-计井一-2023-01-09（TG20230109113200）"
     *
     * @param typeEnum 团购表单业务类型枚举
     * @param createUserName 创建人姓名
     * @param gbNo 团购编号
     * @param createTime 团购创建时间
     * @return java.lang.String
     * @author xuan.zhou
     * @date 2023/2/15
     **/
    public static final String genGbTitle(GbFormBizTypeEnum typeEnum, String createUserName, String gbNo, Date createTime) {
        StringBuffer sb = new StringBuffer();
        return sb.append(typeEnum.getName())
                .append("-")
                .append(createUserName)
                .append("-")
                .append(DateUtil.format(createTime, "yyyy-MM-dd"))
                .append("（")
                .append(gbNo)
                .append("）")
                .toString();
    }

    /**
     *生成团购业务OA待办事项标题
     * @param name 表单名
     * @param createUserName
     * @param code 单据编号
     * @param createTime
     * @return
     */
    public static final String genTitle(String name, String createUserName, String code, Date createTime) {
        StringBuffer sb = new StringBuffer();
        return sb.append(name)
                .append("-")
                .append(createUserName)
                .append("-")
                .append(DateUtil.format(createTime, "yyyy-MM-dd"))
                .append("（")
                .append(code)
                .append("）")
                .toString();
    }

    /**
     * 生成PC待办链接
     *
     * @param ssoUrl OA单点登录链接
     * @param redirectUrl 跳转地址
     * @param formTypeEnum 流程ID
     * @param formId 任务ID
     * @param forwardHistoryId 转发历史记录ID
     * @return java.lang.String
     * @author xuan.zhou
     * @date 2023/3/3
     **/
    public static final String genPcUrl(String ssoUrl, String redirectUrl, FormTypeEnum formTypeEnum, Long formId, Long forwardHistoryId) {
        StringBuffer sb = new StringBuffer();
        return sb.append(ssoUrl)
                .append("?redirectUrl=")
                .append(URLEncoder.createAll().encode(getRedirectUrl(redirectUrl, formTypeEnum, formId, Convert.toStr(forwardHistoryId)), Charset.defaultCharset()))
                .append("&type=1")
                .toString();
    }

    /**
     * 生成APP待办链接
     *
     * @param ssoUrl OA单点登录链接
     * @param redirectUrl 跳转地址
     * @param formTypeEnum 流程ID
     * @param formId 任务ID
     * @param forwardHistoryId 转发历史记录ID
     * @return java.lang.String
     * @author xuan.zhou
     * @date 2023/4/12
     **/
    public static final String genAppUrl(String ssoUrl, String redirectUrl, FormTypeEnum formTypeEnum, Long formId, Long forwardHistoryId) {
        StringBuffer sb = new StringBuffer();
        return sb.append(ssoUrl)
                .append("?redirectUrl=")
                .append(URLEncoder.createAll().encode(getRedirectUrl(redirectUrl, formTypeEnum, formId, Convert.toStr(forwardHistoryId)), Charset.defaultCharset()))
                .append("&type=2")
                .toString();
    }

    private static String getRedirectUrl(String redirectUrl, FormTypeEnum formTypeEnum, Long formId, String forwardHistoryId) {
        Map<String, String> params = new HashMap<>();
        params.put("type", formTypeEnum.getCode().toString());
        params.put("formId", formId.toString());
        params.put("forwardHistoryId", forwardHistoryId);

        return format(redirectUrl, params);
    }

    public static String format(String text, Map<String, String> params) {
        if (CollUtil.isEmpty(params)) {
            return text;
        }

        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            text = StrUtil.replace(text, "{" + entry.getKey() + "}", entry.getValue());
        }

        return text;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String s = "https://sjms-test.59yi.com/#/transfer?type=7&formId=3206&forwardHistoryId=0";
        String s1 = URLUtil.encode(s);
        System.out.println(URLUtil.encode(s));
        s1 = URLEncoder.createAll().encode(s, Charset.defaultCharset());
        System.out.println(s1);
        s1 = java.net.URLEncoder.encode(s, "utf-8");
        System.out.println(s1);
        s1 = URLDecoder.decode(s1, Charset.defaultCharset());
        System.out.println(s1);
        s1 = "https://sjms-test.59yi.com/#/transfer?type=7&amp;formId=3246&amp;forwardHistoryId=0";
        System.out.println(HtmlUtil.unescape(s1));
    }
}
