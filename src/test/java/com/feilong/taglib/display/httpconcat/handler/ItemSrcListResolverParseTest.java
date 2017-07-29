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

import org.junit.Test;

public class ItemSrcListResolverParseTest{

    @Test(expected = NullPointerException.class)
    public void testItemSrcListResolverTestItemNull(){
        ItemSrcListResolver.parse(null, "js", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testItemSrcListResolverTestItemEmpty(){
        ItemSrcListResolver.parse("", "js", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testItemSrcListResolverTestItemBlank(){
        ItemSrcListResolver.parse(" ", "js", "");
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testItemSrcListResolverTestTypeNull(){
        ItemSrcListResolver.parse("feilong.js", null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testItemSrcListResolverTestTypeEmpty(){
        ItemSrcListResolver.parse("feilong.js", "", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testItemSrcListResolverTestTypeBlank(){
        ItemSrcListResolver.parse("feilong.js", " ", "");
    }

}