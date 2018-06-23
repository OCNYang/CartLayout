# CartLayout

[![GitHub issues](https://img.shields.io/github/issues/OCNYang/CartLayout.svg)](https://github.com/OCNYang/CartLayout/issues)&ensp;&ensp;
[![GitHub forks](https://img.shields.io/github/forks/OCNYang/CartLayout.svg)](https://github.com/OCNYang/CartLayout/network)&ensp;&ensp;
[![GitHub stars](https://img.shields.io/github/stars/OCNYang/CartLayout.svg)](https://github.com/OCNYang/CartLayout/stargazers)  

使用纯原生 RecyclerView 实现购物车效果（高仿京东购物车、淘宝购物车、天猫购物车）。不要问我为什么不使用 ExpandableListView (爱过，被坑的次数多了，就不爱了)。 

**Demo 中主要实现以下功能（基本上购物车的功能全实现了）：**  
* 商品列表通过店铺进行分组显示；
* 勾选店铺，联动店铺下的所有商品勾选；勾选商品，联动店铺的勾选；
* 全选功能实现：联动商品、店铺的勾选按钮，反向联动亦然；
* 实现编辑购物车商品的功能：点击编辑 > 删除勾选的商品；
* 实现商品 item 长按弹出选项菜单，进行单个商品删除等操作功能；
* 支持列表头部添加 tips ;
* 统计勾选商品的个数、勾选商品价格等。

[![Version Code](https://img.shields.io/badge/Version%20Code-1.0.0-brightgreen.svg)](https://github.com/OCNYang/CartLayout/releases)  

<img src="./README_Res/CartAdapter.png" width = "400" alt="CartAdapter效果图" align=center />


## 使用方法：
To get a Git project into your build:

### Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}


### Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.OCNYang:CartLayout:v1.0'
	}

> 更详细的使用方法请查看 Demo，Demo中实现方式和各方法的作用在注释中写的很详细。

[APK下载地址](./README_Res/release/app-release.apk)