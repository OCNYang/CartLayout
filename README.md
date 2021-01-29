# CartLayout(严格说应该叫 CartAdapter 的，现在将错就错吧 :grinning:)

[![GitHub issues](https://img.shields.io/github/issues/OCNYang/CartLayout.svg)](https://github.com/OCNYang/CartLayout/issues)&ensp;&ensp;
[![GitHub forks](https://img.shields.io/github/forks/OCNYang/CartLayout.svg)](https://github.com/OCNYang/CartLayout/network)&ensp;&ensp;
[![GitHub stars](https://img.shields.io/github/stars/OCNYang/CartLayout.svg)](https://github.com/OCNYang/CartLayout/stargazers)  

使用纯原生 RecyclerView 实现购物车效果（高仿京东购物车、淘宝购物车、天猫购物车）。不要问我为什么不使用 ExpandableListView (爱过，被坑的次数多了，就不爱了)。 

**Demo 中主要实现以下功能（基本上购物车的功能全实现了）：**  

- [x] 商品列表通过店铺进行分组显示；
- [x] 勾选店铺，联动店铺下的所有商品勾选；勾选商品，联动店铺的勾选；
- [x] 全选功能实现：联动商品、店铺的勾选按钮，反向联动亦然；
- [x] 实现编辑购物车商品的功能：点击编辑 > 删除勾选的商品；
- [x] 实现商品 item 长按弹出选项菜单，进行单个商品删除等操作功能；
- [x] 支持列表头部添加 tips ;
- [x] 统计勾选商品的个数、勾选商品价格等；
- [x] **折叠功能** 点击店铺标题，折叠起对应的商品条目；
- [ ] 添加折叠效果的动画效果

[![Version Code](https://img.shields.io/badge/Version%20Code-1.0.1-brightgreen.svg)](https://github.com/OCNYang/CartLayout/releases)  

<img src="./README_Res/CartAdapter.png" width = "400" alt="CartAdapter效果图" align=center />


## 导入方法：
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
	        implementation 'com.github.OCNYang:CartLayout:v1.0.3'
	}

## 使用方法：

创建 Adapter：

```
// 创建你自己的 Adapter 继承 CartAdapter 并实现对应方法。
public class MyAdapter extends CartAdapter<CartViewHolder> {

    public MainAdapter(...) {
        super(...);
    }

    // 返回对应的 itemViewHolder 
    @Override
    protected CartViewHolder getXXXViewHolder(View itemView) {}

    // 返回对应的 item 布局文件
    @Override
    protected int getXXXItemLayout() { return R.layout.activity_main_item_XXX; }

    // 根据布局类型渲染不同的布局
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ChildViewHolder) {
            ...
        } else if (holder instanceof GroupViewHolder) {
            ...
        } else if (holder instanceof NormalViewHolder) {
            ...
        }
    }
}
```

将 Adapter 设置给 RecyclerView:

```
recyclerView.setLayoutManager(new LinearLayoutManager(this));
mAdapter = new MyAdapter(this, getData());
// 设置是否可以折叠，默认不可折叠； (建议使用)也可以通过 Adapter 的构造方法传入 mAdapter = new MyAdapter(this, getData(), true);
mAdapter.setCanCollapsing(true);
// 设置勾选状态变化的监听器，用来统计总价等逻辑，（必须要设置）
mAdapter.setOnCheckChangeListener(new CartOnCheckChangeListener(recyclerView, mAdapter) {
    @Override
    public void onCalculateChanged(ICartItem cartItemBean) {
        calculate();
    }
});
recyclerView.setAdapter(mAdapter);

// 给列表注册 ContextMenu 事件。
registerForContextMenu(recyclerView);
```

> 更详细的使用方法请查看 Demo，Demo 中实现方式和各方法的作用在注释中写的很详细。

[APK下载地址](./README_Res/release/app-release.apk)
