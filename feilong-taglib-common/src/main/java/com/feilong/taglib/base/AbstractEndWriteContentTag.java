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
package com.feilong.taglib.base;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.tools.jsonlib.JsonUtil;
import com.feilong.core.tools.slf4j.Slf4jUtil;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.servlet.http.builder.RequestLogSwitch;

/**
 * end 输出.
 *
 * @author feilong
 * @version 1.3.0 2015年7月23日 下午9:03:41
 * @since 1.3.0
 */
public abstract class AbstractEndWriteContentTag extends AbstractWriteContentTag{

    private static final long   serialVersionUID = -3979342234682529223L;

    /** The Constant log. */
    private static final Logger LOGGER           = LoggerFactory.getLogger(AbstractEndWriteContentTag.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.taglib.base.AbstractCommonTag#doStartTag()
     */
    @Override
    public int doStartTag(){
        //Request the creation of new buffer, a BodyContent on which to evaluate the body of this tag. Returned from doStartTag when it implements BodyTag. This is an illegal return value for doStartTag when the class does not implement BodyTag.
        return EVAL_BODY_BUFFERED;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException{
        try{
            execute();
        }catch (Exception e){
            //XXX 默认处理异常,让页面正常执行,但是以错误log显示
            String formatMessage = Slf4jUtil.formatMessage(
                            "request info:{},tag is:[{}]",
                            JsonUtil.format(RequestUtil.getRequestInfoMapForLog(
                                            getHttpServletRequest(),
                                            RequestLogSwitch.NORMAL_WITH_IDENTITY_INCLUDE_FORWARD)),
                            getClass().getSimpleName());
            LOGGER.error(formatMessage, e);
        }
        return EVAL_PAGE;
    }
}