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
package com.feilong.taglib.display.pager;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.taglib.display.pager.command.PagerConstants;

/**
 * 分页helper.
 *
 * @author feilong
 * @version 1.4.0 2015年8月20日 下午9:53:48
 * @since 1.4.0
 */
public final class PagerHelper{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PagerHelper.class);

    /**
     * 获得当前分页数字,不带这个参数 或者转换异常 返回1.
     * 
     * @param request
     *            当前请求
     * @param pageParamName
     *            分页参数名称 see {@link PagerConstants#DEFAULT_PAGE_PARAM_NAME}
     * @return <ul>
     *         <li>请求参数中,分页参数值 Integer 类型</li>
     *         <li>如果参数中不带这个分页参数,或者转换异常 返回1</li>
     *         </ul>
     * @see PagerConstants#DEFAULT_PAGE_PARAM_NAME
     */
    public static Integer getCurrentPageNo(HttpServletRequest request,String pageParamName){
        // /s/s-t-b-f-a-cBlack-s-f-p-gHeat+Gear-e-i-o.htm?keyword=&pageNo=%uFF1B
        String pageNoString = RequestUtil.getParameter(request, pageParamName);
        try{
            Integer pageNo = ConvertUtil.toInteger(pageNoString);
            return null == pageNo ? 1 : pageNo;
        }catch (Exception e){
            // 抛出异常, 但是不给 currentPageNo 赋值
            LOGGER.error(e.getClass().getName(), e);
        }
        return 1; // 不带这个参数 或者转换异常 返回1
    }
}
