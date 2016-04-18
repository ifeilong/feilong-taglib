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

有 

1.  [feilongDisplay pager J2EE分页解决方案](https://github.com/venusdrogon/feilong-taglib/wiki/feilongDisplay-pager-J2EE%E5%88%86%E9%A1%B5%E8%A7%A3%E5%86%B3%E6%96%B9%E6%A1%88 "feilongDisplay pager J2EE分页解决方案") 
2.  [isContain-taglib](https://github.com/venusdrogon/feilong-taglib/wiki/isContain-taglib "isContain-taglib") 
3.  [isInTime-taglib](https://github.com/venusdrogon/feilong-taglib/wiki/isInTime-taglib "isInTime-taglib") 


#说明

1. 基于`Apache2` 协议,您可以下载,代码用于闭源项目,但每个修改的过的文件必须放置版权说明;
1. 基于`maven3.2`构建;
1. 1.5.0及以上版本需要`jdk1.7`及以上环境(1.5.0以下版本需要`jdk1.6`及以上环境);


# Maven使用配置

```XML
	<project>
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
				<version>1.5.3</version>
			</dependency>
			<dependency>
				<groupId>com.feilong.platform.taglib</groupId>
				<artifactId>feilong-taglib-display</artifactId>
				<version>1.5.3</version>
			</dependency>
			....
		</dependencies>
		
		....
		
	</project>
```

# feilong-platform module:

Category |Name | Description | JDK编译版本
----|------------ | ---------|------------
taglib |`feilong-taglib-common` | 封装了常用的自定义标签 | 1.7
taglib |`feilong-taglib-display` | 封装了常用的自定义标签 | 1.7


# About

如果您对feilong platform 有任何建议，可以使用下面的联系方式：

* 新浪微博:http://weibo.com/venusdrogon
* iteye博客:http://feitianbenyue.iteye.com/