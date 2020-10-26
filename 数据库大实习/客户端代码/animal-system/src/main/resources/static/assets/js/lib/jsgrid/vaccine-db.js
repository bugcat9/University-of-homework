(function() {

    var db = {
        loadData: function(filter) {

            return $.grep(this.vaccines, function(vaccine) {
                return (!filter.vaccine_ID || vaccine.vaccine_id.indexOf(filter.vaccine_ID) > -1)
                    && (!filter.vaccine_name || vaccine.vaccine_name.indexOf(filter.vaccine_name) > -1)
                    && (!filter.vaccine_address || vaccine.vaccine_id.indexOf(filter.vaccine_address) > -1)
                    && (!filter.postcode || vaccine.vaccine_id.indexOf(filter.postcode) > -1)
                    && (!filter.sum_rooms || vaccine.vaccine_id.indexOf(filter.sum_rooms) > -1)
                    &&(!filter.remain_rooms || vaccine.vaccine_id.indexOf(filter.remain_rooms) > -1)
                    &&(!filter.vaccine_comment || vaccine.vaccine_comment.indexOf(filter.vaccine_comment) > -1);
            });
        },

        insertItem: function(insertingvaccine) {
            console.log('insertItem也被运行')
            this.vaccines.push(insertingvaccine);

            //和后端交互进行信息传递
            $.ajax({
                type:"POST",
                url:"/vaccine-insert",
                data: insertingvaccine,
                async:false,
                success: function(data) {   // 这里的data就是json格式的数据
                    alert(data);
                    console.log(data)
                }

            })

        },

        updateItem: function(updatingvaccine) {
            console.log('updateItem也被运行')
            console.log('这个被更新'+updatingvaccine['vaccine_name'])
            //和后端交互进行信息传递
            $.ajax({
                type:"POST",
                url:"/vaccine-update",
                data: updatingvaccine,
                async:false,
                success: function(data) {   // 这里的data就是json格式的数据
                    alert(data);
                    console.log(data)
                }
            })
        },

        deleteItem: function(deletingvaccine) {
            console.log('deleteItem被运行')
            console.log('这个被删除'+deletingvaccine['vaccine_name'])
            var vaccineIndex = $.inArray(deletingvaccine, this.vaccines);
            this.vaccines.splice(vaccineIndex, 1);
            for (var i=0;i<this.vaccines.length;i++){
                console.log(this.vaccines[i]['vaccine_name'])
            }
            $.ajax({
                type:"POST",
                url:"/vaccine-delete",
                data: deletingvaccine,
                async:false,
                success: function(data) {   // 这里的data就是json格式的数据
                    alert(data);
                    console.log(data)
                }
            })
        },

    };

    window.db = db;
    db.vaccines = [];   //数组

    $.ajax({
        type:"GET",
        url:"/vaccines",
        async:false,
        success: function(data) {   // 这里的data就是json格式的数据
            console.log("数据读取成功")
            console.log(data.type)
            for (var i=0;i<data.length;i++){
                // console.log(data[i])
              db.vaccines.push(data[i])
            }
        }
    })

    console.log(db.vaccines)

}());