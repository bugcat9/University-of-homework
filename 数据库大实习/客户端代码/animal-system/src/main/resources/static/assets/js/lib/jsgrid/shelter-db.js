(function() {

    var db = {
        loadData: function(filter) {

            return $.grep(this.shelters, function(shelter) {
                return (!filter.shelter_id || shelter.shelter_id.indexOf(filter.shelter_id) > -1)
                    && (!filter.shelter_name || shelter.shelter_name.indexOf(filter.shelter_name) > -1)
                    && (!filter.shelter_address || shelter.shelter_id.indexOf(filter.shelter_address) > -1)
                    && (!filter.postcode || shelter.shelter_id.indexOf(filter.postcode) > -1)
                    && (!filter.sum_rooms || shelter.shelter_id.indexOf(filter.sum_rooms) > -1)
                    &&(!filter.remain_rooms || shelter.shelter_id.indexOf(filter.remain_rooms) > -1)
                    &&(!filter.shelter_comment || shelter.shelter_comment.indexOf(filter.shelter_comment) > -1);
            });
        },

        insertItem: function(insertingshelter) {
            console.log('insertItem也被运行')
            this.shelters.push(insertingshelter);
            console.log(this.shelters.type)

            //和后端交互进行信息传递
            $.ajax({
                type:"POST",
                url:"/shelter-insert",
                data: insertingshelter,
                async:false,
                success: function(data) {   // 这里的data就是json格式的数据
                    alert(data);
                    console.log(data)
                }

            })

        },

        updateItem: function(updatingshelter) {
            console.log('updateItem也被运行')
            console.log('这个被更新'+updatingshelter['shelter_name'])
            $.ajax({
                type:"POST",
                url:"/shelter-update",
                data: updatingshelter,
                async:false,
                success: function(data) {   // 这里的data就是json格式的数据
                    alert(data);
                    console.log(data)
                }
            })
        },

        deleteItem: function(deletingshelter) {
            console.log('deleteItem被运行')
            console.log('这个被删除'+deletingshelter['shelter_name'])
            var shelterIndex = $.inArray(deletingshelter, this.shelters);
            this.shelters.splice(shelterIndex, 1);
            $.ajax({
                type:"POST",
                url:"/shelter-delete",
                data: deletingshelter,
                async:false,
                success: function(data) {   // 这里的data就是json格式的数据
                    alert(data);
                    console.log(data)
                }
            })

            for (var i=0;i<this.shelters.length;i++){
                console.log(this.shelters[i]['shelter_name'])
            }
        },

    };

    window.db = db;
    db.shelters = [];   //数组

    $.ajax({
        type:"GET",
        url:"/shelters",
        async:false,
        success: function(data) {   // 这里的data就是json格式的数据
            console.log("数据读取成功")
            console.log(data.type)
            for (var i=0;i<data.length;i++){
                console.log(data[i])
              db.shelters.push(data[i])
            }
        }

    })

    console.log(db.shelters)

}());