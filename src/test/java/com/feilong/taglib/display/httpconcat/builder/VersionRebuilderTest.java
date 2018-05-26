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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.1
 */
public class VersionRebuilderTest{

    private static final Logger LOGGER = LoggerFactory.getLogger(VersionRebuilderTest.class);

    @Test
    public void test(){
        assertEquals("aa", VersionRebuilder.doWithAutoRefresh("aaa"));
    }

    @Test
    public void test1(){
        LOGGER.debug(VersionRebuilder.doWithAutoRefresh("default_Version_Maven_Install_Will_Replace"));
    }
}
