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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.feilong.core.CharsetType;
import com.feilong.core.Validator;
import com.feilong.core.net.ParamUtil;
import com.feilong.framework.accessor.KeyAccessor;
import com.feilong.framework.accessor.SessionKeyAccessor;
import com.feilong.servlet.http.RequestUtil;
import com.feilong.taglib.AbstractStartWriteContentTag;
import com.feilong.tools.barcode.BarcodeConfig;

/**
 * 二维码等barcode生成 标签.
 * 
 * <h3>核心原理:</h3>
 * <blockquote>
 * 
 * <p>
 * 和 {@link com.feilong.taglib.display.barcode.BarcodeServlet} 搭配着使用
 * </p>
 * 
 * <p>
 * 在使用标签的时候,页面会输出一段 img 标签, src路径会去访问{@link com.feilong.taglib.display.barcode.BarcodeServlet},从而输出二维码图片
 * </p>
 * </blockquote>
 * 
 * @author feilong
 * @version 1.5.4 2016年4月27日 下午12:42:36
 * @since 1.5.4
 */
public class BarcodeTag extends AbstractStartWriteContentTag{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID     = 7891625772743297912L;

    /** 用来标识唯一的barcode,这样同一个页面如果出现不同的barcode不会冲突. */
    private String            barcodeId;

    /** 生成二维码的内容,如果不设置默认是当前请求的url地址. */
    private String            contents;

    /** Barcode 宽度,默认是300. */
    private Integer           width                = 300;

    /** Barcode 高度,默认是300. */
    private Integer           height               = 300;

    /**
     * 指定边距,单位像素 .
     * 
     * @see com.google.zxing.EncodeHintType#MARGIN
     */
    private Integer           encodeHintTypeMargin = 1;

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.taglib.AbstractWriteContentTag#writeContent()
     */
    @Override
    protected Object buildContent(HttpServletRequest request){
        //把这些配置存储起来,以便在图片servlet获取
        BarcodeContentsAndConfig barcodeContentsAndConfig = buildBarcodeContentsAndConfig(request);

        KeyAccessor keyAccessor = new SessionKeyAccessor();
        keyAccessor.save(barcodeId, barcodeContentsAndConfig, request);

        return buildImgTag(request);
    }

    /**
     * Builds the image tag.
     *
     * @param request
     *            the request
     * @return the string
     */
    private String buildImgTag(HttpServletRequest request){
        String imageSrc = buildBarcodeImageSrc(request);

        StringBuilder imgTag = new StringBuilder();
        imgTag.append("<img");
        imgTag.append(" src=\"" + imageSrc + "\"");
        imgTag.append(" width=\"" + width + "\"");
        imgTag.append(" height=\"" + height + "\"");
        imgTag.append("/>");

        return imgTag.toString();
    }

    /**
     * 构造二维码图片请求src.
     * 
     * <p>
     * 设计成protected Access Modifiers,允许重写;<br>
     * 比如可以实现,判断是否是第一次访问,如果是,那么调用二维码APi创建二维码图片,保存到nginx可以访问的地址中,并在cache中记录下已经生成了图片,<br>
     * 下次再访问的话,那么直接加载这个图片地址
     * </p>
     *
     * @param request
     *            the request
     * @return the string
     */
    protected String buildBarcodeImageSrc(HttpServletRequest request){
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put(BarcodeRequestParams.BARCODE_ID, barcodeId);

        String servletPath = "/feilongbarcode";
        return request.getContextPath() + servletPath + "?" + ParamUtil.joinSingleValueMap(paramsMap);
    }

    /**
     * Builds the barcode contents and config.
     *
     * @param request
     *            the request
     * @return the barcode contents and config
     */
    private BarcodeContentsAndConfig buildBarcodeContentsAndConfig(HttpServletRequest request){
        String useContents = Validator.isNullOrEmpty(contents) ? RequestUtil.getRequestFullURL(request, CharsetType.UTF8) : contents;
        BarcodeConfig barcodeConfig = new BarcodeConfig();
        barcodeConfig.setHeight(height);
        barcodeConfig.setWidth(width);
        barcodeConfig.setEncodeHintTypeMargin(encodeHintTypeMargin);
        return new BarcodeContentsAndConfig(useContents, barcodeConfig);
    }

    //************************************************************************************************

    /**
     * 设置 生成二维码的内容,如果不设置默认是当前请求的url地址.
     *
     * @param contents
     *            the contents to set
     */
    public void setContents(String contents){
        this.contents = contents;
    }

    /**
     * 设置 barcode 宽度,默认是300.
     *
     * @param width
     *            the width to set
     */
    public void setWidth(Integer width){
        this.width = width;
    }

    /**
     * 设置 barcode 高度,默认是300.
     *
     * @param height
     *            the height to set
     */
    public void setHeight(Integer height){
        this.height = height;
    }

    /**
     * 设置 用来标识唯一的barcode,这样同一个页面如果出现不同的barcode不会冲突.
     *
     * @param barcodeId
     *            the barcodeId to set
     */
    public void setBarcodeId(String barcodeId){
        this.barcodeId = barcodeId;
    }

    /**
     * 设置 指定边距,单位像素 .
     *
     * @param encodeHintTypeMargin
     *            the encodeHintTypeMargin to set
     */
    public void setEncodeHintTypeMargin(Integer encodeHintTypeMargin){
        this.encodeHintTypeMargin = encodeHintTypeMargin;
    }
}
