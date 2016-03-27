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
package com.feilong.taglib.functions;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * Static methods for JSP EL expression functions.
 * 
 * <h3>does-el-support-overloaded-methods?</h3>
 * 
 * <blockquote>
 * <p>
 * It'll always be the first method of the {@link Class#getMethods()} array whose name (and amount of arguments) matches the EL method call.
 * Whether it returns the same method everytime or not depends on the JVM make/version used.
 * </p>
 * 
 * <p>
 * Perhaps you made a Java SE upgrade in the meanwhile as well. The javadoc even says this:
 * </p>
 * 
 * <p style="color:green">
 * The elements in the array returned are not sorted and are not in any particular order.
 * </p>
 * 
 * <p>
 * You should not rely on unspecified behaviour. Give them a different name.
 * </p>
 * 
 * </blockquote>
 *
 * @author feilong
 * @version 1.4.0 2015年8月11日 下午6:29:37
 * @see org.apache.taglibs.standard.functions.Functions
 * @see "org.owasp.esapi.tags.ELEncodeFunctions"
 * 
 * @see <a href="http://stackoverflow.com/questions/9763619/does-el-support-overloaded-methods">does-el-support-overloaded-methods</a>
 * @since 1.4.0
 */
public final class ELFunctions{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ELFunctions.class);

    /** Don't let anyone instantiate this class. */
    private ELFunctions(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 用于 自定义标签/ 自定义el.
     * 
     * <p style="color:red">
     * 注意,比较的是 {@link java.lang.Object#toString()}
     * </p>
     * 
     * @param collection
     *            一个集合,将会被转成Iterator,可以为逗号隔开的字符串,会被分隔成Iterator.
     * @param value
     *            任意类型的值,最终toString 判断比较.
     * @return true, if successful
     * @see ConvertUtil#toIterator(Object)
     * @see CollectionsUtil#contains(Iterator, Object)
     * @see org.apache.taglibs.standard.tag.common.core.ForEachSupport#supportedTypeForEachIterator(Object)
     */
    public static boolean contains(Object collection,Object value){
        Iterator<?> iterator = ConvertUtil.toIterator(collection);
        return CollectionsUtil.contains(iterator, value);
    }

    /**
     * 将对象format成json字符串(不会有pretty输出,会连在一起).
     *
     * @param obj
     *            the obj
     * @return the string
     * @see com.feilong.tools.jsonlib.JsonUtil#format(Object, int, int)
     */
    public static String toJsonString(Object obj){
        try{
            return JsonUtil.format(obj, 0, 0);
        }catch (Exception e){
            LOGGER.error("json format:" + obj.toString(), e);
        }
        //此方法应用于jsp标签,如果抛出异常,可能页面不能持续渲染, 但是不会显示异常页面, 因此,此处,直接返回null
        return StringUtils.EMPTY;
    }
}
