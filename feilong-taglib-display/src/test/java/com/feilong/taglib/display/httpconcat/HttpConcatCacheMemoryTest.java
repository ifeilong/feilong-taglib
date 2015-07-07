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

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.io.FileUtil;
import com.feilong.core.lang.ObjectUtil;
import com.feilong.taglib.display.httpconcat.command.HttpConcatParam;

/**
 * The Class MapMemoryTest.
 * 
 * @author feilong
 * @version 1.0.7 2014年5月24日 下午11:23:49
 * @since 1.0.7
 */
public class HttpConcatCacheMemoryTest extends BaseHttpConcatTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpConcatCacheMemoryTest.class);

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

        //		Map<Integer, Object> map1 = new LinkedHashMap<Integer, Object>();

        HttpConcatParam httpConcatParam2 = getHttpConcatParam();
        String writeContent = HttpConcatUtil.getWriteContent(httpConcatParam2);

        for (Integer j : ints){
            // 先垃圾回收
            System.gc();
            long start = Runtime.getRuntime().freeMemory();

            //			if (LOGGER.isInfoEnabled()){
            //				LOGGER.info("" + start);
            //			}
            HashMap<HttpConcatParam, String> map = new HashMap<HttpConcatParam, String>();

            for (int i = 0; i < j; i++){

                httpConcatParam2.setDomain("" + i);
                map.put(httpConcatParam2, writeContent);
            }
            // 快要计算的时,再清理一次
            System.gc();
            long end = Runtime.getRuntime().freeMemory();
            //			if (LOGGER.isInfoEnabled()){
            //				LOGGER.info("" + end);
            //			}
            //对象占内存:
            //map1.put(j, FileUtil.formatSize((end - start)));

            LOGGER.info(j + " size cache占用 内存 :" + FileUtil.formatSize((end - start)));

            int size = ObjectUtil.size(map);
            LOGGER.info("Data Size: " + size + "--->" + FileUtil.formatSize(size));

            //map1.put(j, end - start);
        }

        //		if (LOGGER.isInfoEnabled()){
        //			LOGGER.info(JsonUtil.format(map1));
        //		}
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
