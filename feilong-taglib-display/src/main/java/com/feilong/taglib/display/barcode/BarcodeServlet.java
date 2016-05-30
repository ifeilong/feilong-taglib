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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.accessor.KeyAccessor;
import com.feilong.accessor.session.SessionKeyAccessor;
import com.feilong.core.UncheckedIOException;
import com.feilong.tools.barcode.BarcodeEncodeUtil;

/**
 * 渲染验证码的servlet.
 *
 * <pre class="code">
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
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.4
 */
public class BarcodeServlet extends HttpServlet{

    /** The Constant log. */
    private static final Logger LOGGER           = LoggerFactory.getLogger(BarcodeServlet.class);

    /** The Constant serialVersionUID. */
    private static final long   serialVersionUID = 231074760785325078L;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    @Override
    public void service(ServletRequest request,ServletResponse response) throws ServletException{
        String barcodeId = request.getParameter(BarcodeRequestParams.BARCODE_ID);
        Validate.notEmpty(barcodeId, "barcodeId can't be null/empty!");

        KeyAccessor keyAccessor = new SessionKeyAccessor();

        BarcodeContentsAndConfig barcodeContentsAndConfig = keyAccessor.get(barcodeId, (HttpServletRequest) request);
        Validate.notNull(barcodeContentsAndConfig, "barcodeContentsAndConfig can't be null!");

        render(barcodeContentsAndConfig, response);
    }

    /**
     * Encode.
     *
     * @param response
     *            the response
     * @param barcodeContentsAndConfig
     *            the barcode contents and config
     */
    private static void render(BarcodeContentsAndConfig barcodeContentsAndConfig,ServletResponse response){
        try{
            ServletOutputStream outputStream = response.getOutputStream();
            BarcodeEncodeUtil.encode(barcodeContentsAndConfig.getContents(), outputStream, barcodeContentsAndConfig.getBarcodeConfig());
        }catch (IOException e){
            LOGGER.error("", e);
            throw new UncheckedIOException(e);
        }
    }
}
