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
package com.feilong.taglib.display.pager;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ObjectUtils;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.lang.ObjectUtil;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.taglib.AbstractStartWriteContentTag;
import com.feilong.taglib.LocaleSupport;
import com.feilong.taglib.display.pager.command.PagerConstants;
import com.feilong.taglib.display.pager.command.PagerParams;

import static com.feilong.core.CharsetType.UTF8;

/**
 * 分页标签.
 * 
 * <h3>特点:</h3>
 * 
 * <ol>
 * <li>支持皮肤切换</li>
 * <li>支持velocity模版,支持自定义velocity模版</li>
 * <li>自动识别是否是forwoad页面分页连接</li>
 * <li>分页页码,当前页码永远居中</li>
 * <li>分页页码支持根据页码数字自动显示分页码个数,见参数说明里面的{@link #maxIndexPages}参数</li>
 * <li>经过大型项目检验,通用安全扫描</li>
 * <li>支持国际化(1.0.5 new feature)</li>
 * <li>内置文本框页码输入快速跳转(1.0.5 new feature)</li>
 * <li>支持类似于淘宝最大分页码100 这样的控制 ,见参数 {@link #maxShowPageNo} (1.0.5 new feature)</li>
 * <li>支持Ajax 分页 ,见参数 {@link PagerParams#pagerType} (1.4.0 new feature)</li>
 * </ol>
 * 
 * <h3>使用方式:</h3>
 * 
 * <pre class="code">
 * {@code
 * 
 *  步骤1.JSP引用自定义标签
 *      <%@ taglib prefix="feilongDisplay" uri="http://java.feilong.com/tags-display"%>
 * 
 *  步骤2.使用自定义标签
 *  
 *      精简写法:
 *          <feilongDisplay:pager count="1000"/>
 *          此时其余参数缺省,均使用默认值
 *      
 *      所有参数都赋值的写法:
 *          <feilongDisplay:pager count="1000" 
 *              charsetType="utf-8" 
 *              maxIndexPages="3" 
 *              pageParamName="page" 
 *              pageSize="10"
 *              locale="}${requestScope['org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE']}{@code" 
 *              vmPath="velocity/feilong-default-pager.vm" 
 *              skin="scott"
 *              pagerHtmlAttributeName="feilongPagerHtml1" />
 *              每个参数的含义,请参见下面参数表格部分
 * }
 * </pre>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.0
 */
public class PagerTag extends AbstractStartWriteContentTag implements LocaleSupport{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3523064037264688170L;

    /** 数据总数. */
    private Integer           count;

    // *******************************************************************

    /** 每页显示多少行,默认20. */
    private Integer           pageSize         = PagerConstants.DEFAULT_PAGESIZE;

    // *******************************************************************

    /** url页码参数,默认 pageNo. */
    private String            pageParamName    = PagerConstants.DEFAULT_PAGE_PARAM_NAME;

    /** The vm path. */
    private String            vmPath           = PagerConstants.DEFAULT_TEMPLATE_IN_CLASSPATH;

    /** 皮肤,默认digg. */
    private String            skin             = PagerConstants.DEFAULT_SKIN;

    // *************************************************************************************************
    /**
     * 最多显示页数,(-1或者不设置,默认显示所有页数).
     * 
     * <p>
     * 比如淘宝,不管搜索东西多少,最多显示100页
     * </p>
     * 
     * <p>
     * 这是一种折中的处理方式,<b>空间换时间</b>.<br>
     * 数据查询越往后翻,对服务器的压力越大,速度越低,而且从业务上来讲商品质量也越差,所以就没有必要给太多了.<br>
     * 新浪微博的时间轴也只给出了10页,同样的折中处理.
     * </p>
     * 
     * @since 1.0.5
     */
    private Integer           maxShowPageNo    = PagerConstants.DEFAULT_LIMITED_MAX_PAGENO;

    /**
     * 设置{@link Locale} 环境, 支持 java.util.Locale 或 String 类型的实例 .
     *
     * @see org.apache.taglibs.standard.tag.common.fmt.SetLocaleSupport#value
     * @see org.apache.taglibs.standard.tag.common.fmt.SetLocaleSupport#parseLocale(String, String)
     * @see org.apache.taglibs.standard.tag.common.fmt.ParseDateSupport#parseLocale
     * @see org.apache.taglibs.standard.tag.rt.fmt.ParseNumberTag#setParseLocale(Object)
     * @see org.apache.taglibs.standard.tag.rt.fmt.ParseDateTag#setParseLocale(Object)
     * @since 1.0.5
     * @since 1.7.2 change Object type
     */
    private Object            locale;

    /**
     * url编码.
     * 
     * @since 1.0.5
     */
    private String            charsetType      = UTF8;

    /**
     * vm被解析出来的文本,会被存在在这个变量中,作用域为pageContext,以便重复使用.
     * 
     * <p>
     * 比如某些页面,上面下面都要显示同样的分页,方便用户操作.
     * </p>
     * 
     * <p>
     * 此外,此变量名称允许变更,以便实现同一页页面不同功能的的分页.
     * </p>
     * 
     * @since 1.0.5
     */
    private String            pagerHtmlAttributeName;

    // *****************************end**************************************************
    /**
     * 最多显示多少个导航页码.
     * 
     * @deprecated 参数名字取得不好,在将来的版本会更改替换,不建议使用这个参数
     */
    @Deprecated
    private Integer           maxIndexPages;

    // *****************************end*************************************************

    /**
     * Write content.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Override
    public String buildContent(HttpServletRequest request){
        PagerParams pagerParams = buildPagerParams(request);

        String htmlContent = PagerBuilder.buildContent(pagerParams);

        afterBuildContent(htmlContent);

        return htmlContent;
    }

    /**
     * After build content.
     *
     * @param htmlContent
     *            the html
     * @since 1.7.2
     */
    private void afterBuildContent(String htmlContent){
        String name = ObjectUtil.defaultIfNullOrEmpty(pagerHtmlAttributeName, PagerConstants.DEFAULT_PAGE_ATTRIBUTE_PAGER_HTML_NAME);
        pageContext.setAttribute(name, htmlContent);// 解析之后的变量设置在 pageContext作用域中
    }

    /**
     * Builds the pager params.
     *
     * @param request
     *            the request
     * @return the pager params
     * @since 1.7.2
     */
    private PagerParams buildPagerParams(HttpServletRequest request){
        // 当前全路径
        String pageUrl = RequestUtil.getRequestFullURL(request, charsetType);

        // ****************************************************************************
        PagerParams pagerParams = new PagerParams(count, pageUrl);

        pagerParams.setCurrentPageNo(PagerHelper.getCurrentPageNo(request, pageParamName)); // 当前页码
        pagerParams.setPageSize(pageSize);
        pagerParams.setPageParamName(pageParamName);
        pagerParams.setVmPath(vmPath);
        pagerParams.setCharsetType(charsetType);
        pagerParams.setLocale(ObjectUtils.defaultIfNull(ConvertUtil.toLocale(locale), request.getLocale()));
        pagerParams.setMaxShowPageNo(maxShowPageNo);

        pagerParams.setSkin(skin);
        pagerParams.setMaxIndexPages(maxIndexPages);
        pagerParams.setDebugIsNotParseVM(getDebugIsNotParseVM(request));
        return pagerParams;
    }

    /**
     * debugNotParseVM=true参数可以来控制 是否解析vm模板,以便测试.
     *
     * @param request
     *            the request
     * @return the debug is not parse vm
     * @since 1.7.2
     */
    private static boolean getDebugIsNotParseVM(HttpServletRequest request){
        // debugNotParseVM=true参数可以来控制 是否解析vm模板,以便测试
        String parameter = request.getParameter(PagerConstants.DEFAULT_PARAM_DEBUG_NOT_PARSEVM);
        return PagerConstants.DEFAULT_PARAM_DEBUG_NOT_PARSEVM_VALUE.equals(parameter);
    }

    //***************************************************************************

    /**
     * Sets the 数据总数.
     * 
     * @param count
     *            the count to set
     */
    public void setCount(Integer count){
        this.count = count;
    }

    /**
     * Sets the 每页显示多少行,默认20.
     * 
     * @param pageSize
     *            the pageSize to set
     */
    public void setPageSize(Integer pageSize){
        this.pageSize = pageSize;
    }

    /**
     * Sets the 最多显示多少个导航页码.
     * 
     * @param maxIndexPages
     *            the maxIndexPages to set
     */
    public void setMaxIndexPages(Integer maxIndexPages){
        this.maxIndexPages = maxIndexPages;
    }

    /**
     * Sets the url页码参数,默认 pageNo.
     * 
     * @param pageParamName
     *            the pageParamName to set
     */
    public void setPageParamName(String pageParamName){
        this.pageParamName = pageParamName;
    }

    /**
     * Sets the vm path.
     * 
     * @param vmPath
     *            the vmPath to set
     */
    public void setVmPath(String vmPath){
        this.vmPath = vmPath;
    }

    /**
     * Sets the 皮肤 默认digg.
     * 
     * @param skin
     *            the skin to set
     */
    public void setSkin(String skin){
        this.skin = skin;
    }

    /**
     * 最多显示页数,(-1或者不设置,默认显示所有页数).
     * 
     * <p>
     * 比如淘宝,不管搜索东西多少,最多显示100页
     * </p>
     * 
     * <p>
     * 这是一种折中的处理方式,<b>空间换时间</b>.<br>
     * 数据查询越往后翻,对服务器的压力越大,速度越低,而且从业务上来讲商品质量也越差,所以就没有必要给太多了.<br>
     * 新浪微博的时间轴也只给出了10页,同样的折中处理.
     * </p>
     * 
     * @param maxShowPageNo
     *            the maxShowPageNo to set
     */
    public void setMaxShowPageNo(Integer maxShowPageNo){
        this.maxShowPageNo = maxShowPageNo;
    }

    /**
     * Sets the url编码.
     * 
     * @param charsetType
     *            the charsetType to set
     */
    public void setCharsetType(String charsetType){
        this.charsetType = charsetType;
    }

    /**
     * vm被解析出来的文本,会被存在在这个变量中,作用域为pageContext,以便重复使用.
     * 
     * <p>
     * 比如某些页面,上面下面都要显示同样的分页,方便用户操作.
     * </p>
     * 
     * <p>
     * 此外,此变量名称允许变更,以便实现同一页页面不同功能的的分页.
     * </p>
     * 
     * @param pagerHtmlAttributeName
     *            the pagerHtmlAttributeName to set
     */
    public void setPagerHtmlAttributeName(String pagerHtmlAttributeName){
        this.pagerHtmlAttributeName = pagerHtmlAttributeName;
    }

    /**
     * 设置{@link Locale} 环境, 支持 java.util.Locale 或 String 类型的实例 .
     *
     * @param locale
     *            the locale to set
     */
    @Override
    public void setLocale(Object locale){
        this.locale = locale;
    }
}
