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
package com.feilong.taglib.display.httpconcat.resolver;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.bean.ToStringConfig.DEFAULT_CONNECTOR;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.bean.ToStringConfig;
import com.feilong.taglib.display.httpconcat.command.HttpConcatParam;

/**
 * The Class ConcatLinkResolver.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public final class ConcatLinkResolver{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcatLinkResolver.class);

    /** Don't let anyone instantiate this class. */
    private ConcatLinkResolver(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 获得合并的链接.
     *
     * @param httpConcatParam
     *            the http concat param
     * @param itemSrcList
     *            the item src list
     * @return the link
     */
    public static String resolver(HttpConcatParam httpConcatParam,List<String> itemSrcList){
        StringBuilder sb = new StringBuilder();
        sb.append(httpConcatParam.getDomain());
        sb.append(httpConcatParam.getRoot());

        // 只有一条 输出原生字符串
        if (itemSrcList.size() == 1){
            sb.append(itemSrcList.get(0));
            LOGGER.debug("itemSrcList size==1,will generate primary {}.", httpConcatParam.getType());
        }else{
            sb.append("??");

            ToStringConfig toStringConfig = new ToStringConfig(DEFAULT_CONNECTOR);
            sb.append(ConvertUtil.toString(itemSrcList, toStringConfig));
        }
        appendVersion(httpConcatParam.getVersion(), sb);
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
    public static String getNoConcatLink(String itemSrc,HttpConcatParam httpConcatParam){
        StringBuilder sb = new StringBuilder();
        sb.append(httpConcatParam.getDomain());
        sb.append(httpConcatParam.getRoot());
        sb.append(itemSrc);
        appendVersion(httpConcatParam.getVersion(), sb);
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
        if (isNotNullOrEmpty(version)){
            sb.append("?");
            sb.append(version);
        }else{
            LOGGER.debug("the param version isNullOrEmpty,we suggest you should set version value");
        }
    }

}
