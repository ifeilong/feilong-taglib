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
package com.feilong.taglib.common;

import java.util.Iterator;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.util.IteratorUtil;
import com.feilong.taglib.AbstractConditionalTag;

/**
 * 判断一个值是否在一个集合(或者可以被转成Iterator)当中.
 *
 * @author feilong
 * @version 1.0.0 2010-7-5 下午01:16:43
 */
public class IsContainsTag extends AbstractConditionalTag{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 472866723265315380L;

    /** 一个集合,将会被转成Iterator,可以为逗号隔开的字符串,会被分隔成Iterator. */
    private Object            collection       = null;

    /** 任意类型的值,最终toString 判断比较. */
    private Object            value            = null;

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.taglib.base.AbstractConditionalTag#condition()
     */
    @Override
    public boolean condition(){
        Iterator<?> iterator = ConvertUtil.toIterator(collection);
        return IteratorUtil.containsByStringValue(iterator, value);
    }

    /**
     * Sets the 一个集合,将会被转成Iterator,可以为逗号隔开的字符串,会被分隔成Iterator.
     * 
     * @param collection
     *            the collection to set
     */
    public void setCollection(Object collection){
        this.collection = collection;
    }

    /**
     * Sets the 一个值.
     * 
     * @param value
     *            the value to set
     */
    public void setValue(Object value){
        this.value = value;
    }
}
