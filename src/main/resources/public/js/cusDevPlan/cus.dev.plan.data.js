layui.use(['table', 'layer'], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 计划项数据展示
     */
    var tableIns = table.render({
        elem: '#cusDevPlanList',
        url: ctx + '/cus_dev_plan/list?saleChanceId=' + $("input[name='id']").val(),
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "cusDevPlanTable",
        cols: [[
            {type: "checkbox", fixed: "center"},
            {field: "id", title: '编号', fixed: "true"},
            {field: 'planItem', title: '计划项', align: "center"},
            {field: 'exeAffect', title: '执行效果', align: "center"},
            {field: 'planDate', title: '执行时间', align: "center"},
            {field: 'createDate', title: '创建时间', align: "center"},
            {field: 'updateDate', title: '更新时间', align: "center"},
            {title: '操作', fixed: "right", align: "center", minWidth: 150, templet: "#cusDevPlanListBar"}
        ]]
    });

    /**
     * 监听头部工具栏
     */
    table.on('toolbar(cusDevPlans)', function (data) {
        if (data.event == "add") {  //添加计划项
            //打开添加或修改计划项的页面
            openAddOrUpdateCusDevPlanDialog();
        } else if (data.event == "success") {  //开发成功

        } else if (data.event == "failed") {   //开发失败

        }
    });

    /**
     * 监听行工具栏
     */
    table.on("tool(cusDevPlans)", function (data) {
        if (data.event == "edit") {  //更新计划项

            //打开添加或修改计划项的页面
            openAddOrUpdateCusDevPlanDialog(data.data.id);
        } else if (data.event == "del") {

        }

    });


    /**
     * 打开添加或修改计划项的页面
     */
    function openAddOrUpdateCusDevPlanDialog(id) {
        var title = "计划项管理 - 添加计划项";
        var url = ctx + "/cus_dev_plan/toAddOrUpdateCusDevPlanPage?sId=" + $("[name='id']").val();
        //判断计划项ID是否为空，（为空：添加操作；不为空，则表示更新操作）
        if(id != null && id != ''){
            title = "计划项管理 - 更新计划项";
            url += "&id="+id;
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
            area: ['500px', '300px'],
            //url地址
            content: url
        });
    }

});
