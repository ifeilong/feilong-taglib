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

/**
 * 敏感词配置相关.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.1
 */
public class SensitiveConfig{

    /** 敏感词类型. */
    private SensitiveType sensitiveType;

    /** 掩码字符,比如*,#等,默认是*. */
    private char          maskChar = '*';

    //---------------------------------------------------------------
    /**
     * Instantiates a new sensitive config.
     *
     * @param sensitiveType
     *            敏感词类型
     */
    public SensitiveConfig(SensitiveType sensitiveType){
        this.sensitiveType = sensitiveType;
    }

    /**
     * Instantiates a new sensitive config.
     *
     * @param sensitiveType
     *            敏感词类型
     * @param maskChar
     *            掩码字符,比如*,#等,默认是*
     */
    public SensitiveConfig(SensitiveType sensitiveType, char maskChar){
        this.sensitiveType = sensitiveType;
        this.maskChar = maskChar;
    }

    //---------------------------------------------------------------

    /**
     * 获得 敏感词类型.
     *
     * @return the 敏感词类型
     */
    public SensitiveType getSensitiveType(){
        return sensitiveType;
    }

    /**
     * 设置 敏感词类型.
     *
     * @param sensitiveType
     *            the new 敏感词类型
     */
    public void setSensitiveType(SensitiveType sensitiveType){
        this.sensitiveType = sensitiveType;
    }

    /**
     * 获得 掩码字符,比如*,#等,默认是*.
     *
     * @return the 掩码字符,比如*,#等,默认是*
     */
    public char getMaskChar(){
        return maskChar;
    }

    /**
     * 设置 掩码字符,比如*,#等,默认是*.
     *
     * @param maskChar
     *            the new 掩码字符,比如*,#等,默认是*
     */
    public void setMaskChar(char maskChar){
        this.maskChar = maskChar;
    }
}
