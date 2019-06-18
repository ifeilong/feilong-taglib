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
package com.feilong.taglib.display.sitemap;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.date.DateUtil.now;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.MapUtil.newHashMap;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Test;

import com.feilong.io.IOWriteUtil;
import com.feilong.velocity.VelocityUtil;

/**
 * The Class Test1.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class SiteMapURLTest{

    /**
     * Test.
     *
     * @throws IllegalArgumentException
     *             the illegal argument exception
     * @throws IOException
     *             the IO exception
     */
    @Test
    public void test() throws IllegalArgumentException,IOException{
        String templateInClassPath = "velocity/sitemap.vm";
        Map<String, Object> contextKeyValues = newHashMap();

        List<SiteMapURL> siteMapURLList = newArrayList();
        SiteMapURL siteMapURL = new SiteMapURL();
        siteMapURL.setChangefreq(ChangeFreq.daily);
        siteMapURL.setLastmod(now());
        siteMapURL.setLoc(
                        "http://www.example.com/?>>> >>>>>>>>>>>><<<<<<<<<<<<<<<<<&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&'''''''''''''''''''''");
        siteMapURL.setPriority(0.5f);
        siteMapURLList.add(siteMapURL);

        siteMapURL = new SiteMapURL();
        siteMapURL.setChangefreq(ChangeFreq.monthly);
        //siteMapURL.setLastmod("2005-01-01");
        siteMapURL.setLoc("http://www.example.com/2");
        siteMapURL.setPriority(0.5f);
        siteMapURLList.add(siteMapURL);

        siteMapURL = new SiteMapURL();
        //siteMapURL.setChangefreq(ChangeFreq.monthly);
        //siteMapURL.setLastmod("2005-01-01");
        siteMapURL.setLoc("http://www.example.com/1");
        siteMapURL.setPriority(0.5f);
        siteMapURLList.add(siteMapURL);

        siteMapURL = new SiteMapURL();
        //siteMapURL.setChangefreq(ChangeFreq.monthly);
        //siteMapURL.setLastmod("2005-01-01");
        siteMapURL.setLoc("http://www.example.com/1");
        siteMapURL.setPriority(0.5f);
        siteMapURLList.add(siteMapURL);

        //siteMapURL = new SiteMapURL();
        //siteMapURL.setLoc("http://www.example.com/1");
        //siteMapURLList.add(siteMapURL);

        siteMapURLList.add(null);

        contextKeyValues.put("siteMapURLList", siteMapURLList);
        String aString = VelocityUtil.INSTANCE.parseTemplateWithClasspathResourceLoader(templateInClassPath, contextKeyValues);
        //xstre
        String filePath = SystemUtils.USER_HOME + "/feilong/sitemap.xml";

        //		Document document = Dom4jUtil.getDocument(filePath);
        //		
        //		//document.
        IOWriteUtil.writeStringToFile(filePath, aString, UTF8);
    }
}