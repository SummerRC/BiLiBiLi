#android OneXlistview 1.1.0 说明#


##changelog##
2013-05-13
bugfix: XMultiColumnListView pull listener
improve: xListview stoprefresh methond

2013-05-12

update sticklistview

many bug fix xListview

improve xlistview refresh loadmore listener

##原始组件说明##
整合和扩展了如下项目


1. [XListview](https://github.com/Maxwin-z/XListView-Android)
> 上拉加载,下拉刷新,listview

2. [PinterestLikeAdapterView](https://github.com/huewu/PinterestLikeAdapterView)
>  类似于pinterest瀑布流实现

3. [https://github.com/emilsjolander/StickyListHeaders](https://github.com/emilsjolander/StickyListHeaders)
> 列表头部固定的

4. [https://github.com/woozzu/IndexableListView](https://github.com/woozzu/IndexableListView)
> 列表快速索引

**以上项目的版权声明均为 Apache License 2.0**

##扩展说明##
在这些原始项目,并不是所有项目都支持上拉加载,下拉刷新,于是,我在原始项目的基础上扩展出一系列XXlistview, 把xlistview上拉,下拉机制对相应项目进行改造,是这些原本不具备上拉,下拉机制的listview都拥有这个功能. 并且对原xlistview 的存在的bug 也进行了相应的修复.

###xListview changlog###
bugfix: 刷新,加载更多,还没加载完毕的时候在进行上下拉导致的重复加载

bugfix: item 数目不满一屏幕的时候显示更多加载按钮

improve: 正在刷新,或者加载更多的时候不应该可以继续拉
##扩展的view##
以下view 如无特殊说明都支持上下拉

1. XIndexableView
> 具有快速索引的listview

2. XMultiColumnListView
> 扩展自原瀑布流

3. XStickyListHeadersIndexableView
> 具有固定头部,且支持快速索引

4. XStickyListHeadersView
> 固定头部

##使用##
要开启上下拉很简单

开启上拉

`mListView.setPullLoadEnable(true);`

开启下拉(默认开启)

`mListView.setPullRefreshEnable(true);`

监听器

`mListView.setXListViewListener(this);`

由于demo 可能需要点时间,用法跟listview一样,等不及演示demo可以去看原项目的demo,基本没有区别.
##ToDo list##
1. 实现跟便捷的自定义上下拉view机制
2. 演示用demo
3. 写相应的文档

Copyright 2012 youxiachai

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

