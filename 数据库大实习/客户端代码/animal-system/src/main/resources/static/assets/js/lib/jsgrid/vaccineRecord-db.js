(function() {

    var db = {
        loadData: function(filter) {

            return $.grep(this.vaccineRecords, function(vaccineRecord) {
                return (!filter.vaccineRecord_ID || vaccineRecord.vaccineRecord_id.indexOf(filter.vaccineRecord_ID) > -1)
                    && (!filter.vaccineRecord_name || vaccineRecord.vaccineRecord_name.indexOf(filter.vaccineRecord_name) > -1)
                    && (!filter.vaccineRecord_address || vaccineRecord.vaccineRecord_id.indexOf(filter.vaccineRecord_address) > -1)
                    && (!filter.postcode || vaccineRecord.vaccineRecord_id.indexOf(filter.postcode) > -1)
                    && (!filter.sum_rooms || vaccineRecord.vaccineRecord_id.indexOf(filter.sum_rooms) > -1)
                    &&(!filter.remain_rooms || vaccineRecord.vaccineRecord_id.indexOf(filter.remain_rooms) > -1)
                    &&(!filter.vaccineRecord_comment || vaccineRecord.vaccineRecord_comment.indexOf(filter.vaccineRecord_comment) > -1);
            });
        },

        insertItem: function(insertingvaccineRecord) {
            console.log('insertItem也被运行')
            this.vaccineRecords.push(insertingvaccineRecord);

            //和后端交互进行信息传递
            $.ajax({
                type:"POST",
                url:"/vaccineRecord-insert",
                data: insertingvaccineRecord,
                async:false,
                success: function(data) {   // 这里的data就是json格式的数据
                    alert(data);
                    console.log(data)
                }

            })

        },

        updateItem: function(updatingvaccineRecord) {
            console.log('updateItem也被运行')
            console.log('这个被更新'+updatingvaccineRecord['vaccineRecord_name'])
            //和后端交互进行信息传递
            $.ajax({
                type:"POST",
                url:"/vaccineRecord-update",
                data: updatingvaccineRecord,
                async:false,
                success: function(data) {   // 这里的data就是json格式的数据
                    alert(data);
                    console.log(data)
                }
            })
        },

        deleteItem: function(deletingvaccineRecord) {
            console.log('deleteItem被运行')
            console.log('这个被删除'+deletingvaccineRecord['Remarks'])
            var vaccineRecordIndex = $.inArray(deletingvaccineRecord, this.vaccineRecords);
            this.vaccineRecords.splice(vaccineRecordIndex, 1);
            for (var i=0;i<this.vaccineRecords.length;i++){
                console.log(this.vaccineRecords[i]['Remarks'])
            }
            $.ajax({
                type:"POST",
                url:"/vaccineRecord-delete",
                data: deletingvaccineRecord,
                async:false,
                success: function(data) {   // 这里的data就是json格式的数据
                    alert(data);
                    console.log(data)
                }
            })
        },
    };

    window.db = db;
    db.vaccineRecords = [];   //数组

    $.ajax({
        type:"GET",
        url:"/vaccineRecords",
        async:false,
        success: function(data) {   // 这里的data就是json格式的数据
            //console.log("数据读取成功")
            //console.log(data.length)
            for (var i=0;i<data.length;i++){
                // console.log(data[i])
              db.vaccineRecords.push(data[i])
            }
        }

    })

    console.log(db.vaccineRecords)

}());