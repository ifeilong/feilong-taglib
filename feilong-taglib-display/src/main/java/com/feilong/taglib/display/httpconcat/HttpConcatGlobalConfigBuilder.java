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

import com.feilong.core.util.ResourceBundleUtil;
import com.feilong.taglib.display.httpconcat.command.HttpConcatGlobalConfig;
import com.feilong.tools.slf4j.Slf4jUtil;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.convert;

/**
 * The Class HttpConcatGlobalConfigBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.0
 */
public class HttpConcatGlobalConfigBuilder{

    /** 配置文件 <code>{@value}</code>. */
    //XXX support different environment
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

    /** Don't let anyone instantiate this class. */
    private HttpConcatGlobalConfigBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

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
     * 如果 {@code isNullOrEmpty(Object)} ,抛出NullPointerException
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
        return getRequiredValue(CONFIG_FILE, keyName, typeClass);
    }

    /**
     * Gets the required value.
     *
     * @param <T>
     *            the generic type
     * @param baseName
     *            the base name
     * @param keyName
     *            the key name
     * @param typeClass
     *            the type class
     * @return the required value
     * @since 1.8.1
     */
    private static <T> T getRequiredValue(String baseName,String keyName,Class<T> typeClass){
        String value = ResourceBundleUtil.getValue(baseName, keyName);
        if (isNullOrEmpty(value)){
            String messagePattern = "can't find key:[{}] in file:[{}],pls ensure you have put the correct configuration";
            throw new NullPointerException(Slf4jUtil.format(messagePattern, keyName, baseName));
        }
        return convert(value, typeClass);
    }
}
