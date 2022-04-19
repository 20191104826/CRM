layui.use(['table', 'layer'], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 加载数据表格
     */
    // table.render({
    //     //容器元素的ID属性值
    //     elem: '#saleChanceList'
    //     //容器的高度  full-差值
    //     , height: 'full-125'
    //     //单元格的最小宽度
    //     , cellMinWidth: 95
    //     //访问数据的URL （对应后台的数据接口）
    //     , url: ctx + '/sale_chance/list' //数据接口
    //     // 开启分页
    //     , page: true
    //     // 默认每页显示的数量
    //     , limit: 10
    //     //每页页数的可选项
    //     , limits: [10, 20, 30, 40, 50]
    //     //开启头部工具栏
    //     , toolbar: '#toolbarDemo'
    //     // 表头
    //     , cols: [[
    //         // field:要求 field 属性值与返回的数据中对应的属性字段名一致
    //         // title:设置列的标题
    //         // sort:是否允许排序（默认：false）
    //         // fixed:固定列
    //         {type: 'checkbox', fixed: 'center'}
    //         , {field: "id", title: '编号', sort: true, fixed: "left"}
    //         , {field: 'chanceSource', title: '机会来源', align: 'center'}
    //         , {field: 'customerName', title: '客户名称', align: 'center'}
    //         , {field: 'cgil', title: '成功几率', align: 'center'}
    //         , {field: 'overview', title: '概要', align: 'center'}
    //         , {field: 'linkMan', title: '联系人', align: 'center'}
    //         , {field: 'linkPhone', title: '联系电话', align: 'center'}
    //         , {field: 'description', title: '描述', align: 'center'}
    //         , {field: 'createMan', title: '创建人', align: 'center'}
    //         , {field: 'assignMan', title: '分配人', align: 'center'}
    //         , {field: 'assignTime', title: '分配时间', align: 'center'}
    //         , {field: 'createDate', title: '创建时间', align: 'center'}
    //         , {field: 'devResult', title: '开发状态', align: 'center'}
    //         , {field: 'isState', title: '分配状态', align: 'center'}
    //         , {title: '操作', templet: '#saleChanceListBar', fixed: 'right', align: 'center', minWidth: 150}
    //     ]]
    // });

    /**
     * 营销机会列表展示
     */
    var tableIns = table.render({
        elem: '#saleChanceList', // 表格绑定的ID
        url: ctx + '/sale_chance/list', // 访问数据的地址
        cellMinWidth: 95,
        page: true, // 开启分页
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "saleChanceListTable",
        cols: [[
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
            {field: 'uname', title: '指派人', align: 'center'},
            {field: 'assignTime', title: '分配时间', align: 'center'},
            {
                field: 'isState', title: '分配状态', align: 'center', templet: function (d) {
                    return formatterIsState(d.isState);
                }
            },
            {
                field: 'devResult', title: '开发状态', align: 'center', templet: function (d) {
                    return formatterDevResult(d.devResult);
                }
            },
            {title: '操作', templet: '#saleChanceListBar', fixed: "right", align: "center", minWidth: 150}
        ]]
    });

    /**
     * 格式化分配状态
     *  0 - 未分配
     *  1 - 已分配
     *  其他 - 未知
     * @param isState
     * @returns {string}
     */
    function formatterIsState(isState) {
        if (isState == 0) {
            return "<div style='color: yellow'>未分配</div>";
        } else if (isState == 1) {
            return "<div style='color: green'>已分配</div>";
        } else {
            return "<div style='color: red'>未知</div>";
        }
    }

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
});
