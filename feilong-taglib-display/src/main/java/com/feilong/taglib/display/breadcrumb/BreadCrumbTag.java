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
package com.feilong.taglib.display.breadcrumb;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.util.Validator;
import com.feilong.taglib.base.AbstractStartWriteContentTag;
import com.feilong.taglib.display.breadcrumb.command.BreadCrumbConstants;
import com.feilong.taglib.display.breadcrumb.command.BreadCrumbEntity;
import com.feilong.taglib.display.breadcrumb.command.BreadCrumbParams;

/**
 * 飞龙面包屑标签.
 *
 * @author feilong
 * @version 1.0.0 2010-6-8 上午05:50:38
 * @version 1.2.2 2015年7月17日 上午12:09:08
 */
//TODO ADD javadoc
public class BreadCrumbTag extends AbstractStartWriteContentTag{

    /** The Constant serialVersionUID. */
    private static final long              serialVersionUID = -8596553099620845748L;

    /** The Constant LOGGER. */
    private static final Logger            LOGGER           = LoggerFactory.getLogger(BreadCrumbTag.class);

    /** breadCrumbEntityList,用户所有可以访问的菜单url List,不要求已经排完序. */
    private List<BreadCrumbEntity<Object>> breadCrumbEntityList;

    /** url前缀, 用来拼接 {@link BreadCrumbEntity#getPath()},可以不设置,那么原样输出{@link BreadCrumbEntity#getPath()}. */
    private String                         urlPrefix        = StringUtils.EMPTY;

    /** 连接符,默认>. */
    private String                         connector        = BreadCrumbConstants.DEFAULT_CONNECTOR;

    /** vm的路径. */
    private String                         vmPath           = BreadCrumbConstants.DEFAULT_TEMPLATE_IN_CLASSPATH;

    /**
     * 实现自定义站点地图数据提供程序的途径.
     * 
     * @return the object
     */
    @Override
    protected Object writeContent(){
        List<BreadCrumbEntity<Object>> opBreadCrumbEntityList = constructBreadCrumbEntityList();

        if (Validator.isNullOrEmpty(opBreadCrumbEntityList)){
            LOGGER.warn("breadCrumbEntityList is NullOrEmpty!!,return empty!!");
            return StringUtils.EMPTY;
        }

        BreadCrumbParams breadCrumbParams = new BreadCrumbParams();
        breadCrumbParams.setBreadCrumbEntityList(opBreadCrumbEntityList);
        breadCrumbParams.setConnector(connector);
        breadCrumbParams.setVmPath(vmPath);
        breadCrumbParams.setUrlPrefix(urlPrefix);
        return BreadCrumbUtil.getBreadCrumbContent(breadCrumbParams);
    }

    /**
     * Construct bread crumb entity list.
     *
     * @return the list< bread crumb entity< object>>
     */
    protected List<BreadCrumbEntity<Object>> constructBreadCrumbEntityList(){
        return breadCrumbEntityList;
    }

    /**
     * 设置 连接符,默认>.
     *
     * @param connector
     *            the connector to set
     */
    public void setConnector(String connector){
        this.connector = connector;
    }

    /**
     * 设置 siteMapEntityList,用户所有可以访问的菜单url List.
     *
     * @param breadCrumbEntityList
     *            the breadCrumbEntityList to set
     */
    public void setBreadCrumbEntityList(List<BreadCrumbEntity<Object>> breadCrumbEntityList){
        this.breadCrumbEntityList = breadCrumbEntityList;
    }

    /**
     * 设置 vm的路径.
     *
     * @param vmPath
     *            the vmPath to set
     */
    public void setVmPath(String vmPath){
        this.vmPath = vmPath;
    }

    /**
     * url前缀, 用来拼接 {@link BreadCrumbEntity#getPath()},可以不设置,那么原样输出{@link BreadCrumbEntity#getPath()}.
     *
     * @param urlPrefix
     *            the urlPrefix to set
     */
    public void setUrlPrefix(String urlPrefix){
        this.urlPrefix = urlPrefix;
    }

    /**
     * 获得 url前缀, 用来拼接 {@link BreadCrumbEntity#getPath()},可以不设置,那么原样输出{@link BreadCrumbEntity#getPath()}.
     *
     * @return the urlPrefix
     */
    public String getUrlPrefix(){
        return urlPrefix;
    }

    /**
     * 获得 连接符,默认>.
     *
     * @return the connector
     */
    public String getConnector(){
        return connector;
    }

    /**
     * 获得 vm的路径.
     *
     * @return the vmPath
     */
    public String getVmPath(){
        return vmPath;
    }
}
