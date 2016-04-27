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
package com.feilong.taglib.display.barcode;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.tools.jsonlib.JsonUtil;

/**
 * 基于session的寄存器实现.
 *
 * @author feilong
 * @version 1.5.4 2016年4月27日 下午4:12:37
 * @since 1.5.4
 */
public class SessionAccessor implements Accessor{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionAccessor.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.taglib.display.barcode.Accessor#save(java.lang.String, java.io.Serializable, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void save(String key,Serializable serializable,HttpServletRequest request){
        request.getSession().setAttribute(key, serializable);

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("setAttribute to session,key is:{},value is:{}", key, JsonUtil.format(serializable));
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.taglib.display.barcode.Accessor#get(java.lang.String, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public <T extends Serializable> T get(String key,HttpServletRequest request){
        @SuppressWarnings("unchecked")
        T t = (T) request.getSession().getAttribute(key);

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("when key is :[{}],get t:{}", key, JsonUtil.format(t));
        }

        return t;
    }
}
