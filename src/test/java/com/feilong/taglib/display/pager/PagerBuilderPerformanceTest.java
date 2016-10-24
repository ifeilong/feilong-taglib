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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.date.DateUtil;
import com.feilong.coreextension.awt.DesktopUtil;
import com.feilong.io.FileUtil;
import com.feilong.io.IOWriteUtil;
import com.feilong.taglib.display.pager.command.PagerParams;
import com.feilong.taglib.display.pager.command.PagerType;
import com.feilong.tools.ChartIndexUtil;
import com.feilong.tools.jsonlib.JsonUtil;
import com.feilong.tools.velocity.ToolVelocityUtil;

import static com.feilong.core.CharsetType.UTF8;

import static com.feilong.core.DatePattern.TIMESTAMP_WITH_MILLISECOND;

/**
 * The Class PagerBuilderPerformanceTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class PagerBuilderPerformanceTest extends BasePagerTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PagerBuilderPerformanceTest.class);

    /**
     * Test get pager content 1.
     */
    @Test
    public void testGetPagerContent1(){
        Map<Integer, Long> dataMap = PerformanceTestUtil.run(new Runnable(){

            @Override
            public void run(){
                testGetPagerContent();

            }
        }, 1, 1, 100, 500, 20000, 80000);

        LOGGER.debug(JsonUtil.format(dataMap));

        Map<String, Object> map = new HashMap<>();
        map.put("data", dataMap);
        map.put("dataProvider", JsonUtil.format(ChartIndexUtil.toChartIndexListByValue(dataMap)));
        writeAndOpen(
                        new VelocityFileData(
                                        "velocity/test.vm",
                                        map,
                                        "C:/Users/feilong/feilong/taglib/pager/" + DateUtil.toString(new Date(), TIMESTAMP_WITH_MILLISECOND)
                                                        + ".html"));
    }

    /**
     * <p>
     * 如果 <code>outPutFilePath</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>outPutFilePath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * </p>
     *
     * @param velocityFileData
     *            the velocity file data
     */
    public void writeAndOpen(VelocityFileData velocityFileData){
        String outPutFilePath = velocityFileData.getOutPutFilePath();
        Validate.notBlank(outPutFilePath, "outPutFilePath can't be null/empty!");

        String vmPath = velocityFileData.getVmPath();
        Validate.notBlank(vmPath, "vmPath can't be blank!");

        //*******************************************************************
        String content = new ToolVelocityUtil().parseTemplateWithClasspathResourceLoader(vmPath, velocityFileData.getData());
        IOWriteUtil.writeStringToFile(outPutFilePath, content, UTF8);

        DesktopUtil.open(FileUtil.getParent(outPutFilePath));//和输出文件同级目录
        DesktopUtil.open(outPutFilePath);
    }

    /**
     * Test get pager content.
     * 
     */
    @Test
    public void testGetPagerContent(){
        PagerParams pagerParams = getPagerParams();
        pagerParams.setPagerType(PagerType.NO_REDIRECT);

        String content = PagerBuilder.buildContent(pagerParams);

        if (false){
            String filePath = "F://pagerTest.html";
            IOWriteUtil.writeStringToFile(filePath, content, UTF8);
            DesktopUtil.browse(filePath);
        }
    }
}