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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.Validate;

import com.feilong.tools.barcode.BarcodeEncodeUtil;

/**
 * 渲染验证码的servlet.
 *
 * <pre>
{@code
    <!-- barcode -->
    <servlet>
        <servlet-name>feilong-barcode</servlet-name>
        <servlet-class>com.feilong.taglib.display.barcode.BarcodeServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>feilong-barcode</servlet-name>
        <url-pattern>/feilongbarcode</url-pattern>
    </servlet-mapping>
}
 * </pre>
 *
 * @author feilong
 * @version 1.5.4 2016年4月27日 下午12:53:56
 * @since 1.5.4
 */
public class BarcodeServlet extends HttpServlet{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 231074760785325078L;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    @Override
    public void service(ServletRequest request,ServletResponse response) throws ServletException,IOException{
        String barcodeId = request.getParameter(BarcodeRequestParams.BARCODE_ID);
        Validate.notEmpty(barcodeId, "barcodeId can't be null/empty!");

        Accessor accessor = new SessionAccessor();

        BarcodeContentsAndConfig barcodeContentsAndConfig = accessor.get(barcodeId, (HttpServletRequest) request);
        Validate.notNull(barcodeContentsAndConfig, "barcodeContentsAndConfig can't be null!");

        ServletOutputStream outputStream = response.getOutputStream();
        BarcodeEncodeUtil.encode(barcodeContentsAndConfig.getContents(), outputStream, barcodeContentsAndConfig.getBarcodeConfig());
    }

}
