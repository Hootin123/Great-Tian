$(function () {
    //重写校验方法
    parent.DataGrid.prototype.onCheck = function () {

        var roleName = $('#jobName').val();
        if (isEmpty(roleName)) {
            layer.tips('请输入任务名', '#jobName', {time: 3000, tips: 1});
            return false;
        }

        var jobGroup = $('#jobGroup').val();
        if (isEmpty(jobGroup)) {
            layer.tips('请输入任务组', '#jobGroup', {time: 3000, tips: 1});
            return false;
        }

        var cronExpression = $('#cronExpression').val();
        if (isEmpty(cronExpression)) {
            layer.tips('请输入cron表达式', '#cronExpression', {time: 3000, tips: 1});
            return false;
        }

        //var beanClass = $('#beanClass').val();
        //if (isEmpty(beanClass)) {
        //    layer.tips('请输入类路径', '#beanClass', {time: 3000, tips: 1});
        //    return false;
        //}

        //var methodName = $('#methodName').val();
        //if (isEmpty(methodName)) {
        //    layer.tips('请输入方法名', '#methodName', {time: 3000, tips: 1});
        //    return false;
        //}

        var springId = $('#springId').val();
        if (isEmpty(springId)) {
            layer.tips('请输入springId', '#springId', {time: 3000, tips: 1});
            return false;
        }
        return true;
    };

});



