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
package com.feilong.taglib.display.pager.command;

import java.io.Serializable;

/**
 * {@link Pager} && 分页解析的html 代码.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @since 1.4.0
 */
public class PagerAndContent<T> implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4989412502718346094L;

    /** The pager. */
    private Pager<T>          pager;

    /** The html. */
    private String            content;

    /**
     * The Constructor.
     */
    public PagerAndContent(){
        super();
    }

    /**
     * The Constructor.
     *
     * @param pager
     *            the pager
     * @param content
     *            the content
     */
    public PagerAndContent(Pager<T> pager, String content){
        super();
        this.pager = pager;
        this.content = content;
    }

    /**
     * 获得 the pager.
     *
     * @return the pager
     */
    public Pager<T> getPager(){
        return pager;
    }

    /**
     * 设置 the pager.
     *
     * @param pager
     *            the pager to set
     */
    public void setPager(Pager<T> pager){
        this.pager = pager;
    }

    /**
     * 获得 the html.
     *
     * @return the content
     */
    public String getContent(){
        return content;
    }

    /**
     * 设置 the html.
     *
     * @param content
     *            the content to set
     */
    public void setContent(String content){
        this.content = content;
    }
}
