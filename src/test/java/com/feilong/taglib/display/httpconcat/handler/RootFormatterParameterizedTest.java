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
package com.feilong.taglib.display.httpconcat.handler;

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.test.Abstract1ParamAndResultParameterizedTest;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.5
 */
public class RootFormatterParameterizedTest extends Abstract1ParamAndResultParameterizedTest<String, String>{

    private static final Logger LOGGER = LoggerFactory.getLogger(RootFormatterParameterizedTest.class);

    @Parameters(name = "index:{index}: RootFormatter.resolver({0})={1}")
    public static Iterable<Object[]> data(){
        Object[][] objects = new Object[][] { //
                                              { "", EMPTY },
                                              { null, EMPTY },
                                              { "  ", EMPTY },

                                              { "feilong", "feilong/" },
                                              { "feilong/", "feilong/" },
                                              { "/feilong", "feilong/" },
                                              { "/feilong/", "feilong/" },

                                              { "/feilong/static", "feilong/static/" },
                                              { "/feilong/static/", "feilong/static/" },
                //
        };
        return toList(objects);
    }

    @Test
    public void testResolver(){
        assertEquals(expectedValue, RootFormatter.format(input1));
    }
}
