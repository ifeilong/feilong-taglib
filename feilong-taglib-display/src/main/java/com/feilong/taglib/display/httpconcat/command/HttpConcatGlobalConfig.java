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
package com.feilong.taglib.display.httpconcat.command;

/**
 * http concat 全局配置.
 *
 * @author feilong
 * @version 1.5.0 2016年1月26日 下午4:58:28
 * @since 1.5.0
 */
public class HttpConcatGlobalConfig{

    /** The template css. */
    private String  templateCss;

    /** The template js. */
    private String  templateJs;

    /** 是否支持 HTTP_CONCAT (全局参数). */
    private Boolean httpConcatSupport;

    /** 设置缓存是否开启. */
    private Boolean defaultCacheEnable;

    /**
     * cache size 限制,仅当 {@link #DEFAULT_CACHEENABLE}开启生效, 当cache数达到 {@link #DEFAULT_CACHESIZELIMIT},将不会再缓存结果
     * 经过测试
     * <ul>
     * <li>300000 size cache占用 内存 :87.43KB(非精准)</li>
     * <li>304850 size cache占用 内存 :87.43KB(非精准)</li>
     * <li>400000 size cache占用 内存 :8.36MB(非精准)</li>
     * </ul>
     * 
     * 对于一个正式项目而言,http concat的cache, size极限大小会是 <blockquote><i>页面总数(P)*页面concat标签数(C)*i18N数(I)*版本号(V)</i></blockquote><br>
     * 如果一个项目 页面有1000个,每个页面有5个concat块,一共有5种国际化语言,如果应用重启前支持5次版本更新,那么计算公式会是 <blockquote><i>1000*5*5*5=50000</i></blockquote>
     * <b>注意:此公式中的页面总数是指,VM/JSP的数量,除非参数不同导致VM/JSP渲染的JS也不同,另当别论</b>
     * 
     * @see org.apache.commons.collections4.map.LRUMap
     */
    private int     defaultCacheSizeLimit;

    /**
     * 获得 the template css.
     *
     * @return the templateCss
     */
    public String getTemplateCss(){
        return templateCss;
    }

    /**
     * 设置 the template css.
     *
     * @param templateCss
     *            the templateCss to set
     */
    public void setTemplateCss(String templateCss){
        this.templateCss = templateCss;
    }

    /**
     * 获得 the template js.
     *
     * @return the templateJs
     */
    public String getTemplateJs(){
        return templateJs;
    }

    /**
     * 设置 the template js.
     *
     * @param templateJs
     *            the templateJs to set
     */
    public void setTemplateJs(String templateJs){
        this.templateJs = templateJs;
    }

    /**
     * 获得 是否支持 HTTP_CONCAT (全局参数).
     *
     * @return the httpConcatSupport
     */
    public Boolean getHttpConcatSupport(){
        return httpConcatSupport;
    }

    /**
     * 设置 是否支持 HTTP_CONCAT (全局参数).
     *
     * @param httpConcatSupport
     *            the httpConcatSupport to set
     */
    public void setHttpConcatSupport(Boolean httpConcatSupport){
        this.httpConcatSupport = httpConcatSupport;
    }

    /**
     * 获得 default cache enable.
     *
     * @return the defaultCacheEnable
     */
    public Boolean getDefaultCacheEnable(){
        return defaultCacheEnable;
    }

    /**
     * 设置 default cache enable.
     *
     * @param defaultCacheEnable
     *            the defaultCacheEnable to set
     */
    public void setDefaultCacheEnable(Boolean defaultCacheEnable){
        this.defaultCacheEnable = defaultCacheEnable;
    }

    /**
     * cache size 限制,仅当 {@link #defaultCacheEnable}开启生效, 当cache数达到 {@link #defaultCacheEnable},将不会再缓存结果
     * 经过测试
     * <ul>
     * <li>300000 size cache占用 内存 :87.43KB(非精准)</li>
     * <li>304850 size cache占用 内存 :87.43KB(非精准)</li>
     * <li>400000 size cache占用 内存 :8.36MB(非精准)</li>
     * </ul>
     * 
     * 对于一个正式项目而言,http concat的cache, size极限大小会是 <blockquote><i>页面总数(P)*页面concat标签数(C)*i18N数(I)*版本号(V)</i></blockquote><br>
     * 如果一个项目 页面有1000个,每个页面有5个concat块,一共有5种国际化语言,如果应用重启前支持5次版本更新,那么计算公式会是 <blockquote><i>1000*5*5*5=50000</i></blockquote>
     * <b>注意:此公式中的页面总数是指,VM/JSP的数量,除非参数不同导致VM/JSP渲染的JS也不同,另当别论</b>.
     *
     * @return the defaultCacheSizeLimit
     */
    public int getDefaultCacheSizeLimit(){
        return defaultCacheSizeLimit;
    }

    /**
     * cache size 限制,仅当 {@link #defaultCacheEnable}开启生效, 当cache数达到 {@link #defaultCacheEnable},将不会再缓存结果
     * 经过测试
     * <ul>
     * <li>300000 size cache占用 内存 :87.43KB(非精准)</li>
     * <li>304850 size cache占用 内存 :87.43KB(非精准)</li>
     * <li>400000 size cache占用 内存 :8.36MB(非精准)</li>
     * </ul>
     * 
     * 对于一个正式项目而言,http concat的cache, size极限大小会是 <blockquote><i>页面总数(P)*页面concat标签数(C)*i18N数(I)*版本号(V)</i></blockquote><br>
     * 如果一个项目 页面有1000个,每个页面有5个concat块,一共有5种国际化语言,如果应用重启前支持5次版本更新,那么计算公式会是 <blockquote><i>1000*5*5*5=50000</i></blockquote>
     * <b>注意:此公式中的页面总数是指,VM/JSP的数量,除非参数不同导致VM/JSP渲染的JS也不同,另当别论</b>.
     *
     * @param defaultCacheSizeLimit
     *            the defaultCacheSizeLimit to set
     */
    public void setDefaultCacheSizeLimit(int defaultCacheSizeLimit){
        this.defaultCacheSizeLimit = defaultCacheSizeLimit;
    }
}
