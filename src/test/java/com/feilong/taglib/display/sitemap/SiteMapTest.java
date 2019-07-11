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

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Test;

import com.feilong.coreextension.awt.DesktopUtil;
import com.feilong.io.IOWriteUtil;
import com.feilong.velocity.VelocityUtil;

public class SiteMapTest{

    String templateInClassPath = "velocity/sitemap.template.xml";

    @Test
    public void test(){

        //创建Sitemap时有哪些注意事项？
        //1，一个Sitemap文件包含的网址不得超过 5 万个，且文件大小不得超过 10 MB。如果您的Sitemap超过了这些限值，请将其拆分为几个小的Sitemap。这些限制条件有助于确保您的网络服务器不会因提供大文件而超载。
        //2，一个站点支持提交的sitemap文件个数必须小于5万个，多于5万个后会不再处理，并显示“链接数超”的提示。    
        //3，如果验证了网站的主域，那么Sitemap文件中可包含该网站主域下的所有网址。

        Map<String, Object> contextKeyValues = newHashMap();
        contextKeyValues.put("siteMapURLList", build());

        String aString = VelocityUtil.INSTANCE.parseTemplateWithClasspathResourceLoader(templateInClassPath, contextKeyValues);
        String filePath = SystemUtils.USER_HOME + "/feilong/sitemap.xml";

        IOWriteUtil.writeStringToFile(filePath, aString, UTF8);
        DesktopUtil.open(filePath);
    }

    protected List<SiteMapEntity> build(){
        List<SiteMapEntity> siteMapURLList = newArrayList();
        SiteMapEntity siteMapEntity = new SiteMapEntity();
        siteMapEntity.setChangefreq(ChangeFreq.DAILY);
        siteMapEntity.setLastmod(now());
        siteMapEntity.setLoc(
                        "http://www.example.com/?>>> >>>>>>>>>>>><<<<<<<<<<<<<<<<<&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&'''''''''''''''''''''");
        siteMapEntity.setPriority(0.5f);
        siteMapURLList.add(siteMapEntity);

        siteMapEntity = new SiteMapEntity();
        siteMapEntity.setChangefreq(ChangeFreq.MONTHLY);
        //siteMapURL.setLastmod("2005-01-01");
        siteMapEntity.setLoc("http://www.example.com/2");
        siteMapEntity.setPriority(0.5f);
        siteMapURLList.add(siteMapEntity);

        siteMapEntity = new SiteMapEntity();
        //siteMapURL.setChangefreq(ChangeFreq.monthly);
        //siteMapURL.setLastmod("2005-01-01");
        siteMapEntity.setLoc("http://www.example.com/1");
        siteMapEntity.setPriority(0.5f);
        siteMapURLList.add(siteMapEntity);

        siteMapEntity = new SiteMapEntity();
        //siteMapURL.setChangefreq(ChangeFreq.monthly);
        //siteMapURL.setLastmod("2005-01-01");
        siteMapEntity.setLoc("http://www.example.com/1");
        siteMapEntity.setPriority(0.5f);
        siteMapURLList.add(siteMapEntity);

        siteMapURLList.add(null);
        return siteMapURLList;
    }
}