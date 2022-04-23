layui.use(['table','layer'],function() {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;


    /**
     * 加载数据表格
     */

    var tableIns = table.render({
        id: 'saleChanceTable',
        //容器元素的ID属性值
        elem: '#saleChanceList', // 表格绑定的ID
        //访问数据的URL （对应后台的数据接口）
        url: ctx + '/sale_chance/list?flag=1', // 访问数据的地址   表示查询的是客户开发计划数据
        //单元格的最小宽度
        cellMinWidth: 95,
        // 开启分页
        page: true,
        //容器的高度  full-差值
        height: "full-125",
        //每页页数的可选项
        limits: [10, 15, 20, 25],
        // 默认每页显示的数量
        limit: 10,
        //开启头部工具栏
        toolbar: "#toolbarDemo",
        cols: [[
            // field:要求 field 属性值与返回的数据中对应的属性字段名一致
            // title:设置列的标题
            // sort:是否允许排序（默认：false）
            // fixed:固定列
            {type: "checkbox", fixed: "center"},
            {field: "id", title: '编号', fixed: "true"},
            {field: 'chanceSource', title: '机会来源', align: "center"},
            {field: 'customerName', title: '客户名称', align: 'center'},
            {field: 'cgjl', title: '成功几率', align: 'center'},
            {field: 'overview', title: '概要', align: 'center'},
            {field: 'linkMan', title: '联系人', align: 'center'},
            {field: 'linkPhone', title: '联系电话', align: 'center'},
            {field: 'description', title: '描述', align: 'center'},
            {field: 'createMan', title: '创建人', align: 'center'},
            {field: 'createDate', title: '创建时间', align: 'center'},
            {field: 'updateDate', title: '修改时间', align: 'center'},
            {
                field: 'devResult', title: '开发状态', align: 'center', templet: function (d) {
                    return formatterDevResult(d.devResult);
                }
            },
            {title: '操作', templet: '#op', fixed: "right", align: "center", minWidth: 150}
        ]]
    });

    /**
     * 格式化开发状态
     *  0 - 未开发
     *  1 - 开发中
     *  2 - 开发成功
     *  3 - 开发失败
     *  其他 - 未知
     * @param value
     * @returns {string}
     */
    function formatterDevResult(value) {
        if (value == 0) {
            return "<div style='color: yellow'>未开发</div>";
        } else if (value == 1) {
            return "<div style='color: #00FF00;'>开发中</div>";
        } else if (value == 2) {
            return "<div style='color: #00B83F'>开发成功</div>";
        } else if (value == 3) {
            return "<div style='color: red'>开发失败</div>";
        } else {
            return "<div style='color: #af0000'>未知</div>"
        }
    }

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
                customerName: $("[name='customerName']").val()   //客户名称
                , createMan: $("[name='createMan']").val()        //创建人
                , devResult: $("#devResult").val()                     //状态
            }
            , page: {
                curr: 1   //重新从第一页开始
            }
        });
    });

    /**
     * 行工具栏监听事件
     */
    table.on('tool(saleChances)',function (data) {
        //判断类型
        if(data.event == "dev"){  //开发

            //打开计划项开发与详情页面
            openCusDevPlanDialog('计划项数据开发',data.data.id);

        }else if(data.event == "info"){  //详情

            //打开计划项开发与详情页面
            openCusDevPlanDialog('计划项数据维护',data.data.id);

        }
    });

    /**
     * 打开计划项开发或者详情页面
     *
     * @param title
     * @param id
     */
    function openCusDevPlanDialog(title,id){
        //iframe 层
        layui.layer.open({
            //类型
            type: 2,
            //标题
            title: title,
            //开启最大化最小化按钮
            maxmin: true,
            //宽高
            area: ['750px', '550px'],
            //url地址
            content: ctx + "/cus_dev_plan/toCusDevPlanPage?id="+id
        });
    }

});
