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
package com.feilong.taglib.display.barcode;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * 存取器.
 *
 * @author feilong
 * @version 1.5.4 2016年4月27日 下午4:10:33
 * @since 1.5.4
 */
public interface Accessor{

    /**
     * 保存.
     *
     * @param key
     *            标识,下次就拿这个标识来取就可以了
     * @param serializable
     *            the serializable
     * @param request
     *            the request
     */
    void save(String key,Serializable serializable,HttpServletRequest request);

    /**
     * 获取.
     *
     * @param <T>
     *            the generic type
     * @param key
     *            标识,和 {@link #save(String, Serializable, HttpServletRequest)}里面的 key相等,用什么存就用什么取
     * @param request
     *            the request
     * @return the barcode contents and config
     */
    <T extends Serializable> T get(String key,HttpServletRequest request);
}
