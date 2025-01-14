layui.use(['form', 'layer','formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    var formSelects = layui.formSelects;


    /**
     * 监听表单submit事件
     */
    form.on('submit(addOrUpdateUser)',function (data) {
        //提交数据时的加载层
        var index = layer.msg("数据提交中，请稍后…", {
            icon: 16,     //图标
            time: false,  //不关闭
            shade: 0.8    //设置遮罩的透明度
        });

        //发送ajax请求
        var url = ctx + "/user/add";  //添加操作

        //判断ID是否为空
        if($("[name='id']").val()){
            //更新操作
            url = ctx + "/user/update";
        }
        $.post(url, data.field, function (result) {
            //判断操作是否执行成功，200=成功
            if (result.code == 200) {
                //成功
                //提示成功
                layer.msg("操作成功！", {icon: 6});
                //关闭加载层
                layer.close(index);
                //关闭弹出层
                layer.closeAll("iframe");
                //刷新父窗口，重新加载（渲染）数据
                parent.location.reload();
            } else {
                //失败
                layer.msg(result.msg, {icon: 5});
            }
        });
        //阻止表单提交
        return false;
    });

    //关闭弹出层
    $("#closeBtn").click(function () {
        //当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });

    /**
     * 加载角色下拉框
     *
     * 配置搜索远程， 请求头，请求参数，请求类型等
     *
     * formSelects.config(ID,Options,isJson)
     *   ID :  selectId
     *   Options : 配置项
     *   isJson : 是否传输json数据，true将添加请求头 Content-Type : application/json;charset=UTF-8;
     */

    var userId = $("[name='id']").val();
    formSelects.config('selectId',{
        type:"POST",  //请求方式
        searchUrl:ctx + "/role/queryAllRoles?userId="+userId,
        keyName:'roleName',   //下拉框中的文本内容，要与返回的数据中对应的key一致
        keyVal:'id'
    },true);

});