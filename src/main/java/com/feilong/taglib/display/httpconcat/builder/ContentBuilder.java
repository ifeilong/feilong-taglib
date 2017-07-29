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
package com.feilong.taglib.display.httpconcat.builder;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.text.MessageFormatUtil;
import com.feilong.taglib.display.httpconcat.command.HttpConcatGlobalConfig;
import com.feilong.taglib.display.httpconcat.command.HttpConcatParam;
import com.feilong.taglib.display.httpconcat.handler.ConcatLinkResolver;
import com.feilong.taglib.display.httpconcat.handler.DomainFormatter;
import com.feilong.taglib.display.httpconcat.handler.RootFormatter;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * The Class ContentBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public class ContentBuilder{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentBuilder.class);

    /**
     * 构造content.
     *
     * @param httpConcatParam
     *            the http concat param
     * @param itemSrcList
     *            the item src list
     * @param httpConcatGlobalConfig
     *            the http concat global config
     * @return the string
     * @since 1.4.1
     */
    public static String buildContent(
                    HttpConcatParam httpConcatParam,
                    List<String> itemSrcList,
                    HttpConcatGlobalConfig httpConcatGlobalConfig){
        // 标准化 httpConcatParam,比如list去重,标准化domain等等
        // 下面的解析均基于standardHttpConcatParam来操作
        // httpConcatParam只做入参判断,数据转换,以及cache存取
        HttpConcatParam standardHttpConcatParam = standardHttpConcatParam(httpConcatParam);

        //---------------------------------------------------------------
        String template = TemplateFactory.getTemplate(httpConcatGlobalConfig, standardHttpConcatParam.getType());

        boolean concatSupport = defaultIfNull(
                        httpConcatParam.getHttpConcatSupport(),
                        BooleanUtils.toBoolean(httpConcatGlobalConfig.getHttpConcatSupport()));

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(
                            "after standard HttpConcatParam info:{},itemSrcList info:[{}],concatSupport:[{}]",
                            JsonUtil.format(standardHttpConcatParam),
                            JsonUtil.format(itemSrcList),
                            concatSupport);
        }

        //---------------------------------------------------------------
        if (concatSupport){ // concat
            return MessageFormatUtil.format(template, ConcatLinkResolver.resolver(standardHttpConcatParam, itemSrcList));
        }

        // 本地开发环境支持的.
        StringBuilder sb = new StringBuilder();
        for (String itemSrc : itemSrcList){
            sb.append(MessageFormatUtil.format(template, ConcatLinkResolver.getNoConcatLink(itemSrc, standardHttpConcatParam)));
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
        HttpConcatParam standardHttpConcatParam = new HttpConcatParam();
        standardHttpConcatParam.setDomain(DomainFormatter.format(httpConcatParam.getDomain()));
        standardHttpConcatParam.setRoot(RootFormatter.format(httpConcatParam.getRoot()));
        standardHttpConcatParam.setHttpConcatSupport(httpConcatParam.getHttpConcatSupport());
        standardHttpConcatParam.setType(httpConcatParam.getType());
        standardHttpConcatParam.setVersion(httpConcatParam.getVersion());
        standardHttpConcatParam.setContent(httpConcatParam.getContent());
        return standardHttpConcatParam;
    }
}
