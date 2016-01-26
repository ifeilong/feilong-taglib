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

import com.feilong.core.tools.slf4j.Slf4jUtil;
import com.feilong.core.util.ResourceBundleUtil;
import com.feilong.core.util.Validator;
import com.feilong.taglib.display.httpconcat.command.HttpConcatGlobalConfig;

/**
 * The Class HttpConcatGlobalConfigBuilder.
 *
 * @author feilong
 * @version 1.5.0 2016年1月26日 下午5:16:59
 * @since 1.5.0
 */
public class HttpConcatGlobalConfigBuilder{

    /** 配置文件 <code>{@value}</code>. */
    //FIXME support different environment
    public static final String CONFIG_FILE                  = "config/httpconcat";

    /** <code>{@value}</code>. */
    public static final String KEY_HTTPCONCAT_SUPPORT       = "httpconcat.support";

    //**************************************************************

    /** <code>{@value}</code>. */
    public static final String KEY_TEMPLATE_CSS             = "httpconcat.template.css";

    /** <code>{@value}</code>. */
    public static final String KEY_TEMPLATE_JS              = "httpconcat.template.js";

    /** <code>{@value}</code>. */
    public static final String KEY_DEFAULT_CACHE_ENABLE     = "httpconcat.defaultCacheEnable";

    /** <code>{@value}</code>. */
    public static final String KEY_DEFAULT_CACHE_SIZE_LIMIT = "httpconcat.defaultCacheSizeLimit";

    /**
     * Builds the http concat global config.
     *
     * @return the http concat global config
     */
    public static HttpConcatGlobalConfig buildHttpConcatGlobalConfig(){
        HttpConcatGlobalConfig httpConcatGlobalConfig = new HttpConcatGlobalConfig();

        httpConcatGlobalConfig.setHttpConcatSupport(getRequiredValue(KEY_HTTPCONCAT_SUPPORT, Boolean.class));
        httpConcatGlobalConfig.setTemplateCss(getRequiredValue(KEY_TEMPLATE_CSS, String.class));
        httpConcatGlobalConfig.setTemplateJs(getRequiredValue(KEY_TEMPLATE_JS, String.class));

        httpConcatGlobalConfig.setDefaultCacheEnable(getRequiredValue(KEY_DEFAULT_CACHE_ENABLE, Boolean.class));
        httpConcatGlobalConfig.setDefaultCacheSizeLimit(getRequiredValue(KEY_DEFAULT_CACHE_SIZE_LIMIT, Integer.class));

        return httpConcatGlobalConfig;
    }

    /**
     * 获得 required value.
     * 
     * <p>
     * 如果 {@code Validator.isNullOrEmpty(Object)} ,抛出NullPointerException
     * </p>
     * 
     * @param <T>
     *            the generic type
     * @param keyName
     *            the key name
     * @param typeClass
     *            the type class
     * @return the value if not null or empty
     */
    private static <T> T getRequiredValue(String keyName,Class<T> typeClass){
        String baseName = CONFIG_FILE;
        T keyValue = ResourceBundleUtil.getValue(baseName, keyName, typeClass);
        if (Validator.isNullOrEmpty(keyValue)){
            String messagePattern = "can not find key:[{}],pls ensure you have put the correct configuration file path:[{}]";
            throw new NullPointerException(Slf4jUtil.formatMessage(messagePattern, keyName, baseName));
        }
        return keyValue;
    }
}
