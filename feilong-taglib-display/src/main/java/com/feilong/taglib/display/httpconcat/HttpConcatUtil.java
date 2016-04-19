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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.Validator;
import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.bean.ToStringConfig;
import com.feilong.core.lang.StringUtil;
import com.feilong.core.text.MessageFormatUtil;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.core.util.ResourceBundleUtil;
import com.feilong.taglib.display.httpconcat.command.HttpConcatGlobalConfig;
import com.feilong.taglib.display.httpconcat.command.HttpConcatParam;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * http concat的核心工具类.
 * 
 * <p>
 * 类加载的时候,会使用 {@link ResourceBundleUtil} 来读取{@link HttpConcatGlobalConfigBuilder#CONFIG_FILE} 配置文件中的
 * {@link HttpConcatGlobalConfigBuilder#KEY_TEMPLATE_CSS} css模板 以及 {@link HttpConcatGlobalConfigBuilder#KEY_TEMPLATE_JS} JS模板<br>
 * </p>
 * 
 * <p>
 * 注意: 请确保文件路径中有配置文件,以及正确的key ,如果获取不到,会 throw {@link IllegalArgumentException}
 * </p>
 * 
 * @author feilong
 * @version 1.0.7 2014年5月19日 下午2:50:43
 * @see HttpConcatTag
 * @see HttpConcatConstants
 * @see HttpConcatParam
 * @since 1.0.7
 */
//XXX 丰富 JavaDOC
public final class HttpConcatUtil{

    /** The Constant LOGGER. */
    private static final Logger                       LOGGER = LoggerFactory.getLogger(HttpConcatUtil.class);

    /** http concat 全局配置. */
    private static final HttpConcatGlobalConfig       httpConcatGlobalConfig;

    /**
     * 将结果缓存到map.<br>
     * key是入参 {@link HttpConcatParam}对象,value是解析完的字符串<br>
     * 该cache里面value不会存放null/empty
     * 
     * @since 1.0.7
     */
    //TODO change to ConcurrentHashMap
    //这里对线程安全的要求不高,仅仅是插入和读取的操作,即使出了线程安全问题,重新解析js/css标签代码并加载即可
    private static final Map<HttpConcatParam, String> CACHE  = new HashMap<HttpConcatParam, String>();

    static{
        LOGGER.info("begin init [{}]", HttpConcatUtil.class.getSimpleName());

        httpConcatGlobalConfig = HttpConcatGlobalConfigBuilder.buildHttpConcatGlobalConfig();

        LOGGER.info("end init [{}],httpConfig:[{}]", HttpConcatUtil.class.getSimpleName(), JsonUtil.format(httpConcatGlobalConfig));
    }

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
     *         <li>如果 isNullOrEmpty httpConcatParam.getItemSrcList() ,return {@link StringUtils#EMPTY}</li>
     *         <li>如果支持 concat,那么生成concat字符串</li>
     *         <li>如果不支持 concat,那么生成多行js/css 原生的字符串</li>
     *         </ul>
     */
    public static String getWriteContent(HttpConcatParam httpConcatParam){
        Validate.notNull(httpConcatParam, "httpConcatParam can't be null!");

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(JsonUtil.format(httpConcatParam));
        }

        // 判断item list
        List<String> itemSrcList = httpConcatParam.getItemSrcList();
        if (Validator.isNullOrEmpty(itemSrcList)){
            LOGGER.warn("the param itemSrcList isNullOrEmpty,need itemSrcList to create links,return [empty]");
            return StringUtils.EMPTY;
        }

        //是否使用cache
        boolean isWriteCache = httpConcatGlobalConfig.getDefaultCacheEnable();

        int cacheKeyHashCode = httpConcatParam.hashCode();
        //*************************************************************************************
        //缓存
        if (httpConcatGlobalConfig.getDefaultCacheEnable()){
            //返回此映射中的键-值映射关系数.如果该映射包含的元素大于 Integer.MAX_VALUE,则返回 Integer.MAX_VALUE. 
            int cacheSize = CACHE.size();

            String content = CACHE.get(httpConcatParam);
            //包含
            if (null != content){
                LOGGER.info("hashcode:[{}],get httpConcat info from httpConcatCache,cache.size:[{}]", cacheKeyHashCode, cacheSize);
                return content;
            }

            //超出cache 数量
            boolean outOfCacheItemSizeLimit = cacheSize >= httpConcatGlobalConfig.getDefaultCacheSizeLimit();
            if (outOfCacheItemSizeLimit){
                LOGGER.warn(
                                "hashcode:[{}],cache.size:[{}] >= DEFAULT_CACHESIZELIMIT:[{}],this time will not put result to cache",
                                cacheKeyHashCode,
                                cacheSize,
                                httpConcatGlobalConfig.getDefaultCacheSizeLimit());

                //超过,那么就不记录cache
                isWriteCache = false;
            }else{
                LOGGER.info(
                                "hashcode:[{}],httpConcatCache.size:[{}] not contains httpConcatParam,will do parse",
                                cacheKeyHashCode,
                                cacheSize);
            }
        }

        String content = buildContent(httpConcatParam);
        // **************************log***************************************************
        LOGGER.debug("returnValue:[{}],length:[{}]", content, content.length());

        //********************设置cache***********************************************
        if (isWriteCache){
            CACHE.put(httpConcatParam, content);
            LOGGER.info("key's hashcode:[{}] put to cache,cache size:[{}]", httpConcatParam.hashCode(), CACHE.size());
        }else{
            if (httpConcatGlobalConfig.getDefaultCacheEnable()){
                LOGGER.warn(
                                "hashcode:[{}],DEFAULT_CACHEENABLE:[{}],but isWriteCache:[{}],so http concat result not put to cache",
                                cacheKeyHashCode,
                                httpConcatGlobalConfig.getDefaultCacheEnable(),
                                isWriteCache);
            }
        }
        return content;
    }

    /**
     * 构造content.
     *
     * @param httpConcatParam
     *            the http concat param
     * @return the string
     * @since 1.4.1
     */
    private static String buildContent(HttpConcatParam httpConcatParam){
        // **********是否开启了连接********************************************************
        Boolean httpConcatSupport = httpConcatParam.getHttpConcatSupport();
        //如果没有设置就使用默认的全局设置
        if (null == httpConcatSupport){
            httpConcatSupport = (null == httpConcatGlobalConfig.getHttpConcatSupport()) ? false
                            : httpConcatGlobalConfig.getHttpConcatSupport();
        }

        // *******************************************************************
        // 标准化 httpConcatParam,比如list去重,标准化domain等等
        // 下面的解析均基于standardHttpConcatParam来操作
        // httpConcatParam只做入参判断,数据转换,以及cache存取
        HttpConcatParam standardHttpConcatParam = standardHttpConcatParam(httpConcatParam);

        // *********************************************************************************
        String type = standardHttpConcatParam.getType();
        String template = getTemplate(type);

        if (httpConcatSupport){ // concat
            String concatLink = getConcatLink(standardHttpConcatParam);
            return MessageFormatUtil.format(template, concatLink);
        }

        // 本地开发环境支持的.
        List<String> itemSrcList = standardHttpConcatParam.getItemSrcList();
        StringBuilder sb = new StringBuilder();
        for (String itemSrc : itemSrcList){
            String noConcatLink = getNoConcatLink(itemSrc, standardHttpConcatParam);
            sb.append(MessageFormatUtil.format(template, noConcatLink));
        }
        return sb.toString();
    }

    /**
     * 标准化 httpConcatParam,比如list去重,标准化domain等等.
     * 
     * @param httpConcatParam
     *            the http concat param
     * @return the http concat param
     */
    private static HttpConcatParam standardHttpConcatParam(HttpConcatParam httpConcatParam){
        //******************domain*******************************************
        String domain = httpConcatParam.getDomain();
        // 格式化 domain 成 http://www.feilong.com/ 形式
        if (Validator.isNotNullOrEmpty(domain)){
            if (!domain.endsWith("/")){
                domain = domain + "/";
            }
        }else{
            domain = "";
        }

        //********************root*****************************************
        String root = httpConcatParam.getRoot();
        // 格式化 root 成 xxxx/xxx/ 形式,
        if (Validator.isNotNullOrEmpty(root)){
            if (!root.endsWith("/")){
                root = root + "/";
            }
            if (root.startsWith("/")){
                root = StringUtil.substring(root, 1);
            }
        }else{
            root = "";
        }

        // ********************itemSrcList*******************************************************
        // 判断item list
        List<String> itemSrcList = httpConcatParam.getItemSrcList();

        // 去重,元素不重复
        List<String> noRepeatitemList = CollectionsUtil.removeDuplicate(itemSrcList);

        //**************************************************************
        if (Validator.isNullOrEmpty(noRepeatitemList)){
            LOGGER.warn("the param noRepeatitemList isNullOrEmpty,need noRepeatitemList to create links");
            return null;
        }
        int noRepeatitemListSize = noRepeatitemList.size();
        int itemSrcListSize = itemSrcList.size();

        if (noRepeatitemListSize != itemSrcListSize){
            LOGGER.warn(
                            "noRepeatitemList.size():[{}] != itemSrcList.size():[{}],httpConcatParam:{}",
                            noRepeatitemListSize,
                            itemSrcListSize,
                            JsonUtil.format(httpConcatParam));
        }
        // *******************************************************************
        HttpConcatParam standardHttpConcatParam = new HttpConcatParam();
        standardHttpConcatParam.setItemSrcList(noRepeatitemList);
        standardHttpConcatParam.setDomain(domain);
        standardHttpConcatParam.setRoot(root);
        standardHttpConcatParam.setHttpConcatSupport(httpConcatParam.getHttpConcatSupport());
        standardHttpConcatParam.setType(httpConcatParam.getType());
        standardHttpConcatParam.setVersion(httpConcatParam.getVersion());

        // *******************************************************************
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("standardHttpConcatParam:{}", JsonUtil.format(standardHttpConcatParam));
        }
        return standardHttpConcatParam;

    }

    // *****************************************************************************
    /**
     * 获得合并的链接.
     * 
     * @param httpConcatParam
     *            the http concat param
     * @return the link
     */
    private static String getConcatLink(HttpConcatParam httpConcatParam){
        List<String> itemSrcList = httpConcatParam.getItemSrcList();
        String domain = httpConcatParam.getDomain();
        String root = httpConcatParam.getRoot();
        String version = httpConcatParam.getVersion();

        // **********************************************************************************

        StringBuilder sb = new StringBuilder();
        sb.append(domain);
        sb.append(root);

        int size = itemSrcList.size();
        // 只有一条 输出原生字符串
        if (size == 1){
            sb.append(itemSrcList.get(0));
            LOGGER.debug("itemSrcList size==1,will generate primary {}.", httpConcatParam.getType());
        }else{
            sb.append("??");

            ToStringConfig toStringConfig = new ToStringConfig(ToStringConfig.DEFAULT_CONNECTOR);
            sb.append(ConvertUtil.toString(toStringConfig, itemSrcList));
        }
        appendVersion(version, sb);

        return sb.toString();
    }

    /**
     * 获得不需要 Concat 的连接.
     * 
     * @param itemSrc
     *            the src
     * @param httpConcatParam
     *            the http concat param
     * @return the string
     */
    private static String getNoConcatLink(String itemSrc,HttpConcatParam httpConcatParam){
        String domain = httpConcatParam.getDomain();
        String root = httpConcatParam.getRoot();
        String version = httpConcatParam.getVersion();
        StringBuilder sb = new StringBuilder();
        sb.append(domain);
        sb.append(root);
        sb.append(itemSrc);
        appendVersion(version, sb);
        return sb.toString();
    }

    /**
     * Append version.
     * 
     * @param version
     *            the version
     * @param sb
     *            the sb
     */
    private static void appendVersion(String version,StringBuilder sb){
        if (Validator.isNotNullOrEmpty(version)){
            sb.append("?");
            sb.append(version);
        }else{
            LOGGER.debug("the param version isNullOrEmpty,we suggest you should set version value");
        }
    }

    /**
     * 获得 items array.
     * 
     * @param blockContent
     *            内容,目前 以 \n 分隔
     * @return the items array
     */
    public static List<String> toItemSrcList(String blockContent){
        String regex = "\n";
        String[] items = blockContent.trim().split(regex);
        int length = items.length;

        List<String> list = new ArrayList<String>(length);
        for (int i = 0; i < length; ++i){
            String item = items[i];
            // 忽视空行
            if (Validator.isNotNullOrEmpty(item)){
                // 去除空格
                list.add(item.trim());
            }
        }
        return list;
    }

    // *****************************************************************************

    /**
     * 不同的type不同的模板.
     *
     * @param type
     *            类型 {@link HttpConcatConstants#TYPE_CSS} 以及{@link HttpConcatConstants#TYPE_JS}
     * @return 目前仅支持 {@link HttpConcatConstants#TYPE_CSS} 以及{@link HttpConcatConstants#TYPE_JS},其余不支持,会抛出
     *         {@link UnsupportedOperationException}
     */
    private static String getTemplate(String type){
        if (HttpConcatConstants.TYPE_CSS.equalsIgnoreCase(type)){
            return httpConcatGlobalConfig.getTemplateCss();
        }else if (HttpConcatConstants.TYPE_JS.equalsIgnoreCase(type)){
            return httpConcatGlobalConfig.getTemplateJs();
        }
        throw new UnsupportedOperationException("type:[" + type + "] not support!,current time,only support js or css");
    }
}