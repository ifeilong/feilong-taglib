feilong-platform feilong-taglib
================

[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

Reduce development, Release ideas

            .--.
           /    \
          ## a  a
          (   '._)
           |'-- |
         _.\___/_   ___feilong___
       ."\> \Y/|<'.  '._.-'
      /  \ \_\/ /  '-' /
      | --'\_/|/ |   _/
      |___.-' |  |`'`
        |     |  |
        |    / './
       /__./` | |
          \   | |
           \  | |
           ;  | |
           /  | |
     jgs  |___\_.\_
          `-"--'---'


#Welcome to feilong-platform feilong-taglib

`封装了常用的自定义标签`

主要由两部分组成

## common 包含

包含所有自定义标签的base类,以及常用的自定义标签和el function

taglib	|说明	
:---- | :---------
[isContains](https://github.com/venusdrogon/feilong-taglib/wiki/feilong-isContains "isContains") 	|判断一个值,是否在一个集合(或者可以被转成Iterator)当中
[isInTime](https://github.com/venusdrogon/feilong-taglib/wiki/feilong-isInTime "isInTime") 	|判断一个日期(date),是否在一个时间区间内(beginDate,endDate)


## Display 

包含页面渲染显示的标签

taglib	|说明	
:---- | :---------
[pager](https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-pager "J2EE分页解决方案") 	|J2EE分页解决方案
[concat](https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-concat "feilongDisplay concat")  	| jsp版本的 "css/js合并以及版本控制"的标签 
[option](https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-option "feilongDisplay-option")  	|用来基于 i18n配置文件,渲染select option选项,实现国际化功能,简化开发
[barcode](https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-barcode "feilongDisplay barcode")  	|用来在页面生成二维码


#说明

1. 基于`Apache2` 协议,您可以下载,代码用于闭源项目,但每个修改的过的文件必须放置版权说明;
1. 基于`maven3.3`构建;
1. 1.5.0及以上版本需要`jdk1.7`及以上环境(1.5.0以下版本需要`jdk1.6`及以上环境);


# Maven使用配置

```XML

	<project>
	
		....
		<properties>
			<version.feilong-platform>1.7.0</version.feilong-platform>
			....
		</properties>
		
		....
		<repositories>
			<repository>
				<id>feilong-repository</id>
				<url>https://raw.github.com/venusdrogon/feilong-platform/repository</url>
			</repository>
		</repositories>
		
		....
		<dependencies>
			....
			<dependency>
				<groupId>com.feilong.platform.taglib</groupId>
				<artifactId>feilong-taglib-common</artifactId>
				<version>${version.feilong-platform}</version>
			</dependency>
			<dependency>
				<groupId>com.feilong.platform.taglib</groupId>
				<artifactId>feilong-taglib-display</artifactId>
				<version>${version.feilong-platform}</version>
			</dependency>
			....
		</dependencies>
		
		....
		
	</project>
```

# feilong-platform module:

Category |Name | Description 
----|------------ | ---------
taglib |`feilong-taglib-common` | 封装了常用的自定义标签 
taglib |`feilong-taglib-display` | 封装了常用的用来页面显示用的标签 


# About

如果您对feilong platform 有任何建议，可以使用下面的联系方式：

* 新浪微博:http://weibo.com/venusdrogon
* iteye博客:http://feitianbenyue.iteye.com/