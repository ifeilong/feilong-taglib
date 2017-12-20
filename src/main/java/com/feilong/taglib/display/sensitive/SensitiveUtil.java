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
package com.feilong.taglib.display.sensitive;

import static com.feilong.core.Validator.isNullOrEmpty;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JsonUtil;

/**
 * 敏感词工具类.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.1
 */
public class SensitiveUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveUtil.class);

    /** Don't let anyone instantiate this class. */
    private SensitiveUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 转换.
     *
     * @param value
     *            the value
     * @param sensitiveConfig
     *            the sensitive config
     * @return 如果 <code>value</code> 是null,直接返回<code>value</code><br>
     *         如果 <code>value</code> 是blank,直接返回<code>value</code><br>
     *         如果 <code>sensitiveConfig</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>sensitiveConfig.getSensitiveType()</code> 是null,抛出 {@link NullPointerException}<br>
     */
    public static String parse(String value,SensitiveConfig sensitiveConfig){
        if (isNullOrEmpty(value)){
            return value;
        }
        Validate.notNull(sensitiveConfig, "sensitiveConfig can't be null!");

        SensitiveType sensitiveType = sensitiveConfig.getSensitiveType();
        Validate.notNull(sensitiveType, "sensitiveConfig.getSensitiveType() can't be null!");

        if (LOGGER.isTraceEnabled()){
            LOGGER.trace("value:[{}],sensitiveConfig:{}", value, JsonUtil.format(sensitiveConfig));
        }

        if (sensitiveType.isNoNeedMask(value)){
            return value;
        }

        //---------------------------------------------------------------
        Integer[] leftAndRightNoMaskLengths = sensitiveType.getLeftAndRightNoMaskLengths(value);

        //左边不需要mask的长度
        int leftNoMaskLength = leftAndRightNoMaskLengths[0];

        //右边不需要mask的长度
        int rightNoMaskLength = leftAndRightNoMaskLengths[1];

        //---------------------------------------------------------------
        StringBuilder sb = new StringBuilder();

        sb.append(StringUtils.left(value, leftNoMaskLength));
        sb.append(StringUtils.repeat(sensitiveConfig.getMaskChar(), value.length() - leftNoMaskLength - rightNoMaskLength));
        sb.append(StringUtils.right(value, rightNoMaskLength));

        return sb.toString();
    }
}
