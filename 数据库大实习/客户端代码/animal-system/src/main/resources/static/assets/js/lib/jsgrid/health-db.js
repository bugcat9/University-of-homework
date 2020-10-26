(function() {

    var db = {
        loadData: function(filter) {

            return $.grep(this.healths, function(health) {
                return (!filter.health_ID || health.health_id.indexOf(filter.health_ID) > -1)
                    && (!filter.health_name || health.health_name.indexOf(filter.health_name) > -1)
                    && (!filter.health_address || health.health_id.indexOf(filter.health_address) > -1)
                    && (!filter.postcode || health.health_id.indexOf(filter.postcode) > -1)
                    && (!filter.sum_rooms || health.health_id.indexOf(filter.sum_rooms) > -1)
                    &&(!filter.remain_rooms || health.health_id.indexOf(filter.remain_rooms) > -1)
                    &&(!filter.health_comment || health.health_comment.indexOf(filter.health_comment) > -1);
            });
        },

        insertItem: function(insertinghealth) {
            console.log('insertItem也被运行')
            this.healths.push(insertinghealth);

            //和后端交互进行信息传递
            $.ajax({
                type:"POST",
                url:"/health-insert",
                data: insertinghealth,
                async:false,
                success: function(data) {   // 这里的data就是json格式的数据
                    alert(data);
                    console.log(data)
                }

            })

        },

        updateItem: function(updatinghealth) {
            console.log('updateItem也被运行')
            console.log('这个被更新'+updatinghealth['health_information'])
            //和后端交互进行信息传递
            $.ajax({
                type:"POST",
                url:"/health-update",
                data: updatinghealth,
                async:false,
                success: function(data) {   // 这里的data就是json格式的数据
                    alert(data);
                    console.log(data)
                }
            })
        },

        deleteItem: function(deletinghealth) {
            console.log('deleteItem被运行')
            console.log('这个被删除'+deletinghealth['health_name'])
            var healthIndex = $.inArray(deletinghealth, this.healths);
            this.healths.splice(healthIndex, 1);
            for (var i=0;i<this.healths.length;i++){
                console.log(this.healths[i]['health_information'])
            }
            $.ajax({
                type:"POST",
                url:"/health-delete",
                data: deletinghealth,
                async:false,
                success: function(data) {   // 这里的data就是json格式的数据
                    alert(data);
                    console.log(data)
                }
            })
        },

    };

    window.db = db;
    db.healths = [];   //数组

    $.ajax({
        type:"GET",
        url:"/all_health",
        async:false,
        success: function(data) {   // 这里的data就是json格式的数据
            console.log("数据读取成功")
            console.log(data.type)
            for (var i=0;i<data.length;i++){
                // console.log(data[i])
              db.healths.push(data[i])
            }
        }

    })

    console.log(db.healths)

}());