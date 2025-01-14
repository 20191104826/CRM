layui.use(['form', 'layer','formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    var formSelects = layui.formSelects;


    /**
     * 监听表单submit事件
     */
    form.on('submit(addOrUpdateRole)',function (data) {
        //提交数据时的加载层
        var index = layer.msg("数据提交中，请稍后…", {
            icon: 16,     //图标
            time: false,  //不关闭
            shade: 0.8    //设置遮罩的透明度
        });

        //得到所有表单元素的值
        var formData = data.field;

        //发送ajax请求
        var url = ctx + "/role/add";  //添加操作

        // //判断ID是否为空
        // if($("[name='id']").val()){
        //     //更新操作
        //     url = ctx + "/user/update";
        // }
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

});