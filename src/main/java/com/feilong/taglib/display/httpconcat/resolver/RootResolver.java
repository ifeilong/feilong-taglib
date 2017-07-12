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

import static com.feilong.core.Validator.isNullOrEmpty;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.feilong.core.lang.StringUtil;

/**
 * The Class DomainResolver.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.4
 */
public final class RootResolver{

    /** Don't let anyone instantiate this class. */
    private RootResolver(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * Do with root.
     *
     * @param root
     *            the root
     * @return the string
     */
    public static String resolver(String root){
        if (isNullOrEmpty(root)){
            return EMPTY;
        }

        // 格式化 root 成 xxxx/xxx/ 形式,
        if (!root.endsWith("/")){
            root = root + "/";
        }
        if (root.startsWith("/")){
            root = StringUtil.substring(root, 1);
        }
        return root;
    }

}
