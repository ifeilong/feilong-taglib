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

import com.feilong.core.util.ResourceBundleUtil;
import com.feilong.taglib.display.TagCacheManager;
import com.feilong.tools.slf4j.Slf4jUtil;

import static com.feilong.core.Validator.isNotNullOrEmpty;

/**
 * The Class OptionBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.4
 */
public class OptionBuilder{

    /** option 格式 <code>{@value}</code>. */
    private static final String OPTION_PATTERN  = "<option value=\"{}\"{}>{}</option>";

    /** 选中的字符串 <code>{@value}</code>. */
    private static final String SELECTED_STRING = " selected=\"selected\"";

    /** Don't let anyone instantiate this class. */
    private OptionBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * Builds the options.
     *
     * @param optionParam
     *            the option param
     * @return the string
     */
    public static String buildContent(OptionParam optionParam){
        String content = TagCacheManager.getContentFromCache(optionParam);
        if (isNotNullOrEmpty(content)){
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
        return ResourceBundleUtil.readAllPropertiesToMap(optionParam.getBaseName(), optionParam.getLocale());
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

            String selectedStatus = key.equals(selectedKey) ? SELECTED_STRING : StringUtils.EMPTY;

            String option = Slf4jUtil.format(OPTION_PATTERN, key, selectedStatus, value);
            sb.append(option).append(SystemUtils.LINE_SEPARATOR);
        }
        return sb.toString();
    }
}
