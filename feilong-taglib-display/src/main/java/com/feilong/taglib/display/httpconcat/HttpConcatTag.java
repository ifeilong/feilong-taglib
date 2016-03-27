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
package com.feilong.taglib.display.httpconcat;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.feilong.core.Validator;
import com.feilong.taglib.base.AbstractEndWriteContentTag;
import com.feilong.taglib.display.httpconcat.command.HttpConcatParam;

/**
 * 根据 TENGINE_SUPPORT判断 将参数动态生成tengine插件的形式或者普通js/css的形式.<br>
 * <br>
 * <p>
 * 作用:遵循Yahoo!前端优化准则第一条：减少HTTP请求发送次数<br>
 * 这一功能可以组合Javascript 以及 Css文件<br>
 * </p>
 * 
 * <h3>使用方法:</h3>
 * 
 * <blockquote>
 * <ul>
 * <li>以两个问号(??)激活combo</li>
 * <li>多文件之间用半角逗号(,)分开</li>
 * <li>用一个?来便是时间戳</li>
 * </ul>
 * </blockquote>
 * 
 * @author feilong
 * @version 1.0.2 2014年5月4日 下午11:45:20
 * @version 1.2.2 2015年7月23日 下午8:50:08
 * @since 1.0.2
 */
public class HttpConcatTag extends AbstractEndWriteContentTag{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID  = -3447592871482978718L;

    /** 类型,是 css 还是 js. */
    private String            type;

    /** 版本号. */
    private String            version;

    /**
     * 根目录.
     * <p>
     * 如果设置root为'/script' 会拼成http://staging.nikestore.com.cn/script/??jquery/jquery-1.4.2.min.js?2013022801
     * </p>
     */
    private String            root;

    /** 域名,如果没有设置，将自动使用 {@link HttpServletRequest#getContextPath()}. */
    private String            domain;

    /** 是否支持 http concat(如果设置这个参数,本次渲染,将会覆盖全局变量). */
    private Boolean           httpConcatSupport = null;

    // ***********************************************************************************
    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.taglib.base.AbstractCommonTag#writeContent()
     */
    @Override
    protected Object writeContent(){
        String bodyContentSrc = bodyContent.getString();
        if (Validator.isNullOrEmpty(bodyContentSrc)){
            return StringUtils.EMPTY;
        }

        if (Validator.isNullOrEmpty(type)){
            return StringUtils.EMPTY;
        }

        List<String> itemSrcList = HttpConcatUtil.toItemSrcList(bodyContentSrc);
        if (Validator.isNullOrEmpty(itemSrcList)){
            return StringUtils.EMPTY;
        }
        if (Validator.isNullOrEmpty(domain)){
            domain = getHttpServletRequest().getContextPath();
        }

        HttpConcatParam httpConcatParam = new HttpConcatParam();
        httpConcatParam.setDomain(domain);
        httpConcatParam.setRoot(root);
        httpConcatParam.setType(type);
        httpConcatParam.setVersion(version);
        httpConcatParam.setItemSrcList(itemSrcList);
        httpConcatParam.setHttpConcatSupport(httpConcatSupport);

        return HttpConcatUtil.getWriteContent(httpConcatParam);
    }

    // **************************************************************************

    /**
     * Sets the 类型css/js.
     * 
     * @param type
     *            the new 类型css/js
     */
    public void setType(String type){
        this.type = type;
    }

    /**
     * Sets the 版本号.
     * 
     * @param version
     *            the new 版本号
     */
    public void setVersion(String version){
        this.version = version;
    }

    /**
     * Sets the 根目录<br>
     * 如果设置root为'/script' 会拼成http://staging.
     * 
     * @param root
     *            the new 根目录<br>
     *            如果设置root为'/script' 会拼成http://staging
     */
    public void setRoot(String root){
        this.root = root;
    }

    /**
     * 域名,如果没有设置，将自动使用 {@link HttpServletRequest#getContextPath()}.
     *
     * @param domain
     *            the domain to set
     */
    public void setDomain(String domain){
        this.domain = domain;
    }

    /**
     * 设置 是否支持 http concat(如果设置这个参数,本次渲染,将会覆盖全局变量).
     *
     * @param httpConcatSupport
     *            the httpConcatSupport to set
     */
    public void setHttpConcatSupport(Boolean httpConcatSupport){
        this.httpConcatSupport = httpConcatSupport;
    }
}