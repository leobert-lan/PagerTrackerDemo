# PagerTrackerDemo

这仅仅是一个demo，演示了一套用户路径埋点统计方案，具体方案的探索和讨论见我的博客：
[地址](https://blog.csdn.net/a774057695/article/details/106843586)

demo中演示了一些典型用法，包含收集环节、链路维护环节、上报触发环节如：

* 自动上报
* 手动上报
* 添加数据
* fragment具备独立点替代activity页面点
* fragment不具备独立点时不影响activity页面点

如果有疑惑可以提issue或者博客下评论，（但是可能因为工作原因回复没那么及时）

博客中提到了利用DummyPager来处理特殊情况的，如果有真是需要可以提issue，目前demo中没有写。

通过日志观测结果：

```
V/pager_track:  
V/pager_track:  
V/pager_track: print chain start========
V/pager_track: 0:PagerEntity(pagerToken='MainActivity_0', pagerPoint='P_1', reserveLimit=1, reserveConfig={}, data=[Pair{frompage }], bpToken='')
V/pager_track: 1:PagerEntity(pagerToken='IndexActivity_1', pagerPoint='P_INDEX_FG', reserveLimit=1, reserveConfig={}, data=[], bpToken='HomeFragment_2')
V/pager_track: 2:PagerEntity(pagerToken='EssayDetailActivity_7', pagerPoint='P_ESSAY_DETAIL', reserveLimit=1, reserveConfig={}, data=[Pair{id 321}, Pair{frompage P_INDEX_FG}], bpToken='')
V/pager_track: 3:PagerEntity(pagerToken='BioActivity_9', pagerPoint='P_BIO', reserveLimit=1, reserveConfig={}, data=[], bpToken='')
V/pager_track: print chain end=====
V/pager_track:  
V/pager_track:  
D/pager_track: onPointUpload:P_BIO , params: [{"first":"frompage","second":"P_ESSAY_DETAIL"}]
D/pager_track: report pager:P_BIO,from: P_ESSAY_DETAIL, chain: P_1 - P_INDEX_FG - P_ESSAY_DETAIL - P_BIO
```

页面链路变更时会打印出链路详细状态（最新的页面在最后）

onPointUpload 是上报的信息（模拟埋点上报）

report pager 是库内部的简要debug信息，触发上报时会打印，给出上报的页面点号、来源页、简要的全页面链路信息。

另外：tracker是library，如果客观分析后该方案适合您使用，可以遵循MIT协议自行移植。

**开源不易，觉得有帮助的朋友欢迎点个star或者给博客点个赞支持下**