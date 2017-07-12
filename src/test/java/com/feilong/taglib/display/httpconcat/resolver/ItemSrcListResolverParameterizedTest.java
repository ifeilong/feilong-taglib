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

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.taglib.display.httpconcat.builder.TemplateFactory.TYPE_CSS;
import static com.feilong.taglib.display.httpconcat.builder.TemplateFactory.TYPE_JS;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.taglib.display.httpconcat.resolver.ItemSrcListResolver;
import com.feilong.test.AbstractThreeParamsAndOneResultParameterizedTest;

public class ItemSrcListResolverParameterizedTest extends AbstractThreeParamsAndOneResultParameterizedTest<String, String, String, String>{

    /**
     * Data.
     *
     * @return the iterable
     */
    @Parameters(name = "index:{index}: ItemSrcListResolver.build({0},{1})={2}")
    public static Iterable<Object[]> data(){
        Object[][] objects = new Object[][] { //
                                              {
                                                "<link rel=\"stylesheet\" href=\"http://css.feilong.com:8888/res/feilong/css/feilong-all.css\" type=\"text/css\"></link>",
                                                TYPE_CSS,
                                                "http://css.feilong.com:8888/",
                                                "res/feilong/css/feilong-all.css" },

                                              {
                                                "<link rel=\"stylesheet\" href=\"http://css.feilong.com:8888/res/feilong/css/feilong-all.css?version=12345666\" type=\"text/css\"></link>",
                                                TYPE_CSS,
                                                "http://css.feilong.com:8888/",
                                                "res/feilong/css/feilong-all.css" },

                                              {
                                                "<link rel=\"stylesheet\" href=\"res/feilong/css/feilong-all.css?version=12345666\" type=\"text/css\"></link>",
                                                TYPE_CSS,
                                                "http://css.feilong.com:8888/",
                                                "res/feilong/css/feilong-all.css" },

                                              //---------------------------------------------------------------

                                              {
                                                "<script type=\"text/javascript\" src=\"scripts/pdp/sub_salesProperties.js?2015\"></script>",
                                                TYPE_JS,
                                                "http://css.feilong.com:8888/",
                                                "scripts/pdp/sub_salesProperties.js" },

                                              {
                                                "<script type=\"text/javascript\" src=\"http://css.feilong.com:8888/scripts/pdp/sub_salesProperties.js?2015\"></script>",
                                                TYPE_JS,
                                                "http://css.feilong.com:8888/",
                                                "scripts/pdp/sub_salesProperties.js" },
                //
        };

        // <link rel="stylesheet" href="http://css.feilong.com:8888/res/feilong/css/feilong-all.css" type="text/css"></link>

        //<script type="text/javascript" src="scripts/pdp/sub_salesProperties.js?2015"></script>
        //<script type="text/javascript" src="scripts/pdp/pdp.js?2015"></script>
        //<script type="text/javascript" src="scripts/pdp/sub_sns.js?2015"></script>

        return toList(objects);
    }

    /**
     * Test get day of year.
     */
    @Test
    public void testBuild(){
        String result = ItemSrcListResolver.parse(input1, input2, input3);
        assertEquals(expectedValue, result);
    }

}