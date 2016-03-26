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
package com.feilong.taglib.display.pager.command;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.lang.CharsetType;

/**
 * The Class PagerParamsTest.
 *
 * @author feilong
 * @version 1.0.7 2014年5月23日 下午10:31:59
 * @since 1.0.7
 */
public class PagerParamsTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PagerParamsTest.class);

    /**
     * Test method for {@link com.feilong.taglib.display.pager.command.PagerParams#hashCode()}.
     */
    @Test
    public void testHashCode(){
        PagerParams pagerParams1 = new PagerParams(0, "a");

        LOGGER.info("" + pagerParams1.hashCode());
        pagerParams1.setCharsetType(null);
        LOGGER.info("" + pagerParams1.hashCode());
    }

    /**
     * Test method for {@link com.feilong.taglib.display.pager.command.PagerParams#equals(java.lang.Object)}.
     */
    @SuppressWarnings("cast")
    @Test
    public void testEqualsObject(){
        PagerParams pagerParams1 = new PagerParams(0, "a");
        PagerParams pagerParams2 = new PagerParams(0, "a");

        Assert.assertEquals(true, pagerParams1.equals(pagerParams1));
        Assert.assertEquals(false, pagerParams1.equals(null));
        Assert.assertEquals(true, pagerParams1.equals(pagerParams2));

        pagerParams2.setCharsetType(CharsetType.UTF8);
        Assert.assertEquals(false, pagerParams1.equals(pagerParams2));

        pagerParams1.setCharsetType(CharsetType.UTF8);
        Assert.assertEquals(true, pagerParams1.equals(pagerParams2));

        pagerParams1.setCharsetType(null);
        Assert.assertEquals(false, pagerParams1.equals(pagerParams2));
        Assert.assertEquals(false, (null instanceof PagerParams));
        Assert.assertEquals(true, (pagerParams1 instanceof PagerParams));
        Assert.assertEquals(false, (null instanceof Object));
    }
}
