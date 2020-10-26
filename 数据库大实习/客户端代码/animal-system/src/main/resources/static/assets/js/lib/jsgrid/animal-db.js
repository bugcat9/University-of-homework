(function() {

    var db = {
        loadData: function(filter) {

            return $.grep(this.animals, function(animal) {
                return (!filter.animal_ID || animal.animal_id.indexOf(filter.animal_ID) > -1)
                    && (!filter.animal_name || animal.animal_name.indexOf(filter.animal_name) > -1)
                    && (!filter.animal_address || animal.animal_id.indexOf(filter.animal_address) > -1)
                    && (!filter.postcode || animal.animal_id.indexOf(filter.postcode) > -1)
                    && (!filter.sum_rooms || animal.animal_id.indexOf(filter.sum_rooms) > -1)
                    &&(!filter.remain_rooms || animal.animal_id.indexOf(filter.remain_rooms) > -1)
                    &&(!filter.animal_comment || animal.animal_comment.indexOf(filter.animal_comment) > -1);
            });
        },

        insertItem: function(insertinganimal) {
            console.log('insertItem也被运行')
            this.animals.push(insertinganimal);

            //和后端交互进行信息传递
            $.ajax({
                type:"POST",
                url:"/animal-insert",
                data: insertinganimal,
                async:false,
                success: function(data) {   // 这里的data就是json格式的数据
                    alert(data);
                    console.log(data)
                }

            })

        },

        updateItem: function(updatinganimal) {
            console.log('updateItem也被运行')
            console.log('这个被更新'+updatinganimal['animal_name'])
            //和后端交互进行信息传递
            $.ajax({
                type:"POST",
                url:"/animal-update",
                data: updatinganimal,
                async:false,
                success: function(data) {   // 这里的data就是json格式的数据
                    alert(data);
                    console.log(data)
                }
            })
        },

        deleteItem: function(deletinganimal) {
            console.log('deleteItem被运行')
            console.log('这个被删除'+deletinganimal['animal_name'])
            var animalIndex = $.inArray(deletinganimal, this.animals);
            this.animals.splice(animalIndex, 1);
            for (var i=0;i<this.animals.length;i++){
                console.log(this.animals[i]['animal_name'])
            }
            $.ajax({
                type:"POST",
                url:"/animal-delete",
                data: deletinganimal,
                async:false,
                success: function(data) {   // 这里的data就是json格式的数据
                    alert(data);
                    console.log(data)
                }
            })
        },

    };

    window.db = db;
    db.animals = [];   //数组

    $.ajax({
        type:"GET",
        url:"/animals",
        async:false,
        success: function(data) {   // 这里的data就是json格式的数据
            console.log("数据读取成功")
            console.log(data.type)
            for (var i=0;i<data.length;i++){
                // console.log(data[i])
              db.animals.push(data[i])
            }
        }

    })

    console.log(db.animals)

}());