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
package com.feilong.taglib.display.httpconcat.builder;

import static com.feilong.taglib.display.httpconcat.builder.HttpConcatGlobalConfigBuilder.GLOBAL_CONFIG;

/**
 * A factory for creating Template objects.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public final class TemplateFactory{

    /** css <code>{@value}</code>. */
    public static final String TYPE_CSS = "css";

    /** js <code>{@value}</code>. */
    public static final String TYPE_JS  = "js";

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private TemplateFactory(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 不同的type不同的模板.
     * 
     * @param type
     *            类型 {@link #TYPE_CSS} 以及{@link #TYPE_JS}
     *
     * @return 目前仅支持 {@link #TYPE_CSS} 以及{@link #TYPE_JS},其余不支持,会抛出
     *         {@link UnsupportedOperationException}
     */
    public static String getTemplate(String type){
        if (TYPE_CSS.equalsIgnoreCase(type)){
            return GLOBAL_CONFIG.getTemplateCss();
        }

        if (TYPE_JS.equalsIgnoreCase(type)){
            return GLOBAL_CONFIG.getTemplateJs();
        }

        //---------------------------------------------------------------
        throw new UnsupportedOperationException("type:[" + type + "] not support!,current time,only support [js] or [css]");
    }
}
