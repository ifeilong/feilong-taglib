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
package com.feilong.taglib.display.option;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.feilong.core.Validator;
import com.feilong.core.util.ResourceBundleUtil;
import com.feilong.taglib.display.TagCacheManager;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * The Class OptionBuilder.
 *
 * @author feilong
 * @version 1.5.4 2016年4月19日 上午2:01:44
 * @since 1.5.4
 */
public class OptionBuilder{

    /** <code>{@value}</code>. */
    private static final String OPTION_PATTERN = "<option value=\"{}\"{}>{}</option>";

    /**
     * Builds the options.
     *
     * @param optionParam
     *            the option param
     * @return the string
     */
    public static String buildContent(OptionParam optionParam){
        String content = TagCacheManager.getContentFromCache(optionParam);
        if (Validator.isNotNullOrEmpty(content)){
            return content;
        }

        content = buildContentMain(optionParam);
        TagCacheManager.put(optionParam, content);
        return content;
    }

    /**
     * Builds the content main.
     *
     * @param optionParam
     *            the option param
     * @return the string
     */
    private static String buildContentMain(OptionParam optionParam){
        Map<String, String> map = getKeyValueMap(optionParam);
        return render(map, optionParam);
    }

    /**
     * 获得 key value map.
     *
     * @param optionParam
     *            the option param
     * @return the key value map
     */
    private static Map<String, String> getKeyValueMap(OptionParam optionParam){
        Map<String, String> map = ResourceBundleUtil.readAllPropertiesToMap(optionParam.getBaseName(), optionParam.getLocale());
        //XXX 排序
        return map;
    }

    /**
     * Render.
     *
     * @param map
     *            the map
     * @param optionParam
     * @return the object
     */
    private static String render(Map<String, String> map,OptionParam optionParam){
        StringBuilder sb = new StringBuilder();

        String selectedKey = optionParam.getSelectedKey();

        for (Map.Entry<String, String> entry : map.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();

            String selectedStatus = key.equals(selectedKey) ? " selected=\"selected\"" : StringUtils.EMPTY;

            String option = Slf4jUtil.formatMessage(OPTION_PATTERN, key, selectedStatus, value);
            sb.append(option).append(SystemUtils.LINE_SEPARATOR);
        }
        return sb.toString();
    }
}
