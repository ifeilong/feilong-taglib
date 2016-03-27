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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.Validator;
import com.feilong.taglib.display.pager.command.PagerParams;

/**
 * The Class PagerCacheManager.
 *
 * @author feilong
 * @version 1.4.0 2015年8月21日 上午11:32:27
 * @since 1.4.0
 * 
 * @see "com.google.common.cache.Cache"
 */
//XXX 将来可能会有更好的做法
//默认作用域
final class PagerCacheManager{

    /** The Constant LOGGER. */
    private static final Logger                   LOGGER       = LoggerFactory.getLogger(PagerCacheManager.class);

    /**
     * 设置缓存是否开启.
     * 
     * @since 1.0.7
     */
    private static final boolean                  CACHE_ENABLE = true;

    /**
     * 将结果缓存到map.
     * <p>
     * key是入参 {@link PagerParams}对象,value是解析完的分页字符串<br>
     * 该cache里面value不会存放null/empty
     * </p>
     * 
     * @since 1.0.7
     */
    private static final Map<PagerParams, String> CACHE        = new HashMap<PagerParams, String>();

    /** Don't let anyone instantiate this class. */
    private PagerCacheManager(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 从缓存中读取.
     *
     * @param pagerParams
     *            the pager params
     * @return the content from cache
     */
    static String getContentFromCache(PagerParams pagerParams){
        //缓存
        if (CACHE_ENABLE){
            LOGGER.debug("pagerCache.size:{}", CACHE.size());
            if (CACHE.containsKey(pagerParams)){
                LOGGER.info("hashcode:[{}],get pager info from pagerCache", pagerParams.hashCode());
                return CACHE.get(pagerParams);
            }
            LOGGER.info("hashcode:[{}],pagerCache not contains pagerParams,will do parse", pagerParams.hashCode());
        }else{
            LOGGER.info("the cache status is disable!");
        }
        return StringUtils.EMPTY;
    }

    /**
     * 设置.
     *
     * @param pagerParams
     *            the pager params
     * @param content
     *            the content
     */
    static void put(PagerParams pagerParams,String content){
        if (CACHE_ENABLE && Validator.isNotNullOrEmpty(content)){//设置cache
            CACHE.put(pagerParams, content);
        }
    }
}
