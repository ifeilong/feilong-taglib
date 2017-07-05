/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.taglib.display.httpconcat;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.taglib.display.httpconcat.HttpConcatConstants.TYPE_CSS;
import static com.feilong.taglib.display.httpconcat.HttpConcatConstants.TYPE_JS;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.LF;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.lang.StringUtil;
import com.feilong.core.util.RegexUtil;

/**
 * 专门用来提取标签体内容的.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public class ItemSrcListResolver{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemSrcListResolver.class);

    /** Don't let anyone instantiate this class. */
    private ItemSrcListResolver(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 获得 items array.
     *
     * @param blockContent
     *            内容,目前 以 \n 分隔
     * @param type
     *            the type
     * @param domain
     *            the domain
     * @return 如果 <code>blockContent</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>blockContent</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>type</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>type</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static List<String> resolve(String blockContent,String type,String domain){
        Validate.notBlank(blockContent, "blockContent can't be blank!");
        Validate.notBlank(type, "type can't be blank!");

        //---------------------------------------------------------------
        String[] items = StringUtil.split(blockContent.trim(), LF);

        int length = items.length;

        //---------------------------------------------------------------
        List<String> list = new ArrayList<>(length);
        for (int i = 0; i < length; ++i){
            String item = items[i];
            if (isNullOrEmpty(item)){// 忽视空行
                continue;
            }

            //---------------------------------------------------------------
            // 去除空格
            String parseResult = parse(item, type, domain);
            if (isNullOrEmpty(parseResult)){
                LOGGER.warn("item parse result is null or empty,[{}]", item);
                continue;
            }

            //---------------------------------------------------------------
            list.add(parseResult);
        }
        return list;
    }

    /**
     * 解析 item 里面的内容.
     * 
     * <ul>
     * <li>item 不能是 blank</li>
     * <li>如果 type 是css, 并且以 {@code "<link "} 开头,那么将提取href 里面的内容,并且去除 domain, 去除 {@code ?} 后面的部分内容</li>
     * <li>如果 type 是js, 并且以 {@code "<script "} 开头,那么将提取src 里面的内容,并且去除 domain, 去除 {@code ?} 后面的部分内容</li>
     * <li>如果都不是,那么直接返回 trim 过的 item</li>
     * </ul>
     *
     * @param item
     *            the item
     * @param type
     *            the type
     * @param domain
     *            the domain
     * @return 如果 <code>item</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>item</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>type</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>type</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    static String parse(String item,String type,String domain){
        Validate.notBlank(item, "item can't be blank!");
        Validate.notBlank(type, "type can't be blank!");

        //---------------------------------------------------------------
        String workItem = item.trim();

        if (TYPE_CSS.equalsIgnoreCase(type) && workItem.startsWith("<link ")){
            String regexPattern = ".*?href=\"(.*?)\".*?";
            return pickUp(workItem, regexPattern, domain);
        }

        //---------------------------------------------------------------

        if (TYPE_JS.equalsIgnoreCase(type) && workItem.startsWith("<script ")){
            String regexPattern = ".*?src=\"(.*?)\".*?";
            return pickUp(workItem, regexPattern, domain);
        }

        return workItem;
    }

    /**
     * 基于正则表达式来提取内部的路径地址.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ul>
     * <li>如果提取出来的内容是以 {@code domain} 开头的, 将会去除</li>
     * <li>如果提取出来的内容有 {@code ?} 部分, 将会去除</li>
     * </ul>
     * </blockquote>
     *
     * @param workItem
     *            the work item
     * @param regexPattern
     *            the regex pattern
     * @param domain
     *            the domain
     * @return 如果 <code>workItem</code> 不符合regexPattern,那么返回 {@link StringUtils#EMPTY}<br>
     */
    private static String pickUp(String workItem,String regexPattern,String domain){
        String value = RegexUtil.group(regexPattern, workItem, 1);
        if (isNullOrEmpty(value)){
            return EMPTY;
        }
        value = value.trim(); //去空

        //------------去除 domain---------------------------------------------------
        if (value.startsWith(domain)){
            value = StringUtils.substringAfter(value, domain);
        }

        //------------去除 ?---------------------------------------------------
        if (value.contains("?")){
            return StringUtils.substringBefore(value, "?");
        }

        return value;
    }
}
