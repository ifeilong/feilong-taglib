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
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.taglib.display.httpconcat.command.HttpConcatParam;
import com.feilong.tools.jsonlib.JsonUtil;

import static com.feilong.core.date.DateExtensionUtil.getIntervalForView;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;

/**
 * The Class HttpConcatUtilTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.7
 */
public class HttpConcatUtilTest extends BaseHttpConcatTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpConcatUtilTest.class);

    @Test
    public void testGetWriteContent(){
        HttpConcatParam httpConcatParam = getHttpConcatParam();
        String writeContent = HttpConcatUtil.getWriteContent(httpConcatParam);
        LOGGER.debug(writeContent);
    }

    /**
     * Performance test.
     */
    @Test
    public void performanceTest(){
        List<Integer> list = new ArrayList<Integer>();

        list.add(2);
        list.add(10);
        list.add(100);
        list.add(1000);
        list.add(5000);
        list.add(10000);
        list.add(20000);
        list.add(50000);
        list.add(100000);
        list.add(1000000);
        //		list.add(300001);

        Map<Integer, Object> map = newLinkedHashMap(list.size());
        for (Integer j : list){
            Date beginDate = new Date();
            for (int i = 0; i < j; ++i){
                //LOGGER.debug(i);
                HttpConcatParam httpConcatParam = getHttpConcatParamByIndex(null);
                HttpConcatUtil.getWriteContent(httpConcatParam);
                //				httpConcatParam = null;
                //				System.gc();
            }
            map.put(j, getIntervalForView(beginDate));
        }
        LOGGER.debug(JsonUtil.format(map));
    }

    /**
     * Test get write content1.
     */
    @Test
    public void testGetWriteContent1(){
        HttpConcatParam httpConcatParam = new HttpConcatParam();
        httpConcatParam.setType("js");
        //httpConcatParam.setDomain("http://www.feilong.com");
        httpConcatParam.setRoot("/js/");
        httpConcatParam.setHttpConcatSupport(true);
        httpConcatParam.setItemSrcList(null);
        httpConcatParam.setVersion("20140517");
        LOGGER.debug(HttpConcatUtil.getWriteContent(httpConcatParam));
    }
}
