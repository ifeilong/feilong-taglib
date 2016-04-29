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
package com.feilong.taglib;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.UncheckedIOException;
import com.feilong.core.date.DateExtensionUtil;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.servlet.http.entity.RequestLogSwitch;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * 输出内容的标签.
 * 
 * <h3>可以使用的方法:</h3>
 * 
 * <blockquote>
 * <ul>
 * <li>{@link #print(Object)}</li>
 * <li>{@link #println(Object)}</li>
 * </ul>
 * </blockquote>
 *
 * @author feilong
 * @version 1.0.0 2009-5-2下午05:20:22
 * @version 1.0.3 2012-3-13 上午1:59:22
 * @version 1.2.1 2015年6月12日 下午3:33:05
 * @version 1.2.2 2015-7-17 00:24 add time monitor
 * @version 1.2.3 2015-7-23 21:21 update Access Modifier to default
 * @see com.feilong.taglib.BaseTag
 * @since 1.0.0
 */
//默认修饰符号 限制访问
abstract class AbstractWriteContentTag extends BaseTag{

    /** The Constant log. */
    private static final Logger LOGGER           = LoggerFactory.getLogger(AbstractWriteContentTag.class);

    /** The Constant serialVersionUID. */
    private static final long   serialVersionUID = 8215127553271356734L;

    /**
     * Execute.
     */
    protected void execute(){
        Date beginDate = new Date();

        HttpServletRequest request = getHttpServletRequest();

        if (LOGGER.isDebugEnabled()){
            RequestLogSwitch requestLogSwitch = RequestLogSwitch.NORMAL;
            Map<String, Object> requestInfoMapForLog = RequestUtil.getRequestInfoMapForLog(request, requestLogSwitch);
            LOGGER.debug("class:[{}],request info:{}", getClass().getSimpleName(), JsonUtil.format(requestInfoMapForLog));
        }

        // 开始执行的部分
        Object writeContent = this.writeContent();
        print(writeContent);

        Date endDate = new Date();
        LOGGER.info(
                        "[{}],[{}],{},use time:[{}]",
                        getClass().getSimpleName(),
                        request.getRequestURI(),
                        useTimeLog(),
                        DateExtensionUtil.getIntervalForView(beginDate, endDate));
    }

    /**
     * 将文字输出到页面.
     *
     * @param object
     *            the object
     * @since 1.5.3 move from {@link BaseTag}
     */
    protected void print(Object object){
        JspWriter jspWriter = pageContext.getOut();
        try{
            jspWriter.print(object);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 将文字输出到页面.
     *
     * @param object
     *            the object
     * @since 1.5.3 move from {@link BaseTag}
     */
    protected void println(Object object){
        JspWriter jspWriter = pageContext.getOut();
        try{
            jspWriter.println(object.toString());
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 耗时时间.
     *
     * @return the string
     */
    protected String useTimeLog(){
        return "";
    }

    // *******************************************************************
    /**
     * 标签体内容.
     *
     * @return the object
     */
    protected abstract Object writeContent();
}