layui.use('flow', function(){
    var $ = layui.jquery; //不用额外加载jQuery，flow模块本身是有依赖jQuery的，直接用即可。
    var flow = layui.flow;
    flow.load({
        elem: '#demo' //指定列表容器
        ,done: function(page, next){ //到达临界点（默认滚动触发），触发下一页
            var lis = [];
            //以jQuery的Ajax请求为例，请求下一页数据（注意：page是从2开始返回）
            $.get('/image/'+page, function(res){
                //假设你的列表返回在data集合中
                layui.each(res.list, function(index, item){

                    lis.push('<li>'+ item.title +'</li>');
                });

                //执行下一页渲染，第二参数为：满足“加载更多”的条件，即后面仍有分页
                //pages为Ajax返回的总页数，只有当前页小于总页数的情况下，才会继续出现加载更多
                next(lis.join(''), page < res.pages);
            });
        }
    });
});

