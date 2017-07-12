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

import static com.feilong.core.date.DateExtensionUtil.formatDuration;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.coreextension.io.SerializableUtil;
import com.feilong.io.FileUtil;
import com.feilong.taglib.display.httpconcat.command.HttpConcatParam;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * The Class MapMemoryTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.7
 */
public class HttpConcatCachePerformanceTest extends BaseHttpConcatTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpConcatCachePerformanceTest.class);

    /**
     * Name.
     *
     * @throws IOException
     *             the IO exception
     */
    @Test
    public void name() throws IOException{
        Integer[] ints = {
                           //		                   1, 
                           //		                   500, 
                           //		                   5000, 
                           //				50000,
                           //				100000,
                           300001,
                //				500000,
                //				1000000,
                //				2000000 
        };

        //		Map<Integer, Object> map1 = new LinkedHashMap<>();

        HttpConcatParam httpConcatParam2 = getHttpConcatParam();
        String writeContent = HttpConcatUtil.getWriteContent(httpConcatParam2);

        for (Integer j : ints){
            // 先垃圾回收
            System.gc();
            long start = Runtime.getRuntime().freeMemory();

            HashMap<HttpConcatParam, String> map = new HashMap<>();
            for (int i = 0; i < j; i++){
                httpConcatParam2.setDomain("" + i);
                map.put(httpConcatParam2, writeContent);
            }
            // 快要计算的时,再清理一次
            System.gc();
            long end = Runtime.getRuntime().freeMemory();
            //对象占内存:
            //map1.put(j, FileUtil.formatSize((end - start)));

            LOGGER.debug(j + " size cache占用 内存 :" + FileUtil.formatSize((end - start)));

            int size = SerializableUtil.size(map);
            LOGGER.debug("Data Size: " + size + "--->" + FileUtil.formatSize(size));

            //map1.put(j, end - start);
        }

    }

    @Test
    public void performanceTest(){
        List<Integer> list = new ArrayList<>();

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
        //      list.add(300001);

        Map<Integer, Object> map = newLinkedHashMap(list.size());
        for (Integer j : list){
            Date beginDate = new Date();
            for (int i = 0; i < j; ++i){
                //LOGGER.debug(i);
                HttpConcatParam httpConcatParam = getHttpConcatParamByIndex(null);
                HttpConcatUtil.getWriteContent(httpConcatParam);
                //              httpConcatParam = null;
                //              System.gc();
            }
            map.put(j, formatDuration(beginDate));
        }
        LOGGER.debug(JsonUtil.format(map));
    }

    /**
     * 获得 write content.
     *
     * @return the write content
     */
    private String getWriteContent(){
        HttpConcatParam httpConcatParam2 = getHttpConcatParam();
        String writeContent = HttpConcatUtil.getWriteContent(httpConcatParam2);
        return writeContent;
    }

}
