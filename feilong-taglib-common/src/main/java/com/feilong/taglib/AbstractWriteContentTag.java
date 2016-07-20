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

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.UncheckedIOException;
import com.feilong.servlet.http.RequestUtil;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.date.DateExtensionUtil.getIntervalForView;

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
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
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

        // 开始执行的部分
        Object writeContent = this.buildContent(request);
        print(writeContent);

        if (LOGGER.isDebugEnabled()){
            String buildExtraKeyInfoToLog = buildExtraKeyInfoToLog();
            String tagLog = isNullOrEmpty(buildExtraKeyInfoToLog) ? "" : "," + buildExtraKeyInfoToLog;
            String useTime = getIntervalForView(beginDate);
            LOGGER.debug("[{}],[{}]{},use time:[{}]", getClass().getSimpleName(), RequestUtil.getRequestURL(request), tagLog, useTime);
        }
    }

    /**
     * 标签体内容.
     *
     * @param request
     *            the request
     * @return the object
     */
    protected abstract Object buildContent(HttpServletRequest request);

    /**
     * 额外的关键的信息,用在log里面显示, 比如面包屑 可以放itemId=xxx.
     *
     * @return the string
     */
    protected String buildExtraKeyInfoToLog(){
        return EMPTY;
    }

    // *******************************************************************

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
}