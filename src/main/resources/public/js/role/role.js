layui.use(['table', 'layer'], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    /**
     * 加载数据
     */
    var tableIns = table.render({
        elem: '#roleList',
        url: ctx + '/role/list',
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "roleTable",
        cols: [[
            {type: "checkbox", fixed: "left", width: 50},
            {field: "id", title: '编号', fixed: "true", width: 80},
            {field: 'roleName', title: '角色名称', minWidth: 50, align: "center"},
            {field: 'roleRemarker', title: '角色备注', minWidth: 100, align: 'center'},
            {field: 'createDate', title: '创建时间', align: 'center', minWidth: 150},
            {field: 'updateDate', title: '更新时间', align: 'center', minWidth: 150},
            {title: '操作', minWidth: 150, templet: '#roleListBar', fixed: "right", align: "center"}
        ]]
    });

    /**
     * 搜索按钮的点击事件
     */
    $(".search_btn").click(function () {

        /**
         * 表格重载，多条件查询
         */
        tableIns.reload({
            //设置需要传递给后端的数据
            where: {  //设置异步数据接口的额外参数
                //通过文本框或者下拉框的值，设置传递的参数
                roleName: $("[name='roleName']").val()   //客户名称
            }
            , page: {
                curr: 1   //重新从第一页开始
            }
        });
    });

    /**
     * 监听头部工具栏
     */
    table.on('toolbar(roles)', function (data) {
        if (data.event == "add") {  //添加角色

            //打开添加/修改角色的页面
            openAddOrUpdateRoleDialog();

        } else if (data.event == "del") {  //删除角色
            //获取被选中的数据信息
            var checkStatus = table.checkStatus(data.config.id);

            //删除多个角色记录
            deleteUsers(checkStatus.data);
        }
    });

    /**
     * 删除多条用户记录
     */
    function deleteUsers(userData) {

        //判断用户是否选择了记录，（选中行大于 0 ）
        if (userData.length < 1) {
            layer.msg("请选择要删除的记录！", {icon: 5});
            return;
        }

        //询问用户是否确认删除记录
        layer.confirm('您确定要删除选中的记录吗？', {icon: 3, title: '用户管理'}, function (index) {
            //关闭确认框
            layer.close(index);
            //传递的参数是数组,   ids=1&ids=2$ids=3
            var ids = "ids=";
            //循环选中的行记录的数据
            for (var i = 0; i < userData.length; i++) {
                if (i < userData.length - 1) {
                    ids = ids + userData[i].id + "&ids=";
                } else {
                    ids = ids + userData[i].id;
                }
            }
            console.log(ids);
            //发送Ajax请求，执行删除用户
            $.ajax({
                type: "POST",
                url: ctx + "/user/delete",
                data: ids,
                success: function (result) {
                    //判断删除结果
                    if (result.code == 200) {
                        //提示成功
                        layer.msg("删除成功！", {icon: 6});
                        //刷新表格
                        tableIns.reload();
                    } else {
                        //提示失败
                        layer.msg(result.msg, {icon: 5});
                    }
                }
            });
        });
    }

    /**
     * 监听行工具栏
     */
    table.on('tool(roles)', function (data) {
        if (data.event == "edit") {  //更新角色

            //打开添加/修改角色的对话框
            openAddOrUpdateRoleDialog(data.data.id);

        } else if (data.event == "del") {  //删除角色
            deleteRole(data.data.id);
        }
    });

    /**
     * 删除单条角色记录
     * @param id
     */
    function deleteRole(id) {
        //弹出确认框，询问用户是否确认删除
        layer.confirm('确认要删除该记录吗？', {icon: 3, title: "角色管理"}, function (index) {
            //关闭确认框
            layer.close(index);

            //发送ajax请求，删除记录
            $.ajax({
                type: "POST",
                url: ctx + "/role/delete",
                data: {
                    ids:id
                },
                success: function (result) {
                    //判断删除结果
                    if (result.code == 200) {
                        //提示成功
                        layer.msg("删除成功！", {icon: 6});
                        //刷新表格
                        tableIns.reload();
                    } else {
                        //提示失败
                        layer.msg(result.msg, {icon: 5});
                    }
                }
            });
        });

    }

    /**
     * 打开添加/修改用户的对话框
     */
    function openAddOrUpdateRoleDialog(id) {
        var title = "<h3>角色管理 - 添加角色</h3>"
        var url = ctx + "/role/toAddOrUpdateRolePage";
        if (id != null && id != '') {
            url += "?id=" + id;   //传递主键，查询数据
            title = "<h3>角色管理 - 更新角色</h3>"
        }

        //iframe 层
        layui.layer.open({
            //类型
            type: 2,
            //标题
            title: title,
            //开启最大化最小化按钮
            maxmin: true,
            //宽高
            area: ['500px', '400px'],
            //url地址
            content: url
        });
    }
});