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

import static com.feilong.core.date.DateExtensionUtil.formatDuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.taglib.display.pager.command.PagerConstants;
import com.feilong.taglib.display.pager.command.PagerParams;

public class IndexAndHrefMapTest extends BasePagerTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexAndHrefMapTest.class);

    @Test
    public void getAllIndexAndHrefMap() throws IllegalArgumentException,IllegalAccessException,InvocationTargetException,SecurityException,
                    NoSuchMethodException{

        int z = 80000;

        Date beginDate = new Date();

        Method declaredMethod = PagerBuilder.class.getDeclaredMethod("getAllIndexAndHrefMap", PagerParams.class, Set.class);
        declaredMethod.setAccessible(true);

        Set<Integer> set = new HashSet<>();
        set.add(PagerConstants.DEFAULT_TEMPLATE_PAGE_NO);

        for (int i = 100; i < 120; ++i){
            set.add(i);
        }
        PagerParams pagerParams = getPagerParams();
        LOGGER.debug(JsonUtil.format(pagerParams));

        for (int j = 0; j < z; ++j){
            //@SuppressWarnings({ "unchecked", "unused" })
            //Map<Integer, String> invoke = (Map<Integer, String>) 
            declaredMethod.invoke(PagerBuilder.class, pagerParams, set);
        }

        LOGGER.debug("{},time:{}", z, formatDuration(beginDate));
    }
}