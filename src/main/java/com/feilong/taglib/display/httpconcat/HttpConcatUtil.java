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

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.MapUtil.newHashMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.util.ResourceBundleUtil;
import com.feilong.taglib.display.httpconcat.builder.ContentBuilder;
import com.feilong.taglib.display.httpconcat.builder.HttpConcatGlobalConfigBuilder;
import com.feilong.taglib.display.httpconcat.command.HttpConcatGlobalConfig;
import com.feilong.taglib.display.httpconcat.command.HttpConcatParam;
import com.feilong.taglib.display.httpconcat.resolver.ItemSrcListResolver;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * http concat的核心工具类.
 * 
 * <h3>说明:</h3>
 * <blockquote>
 * <ol>
 * <li>你可以访问 wiki 查看更多 <a href="https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-concat">feilongDisplay-concat</a></li>
 * <li>类加载的时候,会使用 {@link ResourceBundleUtil} 来读取<code> config/httpconcat </code> 配置文件中的
 * {@link HttpConcatGlobalConfigBuilder#KEY_TEMPLATE_CSS} css模板 以及 {@link HttpConcatGlobalConfigBuilder#KEY_TEMPLATE_JS} JS模板<br>
 * </li>
 * <li>请确保文件路径中有配置文件,以及正确的key,如果获取不到,会 throw {@link IllegalArgumentException}</li>
 * </ol>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see HttpConcatTag
 * @see HttpConcatParam
 * @see org.apache.commons.collections4.map.LRUMap
 * @see <a href="https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-concat">feilongDisplay-concat</a>
 * @since 1.0.7
 */
//XXX 丰富 JavaDOC
public final class HttpConcatUtil{

    /** The Constant LOGGER. */
    private static final Logger                       LOGGER = LoggerFactory.getLogger(HttpConcatUtil.class);

    /** http concat 全局配置. */
    private static final HttpConcatGlobalConfig       HTTP_CONCAT_GLOBAL_CONFIG;

    //---------------------------------------------------------------

    /**
     * 将结果缓存到map.
     * 
     * <p>
     * key是入参 {@link HttpConcatParam}对象,value是解析完的字符串<br>
     * 该cache里面value不会存放null/empty
     * </p>
     * 
     * @since 1.0.7
     */
    //TODO change to ConcurrentHashMap
    //这里对线程安全的要求不高,仅仅是插入和读取的操作,即使出了线程安全问题,重新解析js/css标签代码并加载即可
    private static final Map<HttpConcatParam, String> CACHE  = newHashMap(500);

    //---------------------------------------------------------------

    static{
        HTTP_CONCAT_GLOBAL_CONFIG = HttpConcatGlobalConfigBuilder.buildHttpConcatGlobalConfig();

        if (LOGGER.isInfoEnabled()){
            LOGGER.info("init httpConfig:[{}]", HttpConcatUtil.class.getSimpleName(), JsonUtil.format(HTTP_CONCAT_GLOBAL_CONFIG));
        }
    }

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private HttpConcatUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    // *****************************************************************************
    /**
     * 获得解析的内容.
     *
     * @param httpConcatParam
     *            the http concat param
     * @return
     *         <ul>
     *         <li>如果 isNullOrEmpty httpConcatParam.getItemSrcList() ,返回 {@link StringUtils#EMPTY}</li>
     *         <li>如果支持 concat,那么生成concat字符串</li>
     *         <li>如果不支持 concat,那么生成多行js/css 原生的字符串</li>
     *         </ul>
     */
    public static String getWriteContent(HttpConcatParam httpConcatParam){
        Validate.notNull(httpConcatParam, "httpConcatParam can't be null!");

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("httpConcatParam info:[{}]", JsonUtil.format(httpConcatParam));
        }

        //---------------------------------------------------------------

        //是否使用cache
        boolean isWriteCache = HTTP_CONCAT_GLOBAL_CONFIG.getDefaultCacheEnable();

        int cacheKeyHashCode = httpConcatParam.hashCode();
        //*************************************************************************************
        //缓存
        if (HTTP_CONCAT_GLOBAL_CONFIG.getDefaultCacheEnable()){
            //返回此映射中的键-值映射关系数.如果该映射包含的元素大于 Integer.MAX_VALUE,则返回 Integer.MAX_VALUE. 
            int cacheSize = CACHE.size();

            String content = CACHE.get(httpConcatParam);
            //包含
            if (null != content){
                LOGGER.debug("hashcode:[{}],get httpConcat info from httpConcatCache,cache.size:[{}]", cacheKeyHashCode, cacheSize);
                return content;
            }

            //超出cache 数量
            boolean outOfCacheItemSizeLimit = cacheSize >= HTTP_CONCAT_GLOBAL_CONFIG.getDefaultCacheSizeLimit();
            if (outOfCacheItemSizeLimit){
                String pattern = "hashcode:[{}],cache.size:[{}] >= DEFAULT_CACHESIZELIMIT:[{}],this time will not put result to cache";
                LOGGER.warn(pattern, cacheKeyHashCode, cacheSize, HTTP_CONCAT_GLOBAL_CONFIG.getDefaultCacheSizeLimit());

                //超过,那么就不记录cache
                isWriteCache = false;
            }else{
                String pattern = "hashcode:[{}],httpConcatCache.size:[{}] not contains current httpConcatParam,will do parse";
                LOGGER.debug(pattern, cacheKeyHashCode, cacheSize);
            }
        }

        //---------------------------------------------------------------

        List<String> itemSrcList = ItemSrcListResolver
                        .resolve(httpConcatParam.getContent(), httpConcatParam.getType(), httpConcatParam.getDomain());
        // 判断item list
        if (isNullOrEmpty(itemSrcList)){
            LOGGER.warn("itemSrcList isNullOrEmpty,need itemSrcList to create links,return [empty]");
            return EMPTY;
        }

        String content = ContentBuilder.buildContent(httpConcatParam, itemSrcList, HTTP_CONCAT_GLOBAL_CONFIG);
        // **************************log***************************************************

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("returnValue:[{}],length:[{}]", content, content.length());
        }

        //********************设置cache***********************************************
        if (isWriteCache){
            CACHE.put(httpConcatParam, content);
            LOGGER.debug("hashcode:[{}] put to cache,cache size:[{}]", httpConcatParam.hashCode(), CACHE.size());
        }else{
            if (HTTP_CONCAT_GLOBAL_CONFIG.getDefaultCacheEnable()){
                String pattern = "hashcode:[{}],DEFAULT_CACHEENABLE:[{}],but isWriteCache:[{}],so http concat result not put to cache";
                LOGGER.warn(pattern, cacheKeyHashCode, HTTP_CONCAT_GLOBAL_CONFIG.getDefaultCacheEnable(), isWriteCache);
            }
        }

        //---------------------------------------------------------------
        return content;
    }

}