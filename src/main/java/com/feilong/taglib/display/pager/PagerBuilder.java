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

import static com.feilong.taglib.display.pager.command.PagerConstants.DEFAULT_TEMPLATE_PAGE_NO;
import static com.feilong.taglib.display.pager.command.PagerConstants.I18N_FEILONG_PAGER;
import static com.feilong.taglib.display.pager.command.PagerType.NO_REDIRECT;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.net.ParamUtil;
import com.feilong.taglib.display.TagCacheManager;
import com.feilong.taglib.display.pager.command.Pager;
import com.feilong.taglib.display.pager.command.PagerAndContent;
import com.feilong.taglib.display.pager.command.PagerConstants;
import com.feilong.taglib.display.pager.command.PagerParams;
import com.feilong.taglib.display.pager.command.PagerType;
import com.feilong.taglib.display.pager.command.PagerUrlTemplate;
import com.feilong.taglib.display.pager.command.PagerVMParam;
import com.feilong.tools.jsonlib.JsonUtil;
import com.feilong.tools.velocity.VelocityUtil;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static com.feilong.core.util.ResourceBundleUtil.getResourceBundle;
import static com.feilong.core.util.ResourceBundleUtil.toMap;

/**
 * 分页构造器.
 * 
 * <p>
 * 该类主要是将url相关数据转成vm需要的数据,并解析成字符串返回.
 * </p>
 * 
 * <h3>日志:</h3>
 * 
 * <blockquote>
 * <p>
 * 内部会分别对入参 {@link PagerParams} 和构造vm参数,记录 <b>debug</b> 级别的log,<br>
 * 如果不需要care这部分log,可以在日志配置文件中配置,将log输出的级别调高
 * </p>
 * 
 * <h4>log4j.xml</h4>
 * 
 * <pre class="code">
 * {@code
 *  <category name="com.feilong.taglib.display.pager.PagerBuilder">
 *      <priority value="info" />
 *  </category>
 * }
 * </pre>
 * 
 * <h4>logback.xml</h4>
 * 
 * <pre class="code">
 * {@code
 *      <logger name="com.feilong.taglib.display.pager.PagerBuilder" level="info" />
 * }
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>VM中支持国际化:</h3>
 * 
 * <blockquote>
 * <p>
 * VM中支持国际化,您可以见国际化需要的参数 设置到 {@link PagerConstants#I18N_FEILONG_PAGER} 配置文件中, <br>
 * 程序会解析该文件所有的key/values到 {@link PagerConstants#VM_KEY_I18NMAP} 变量,您可以在VM中直接使用
 * </p>
 * </blockquote>
 * 
 * <h3>缓存:</h3>
 * 
 * <blockquote>
 * <p>
 * 作为vm解析,如果是官方商城常用页面渲染,在大流量的场景下,其实开销也是不小的,<br>
 * 基于如果传入的参数 {@link PagerParams}是一样的 {@link PagerParams#hashCode()} &&{@link PagerParams#equals(Object)},那么分页结果也应该是相同的<br>
 * 因此,如果对性能有很高的要求的话,可以使用cache
 * </p>
 * </blockquote>
 * 
 * <h3>缓存清理:</h3>
 * 
 * <blockquote>
 * <p>
 * 当vm模板内容更改,需要清理缓存,由于pagerCache 是基于JVM内存级的,因此重启应用即会生效
 * </p>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see PagerConstants
 * @see PagerParams
 * @see PagerUrlTemplate
 * @see PagerVMParam
 * @since 1.0.0
 */
public final class PagerBuilder{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PagerBuilder.class);

    /** Don't let anyone instantiate this class. */
    private PagerBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    // *****************************************************************************************

    /**
     * 通常用于ajax分页.
     *
     * @param <T>
     *            the generic type
     * @param pagerParams
     *            the pager params
     * @param itemList
     *            the item list
     * @return the pager and content
     * @see com.feilong.taglib.display.pager.command.PagerType#NO_REDIRECT
     * @since 1.4.0
     */
    public static <T> PagerAndContent<T> buildPagerAndContent(PagerParams pagerParams,List<T> itemList){
        Pager<T> pager = buildPager(pagerParams);
        pager.setItemList(itemList);

        return new PagerAndContent<T>(pager, buildContent(pagerParams));
    }

    /**
     * 解析VM模板,生成分页HTML代码.
     * 
     * <h3>maybe you want to return {@link PagerAndContent}</h3>
     * 
     * <blockquote>
     * <p>
     * 你可以拿到结果,再次封装成 {@link PagerAndContent}
     * </p>
     * </blockquote>
     * 
     * @param pagerParams
     *            构造分页需要的请求参数
     * @return 如果 {@link PagerParams#getTotalCount()}{@code <=0} 返回 {@link StringUtils#EMPTY} <br>
     *         否则 生成分页html代码
     */
    public static String buildContent(PagerParams pagerParams){
        Validate.notNull(pagerParams, "pagerParams can't be null!");

        if (pagerParams.getTotalCount() <= 0){
            LOGGER.debug("totalCount value is [{}] not > 0,will return empty", pagerParams.getTotalCount());
            return EMPTY;// 如果总数不>0 则直接返回 empty,页面分页地方显示空白
        }

        //**********************************************************************************************
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("input [pagerParams] info:{}", JsonUtil.format(pagerParams));
        }

        String content = TagCacheManager.getContentFromCache(pagerParams);
        if (isNotNullOrEmpty(content)){
            return content;
        }

        content = buildContentMain(pagerParams);
        TagCacheManager.put(pagerParams, content);
        return content;

    }

    // *****************************private************************************************************

    /**
     * Builds the pager content.
     *
     * @param pagerParams
     *            the pager params
     * @return the string
     * @since 1.4.0
     */
    private static String buildContentMain(PagerParams pagerParams){
        if (pagerParams.getDebugIsNotParseVM()){
            LOGGER.debug("param [debugIsNotParseVM] is [true],use return empty~");
            return EMPTY;
        }
        // ****************设置变量参数************************************************************
        Map<String, Object> vmParamMap = new HashMap<>();
        vmParamMap.put(PagerConstants.VM_KEY_PAGERVMPARAM, buildPagerVMParam(pagerParams));
        vmParamMap.put(PagerConstants.VM_KEY_I18NMAP, toMap(getResourceBundle(I18N_FEILONG_PAGER, pagerParams.getLocale())));

        String content = new VelocityUtil().parseTemplateWithClasspathResourceLoader(pagerParams.getVmPath(), vmParamMap);

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("parse:[{}],use vmParamMap:{},content result:{}", pagerParams.getVmPath(), JsonUtil.format(vmParamMap), content);
        }
        return content;
    }

    /**
     * Builds the pager vm param.
     *
     * @param <T>
     *            the generic type
     * @param pagerParams
     *            the pager params
     * @return the pager vm param
     * @since 1.0.5
     */
    private static <T> PagerVMParam buildPagerVMParam(PagerParams pagerParams){
        Pager<T> pager = buildPager(pagerParams);

        int allPageNo = pager.getAllPageNo();
        int currentPageNo = pager.getCurrentPageNo();
        // 最多显示多少个导航页码
        Integer maxIndexPages = buildMaxIndexPages(allPageNo, pagerParams.getMaxIndexPages());

        // ***********************************************************************
        Pair<Integer, Integer> startAndEndIndexPair = buildStartAndEndIndexPair(allPageNo, currentPageNo, maxIndexPages); //获得开始和结束的索引

        // ****************************************************************************************
        // 获得所有页码的连接.
        Map<Integer, String> allUseIndexAndHrefMap = buildAllUseIndexAndHrefMap(pagerParams, pager, startAndEndIndexPair);

        // ****************************************************************************************
        int prePageNo = pager.getPrePageNo();
        int nextPageNo = pager.getNextPageNo();

        // ********************************************************************************************
        PagerVMParam pagerVMParam = new PagerVMParam();
        pagerVMParam.setSkin(pagerParams.getSkin());// 皮肤
        pagerVMParam.setPagerType(pagerParams.getPagerType());//分页类型

        // ****************************************************************************************
        pagerVMParam.setTotalCount(pagerParams.getTotalCount());// 总行数,总结果数

        pagerVMParam.setCurrentPageNo(currentPageNo);// 当前页
        pagerVMParam.setAllPageNo(allPageNo);// 总页数
        pagerVMParam.setPrePageNo(prePageNo);
        pagerVMParam.setNextPageNo(nextPageNo);

        pagerVMParam.setStartIteratorIndex(startAndEndIndexPair.getLeft());// 导航开始页码
        pagerVMParam.setEndIteratorIndex(startAndEndIndexPair.getRight());// 导航结束页码

        // ****************************************************************************************
        pagerVMParam.setPreUrl(allUseIndexAndHrefMap.get(prePageNo)); // 上一页链接
        pagerVMParam.setNextUrl(allUseIndexAndHrefMap.get(nextPageNo));// 下一页链接
        pagerVMParam.setFirstUrl(allUseIndexAndHrefMap.get(1));// firstPageNo 第一页的链接
        pagerVMParam.setLastUrl(allUseIndexAndHrefMap.get(pager.getAllPageNo()));//lastPageNo 最后一页的链接
        // ****************************************************************************************

        pagerVMParam.setPagerUrlTemplate(buildPagerUrlTemplate(allUseIndexAndHrefMap));
        pagerVMParam.setPageParamName(pagerParams.getPageParamName());
        pagerVMParam.setIteratorIndexMap(
                        getIteratorIndexAndHrefMap(allUseIndexAndHrefMap, startAndEndIndexPair.getLeft(), startAndEndIndexPair.getRight()));
        return pagerVMParam;
    }

    /**
     * Builds the pager.
     *
     * @param <T>
     *            the generic type
     * @param pagerParams
     *            the pager params
     * @return the pager
     * @since 1.4.0
     */
    private static <T> Pager<T> buildPager(PagerParams pagerParams){
        int totalCount = pagerParams.getTotalCount();
        int currentPageNo = detectCurrentPageNo(pagerParams);
        int pageSize = pagerParams.getPageSize();

        Pager<T> pager = new Pager<T>(currentPageNo, pageSize, totalCount);
        pager.setMaxShowPageNo(pagerParams.getMaxShowPageNo());
        return pager;
    }

    /**
     * Builds the pager url template.
     *
     * @param indexAndHrefMap
     *            the index and href map
     * @return the pager url template
     * @since 1.4.0
     */
    private static PagerUrlTemplate buildPagerUrlTemplate(Map<Integer, String> indexAndHrefMap){
        Integer defaultTemplatePageNo = PagerConstants.DEFAULT_TEMPLATE_PAGE_NO;

        PagerUrlTemplate pagerUrlTemplate = new PagerUrlTemplate();
        pagerUrlTemplate.setTemplateValue(defaultTemplatePageNo);
        pagerUrlTemplate.setHref(indexAndHrefMap.get(defaultTemplatePageNo));// 模板链接
        return pagerUrlTemplate;
    }

    //*************************************************************************************************

    /**
     * 要循环的 index和 end 索引 =href map.
     *
     * @param indexAndHrefMap
     *            the index and href map
     * @param startIteratorIndex
     *            开始迭代索引编号
     * @param endIteratorIndex
     *            结束迭代索引编号
     * @return the iterator index and href map
     * @since 1.8.1 change method param
     */
    private static Map<Integer, String> getIteratorIndexAndHrefMap(
                    Map<Integer, String> indexAndHrefMap,
                    int startIteratorIndex,
                    int endIteratorIndex){
        Map<Integer, String> map = newLinkedHashMap(endIteratorIndex - startIteratorIndex + 1);
        for (int i = startIteratorIndex; i <= endIteratorIndex; ++i){
            map.put(i, indexAndHrefMap.get(i));
        }
        return map;
    }

    /**
     * 获得当前的页码.
     * 
     * <p>
     * 对于 {@code <} 1的情况做 返回1特殊处理.
     * </p>
     * 
     * @param pagerParams
     *            the pager params
     * @return 如果 {@link PagerParams#getCurrentPageNo()} {@code <} 1, 返回 1 <br>
     *         否则返回 {@link PagerParams#getCurrentPageNo()}
     * @since 1.0.5
     */
    private static int detectCurrentPageNo(PagerParams pagerParams){
        Integer currentPageNo = pagerParams.getCurrentPageNo();
        if (null == currentPageNo || currentPageNo < 1){
            return 1;// 解决可能出现界面上负数的情况
        }
        return currentPageNo;
    }

    /**
     * 获得所有页码的连接.
     * 
     * <p>
     * 注:(key={@link #DEFAULT_TEMPLATE_PAGE_NO} 为模板链接,可用户前端解析 {@link PagerVMParam#getHrefUrlTemplate()}.
     * </p>
     *
     * @param <T>
     *            the generic type
     * @param pagerParams
     *            the pager params
     * @param pager
     *            the pager
     * @param startAndEndIndexPair
     *            the start and end index pair
     * @return key是分页页码,value是解析之后的链接
     * @since 1.4.0
     */
    private static <T> Map<Integer, String> buildAllUseIndexAndHrefMap(
                    PagerParams pagerParams,
                    Pager<T> pager,
                    Pair<Integer, Integer> startAndEndIndexPair){
        String pageParamName = pagerParams.getPageParamName();
        PagerType pagerType = pagerParams.getPagerType();

        //*********************这种替换的性能要高***********************************************
        CharSequence targetForReplace = pageParamName + "=" + DEFAULT_TEMPLATE_PAGE_NO;

        String templateEncodedUrl = getTemplateEncodedUrl(pagerParams, pageParamName, pagerType);
        // *************************************************************************
        Set<Integer> indexSet = buildAllUseIndexSet(pager, startAndEndIndexPair.getLeft(), startAndEndIndexPair.getRight());
        Map<Integer, String> returnMap = new HashMap<Integer, String>();
        for (Integer index : indexSet){
            String link = pagerType == NO_REDIRECT ? templateEncodedUrl
                            : templateEncodedUrl.replace(targetForReplace, pageParamName + "=" + index);
            returnMap.put(index, link);
        }
        return returnMap;
    }

    /**
     * Gets the template encoded url.
     *
     * @param pagerParams
     *            the pager params
     * @param pageParamName
     *            the page param name
     * @param pagerType
     *            the pager type
     * @return the template encoded url
     * @since 1.6.1
     */
    private static String getTemplateEncodedUrl(PagerParams pagerParams,String pageParamName,PagerType pagerType){
        boolean isNoRedirect = NO_REDIRECT == pagerType;
        if (isNoRedirect){
            return "javascript:void(0);";//ajaxLink
        }
        String defaultTemplatePageNo = "" + PagerConstants.DEFAULT_TEMPLATE_PAGE_NO;
        return ParamUtil.addParameter(pagerParams.getPageUrl(), pageParamName, defaultTemplatePageNo, pagerParams.getCharsetType());
    }

    /**
     * 获得所有需要使用的链接的索引号.
     * 
     * <h3>包含:</h3>
     * 
     * <blockquote>
     * <ul>
     * <li>{@link PagerConstants#DEFAULT_TEMPLATE_PAGE_NO}</li>
     * <li><code>prePageNo</code></li>
     * <li><code>nextPageNo</code></li>
     * <li><code>firstPageNo</code></li>
     * <li><code>lastPageNo</code></li>
     * <li>以及迭代的 <code>startIteratorIndexAndEndIteratorIndexs</code></li>
     * </ul>
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param pager
     *            the pager
     * @param startIteratorIndex
     *            开始迭代索引编号
     * @param endIteratorIndex
     *            结束迭代索引编号
     * @return the index set
     * @since 1.8.1 change method param
     */
    private static <T> Set<Integer> buildAllUseIndexSet(Pager<T> pager,int startIteratorIndex,int endIteratorIndex){
        Set<Integer> indexSet = new HashSet<Integer>();// 所有需要生成url 的 index值
        indexSet.add(PagerConstants.DEFAULT_TEMPLATE_PAGE_NO);// 模板链接 用于前端操作
        indexSet.add(pager.getPrePageNo());//prePageNo
        indexSet.add(pager.getNextPageNo());//nextPageNo
        indexSet.add(1);//firstPageNo
        indexSet.add(pager.getAllPageNo());//lastPageNo

        for (int i = startIteratorIndex; i <= endIteratorIndex; ++i){
            indexSet.add(i);
        }
        return indexSet;
    }

    //****************************************************************************************************

    /**
     * 获得开始和结束的索引.
     *
     * @param allPageNo
     *            总页码
     * @param currentPageNo
     *            当前页面
     * @param maxIndexPages
     *            最大显示页码数量
     * @return 获得开始和结束的索引
     */
    private static Pair<Integer, Integer> buildStartAndEndIndexPair(int allPageNo,int currentPageNo,Integer maxIndexPages){
        if (allPageNo <= maxIndexPages){
            return Pair.of(1, allPageNo);
        }

        //**********总页数大于最大导航页数******************************************************************
        // 当前页导航两边总数和
        int fenTwo = maxIndexPages - 1;
        // 当前页左侧导航数
        int leftCount = fenTwo / 2;
        // 当前页右侧导航数
        int rightCount = (fenTwo % 2 == 0) ? leftCount : (leftCount + 1);

        //**************************************************************************************
        // 当前页<=(左边页数+1)
        if (currentPageNo <= (leftCount + 1)){
            return Pair.of(1, maxIndexPages); // 此时迭代结束为maxIndexPages
        }

        // 如果当前页+右边页>=总页数
        if (currentPageNo + rightCount >= allPageNo){
            return Pair.of(allPageNo - maxIndexPages + 1, allPageNo);// 此时迭代结束为allPageNo
        }

        return Pair.of(currentPageNo - leftCount, currentPageNo + rightCount);
    }

    /**
     * 获得最大显示的分页码数量.
     * 
     * <p>
     * 如果页码大于1000的时候,如果还是10条页码的显示(如1001,1002,1003,1004,1005,1006,1007,1008,1009,1010),那么页面分页会很长 ,可能打乱页面布局<br>
     * 所以maxIndexPages是<=0或者null,那么根据allpageNo,采用自动调节长度功能
     * </p>
     * 
     * <h3>目前自动规则:</h3>
     * 
     * <blockquote>
     * <ul>
     * <li>当大于1000的页码 显示6个,即 1001,1002,1003,1004,1005,1006 类似于这样的;</li>
     * <li>当大于100的页码 显示8个,即 101,102,103,104,105,106,107,108 类似于这样的;</li>
     * <li>其余,默认显示10条</li>
     * </ul>
     * </blockquote>
     * 
     * @param allPageNo
     *            分页总总页数
     * @param maxIndexPages
     *            表示手动指定一个固定的显示码<br>
     *            如果不指定,或者<=0 那么就采用自动调节的显示码
     * @return 最大分页码数量
     * @deprecated 需要重构,将来可能会拥有更好的扩展性
     */
    @Deprecated
    private static int buildMaxIndexPages(int allPageNo,Integer maxIndexPages){
        if (isNullOrEmpty(maxIndexPages) || maxIndexPages <= 0){
            // 总页数超过1000的时候,自动调节导航数量的作用
            if (allPageNo > 1000){
                return 6;
            }
            if (allPageNo > 100){
                return 8;
            }
            return 10;// 默认为10
        }
        return maxIndexPages;// 不是<= 0 或者null,直接返回指定的
    }
}
