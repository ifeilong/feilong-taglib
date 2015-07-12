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
package com.feilong.taglib.base;

/**
 * 输出内容的标签.
 *
 * @author feilong
 * @version 1.0.0 2009-5-2下午05:20:22
 * @version 1.0.3 2012-3-13 上午1:59:22
 * @version 1.2.1 2015年6月12日 下午3:33:05
 * @see com.feilong.taglib.base.BaseTag
 * @since 1.0.0
 */
public abstract class AbstractWriteContentTag extends BaseTag{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8215127553271356734L;

    /**
     * 标签开始.
     *
     * @return the int
     */
    @Override
    public int doStartTag(){
        // 开始执行的部分
        print(this.writeContent());
        // 开始:跳过了开始和结束标签之间的代码。
        return SKIP_BODY;
    }

    // *******************************************************************
    /**
     * 标签体内容.
     *
     * @return the object
     */
    protected abstract Object writeContent();
}