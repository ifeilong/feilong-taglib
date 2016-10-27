feilong-taglib
================

[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
![build](https://img.shields.io/jenkins/s/https/jenkins.qa.ubuntu.com/precise-desktop-amd64_default.svg "build") 
![JDK 1.7](https://img.shields.io/badge/JDK-1.7-green.svg "JDK 1.7")

Reduce development, Release ideas

#Welcome to feilong-taglib

`封装了常用的自定义标签`

主要由两部分组成

## :rat: Common

包含所有`自定义标签的base类`,以及`常用的自定义标签` 和`el function`

taglib	|说明	
:---- | :---------
[isContains](https://github.com/venusdrogon/feilong-taglib/wiki/feilong-isContains "isContains") 	|判断一个值,是否在一个集合(或者可以被转成Iterator)当中
[isInTime](https://github.com/venusdrogon/feilong-taglib/wiki/feilong-isInTime "isInTime") 	|判断一个日期(date),是否在一个时间区间内(beginDate,endDate)


## :dromedary_camel: Display

包含页面`渲染显示`的标签

taglib	|说明	
:---- | :---------
[pager](https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-pager "J2EE分页解决方案") 	|J2EE分页解决方案
[concat](https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-concat "feilongDisplay concat")  	| jsp版本的 "css/js合并以及版本控制"的标签 
[option](https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-option "feilongDisplay-option")  	|用来基于 i18n配置文件,渲染select option选项,实现国际化功能,简化开发
[barcode](https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-barcode "feilongDisplay barcode")  	|用来在页面生成二维码

# :dragon: Maven使用配置

taglib jar你可以直接在 [仓库]( https://github.com/venusdrogon/feilong-platform/tree/repository/com/feilong/platform/taglib "仓库") 浏览 

如果你使用 `maven`, 您可以通过以下方式来配置 `pom.xml`:

```XML

	<project>
	
		....
		<properties>
			<version.feilong-platform>1.9.2</version.feilong-platform>
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
			
			<!--feilong-taglib -->
			<dependency>
				<groupId>com.feilong.platform</groupId>
				<artifactId>feilong-taglib</artifactId>
				<version>${version.feilong-platform}</version>
			</dependency>
			....
		</dependencies>
		
		....
		
	</project>
```

# :memo: 说明

1. 基于 [Apache2](https://www.apache.org/licenses/LICENSE-2.0) 协议,您可以下载代码用于闭源项目,但每个修改的过的文件必须放置版权说明;
1. 1.5.0及以上版本需要`jdk1.7`及以上环境(1.5.0以下版本需要`jdk1.6`及以上环境);

# :cyclone: feilong 即时交流

微信公众号 `feilongjava`							|QQ 群 `243306798`
:---- 										|:---------
 ![](http://i.imgur.com/hM83Xv9.jpg)		|![](http://i.imgur.com/cIfglCa.png)

# :panda_face: About

如果您对本项目有任何建议和批评,可以使用下面的联系方式：

* iteye博客:http://feitianbenyue.iteye.com/