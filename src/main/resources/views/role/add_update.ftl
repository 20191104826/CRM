<!DOCTYPE html>
<html>
    <head>
        <#include "../common.ftl">
    </head>
    <body class="childrenBody">
        <form class="layui-form" style="width:80%;">
            <input name="id" type="hidden" value="${(role.id)!}"/>
            <div class="layui-form-item layui-row layui-col-xs12">
                <label class="layui-form-label">角色名称</label>
                <div class="layui-input-block">
                    <input type="text" class="layui-input roleName"
                           lay-verify="required" name="roleName" id="roleName"  value="${(role.roleName)!}" placeholder="请输入角色名称">
                </div>
            </div>
            <div class="layui-form-item layui-row layui-col-xs12">
                <label class="layui-form-label">角色备注</label>
                <div class="layui-input-block">
                    <input type="text" class="layui-input roleRemarker"
                           lay-verify="required" name="roleRemarker" id="roleRemarker" value="${(role.roleRemarker)!}" placeholder="请输入角色备注">
                </div>
            </div>

            <br/>
            <div class="layui-form-item layui-row layui-col-xs12">
                <div class="layui-input-block">
                    <button class="layui-btn layui-btn-lg" lay-submit=""
                            lay-filter="addOrUpdateRole">确认
                    </button>
                    <button class="layui-btn layui-btn-lg layui-btn-normal" id="closeBtn">取消</button>
                </div>
            </div>
        </form>

    <script type="text/javascript" src="${ctx}/js/role/add.update.js"></script>
    </body>
</html>