        $(function() {
            $("#vaccineRecordtable").jsGrid({
                height: "100%",
                width: "100%",
                filtering: false,
                editing: true,
                inserting: true,
                sorting: true,
                paging: true,
                autoload: true,
                pageSize: 15,
                pageButtonCount: 5,
                deleteConfirm: "Do you really want to delete the vaccineRecord?",
                controller:db,
                fields: [
                    { name: "user_ID", type: "text",title: "用户id",width: 75 ,editing:false,},
                    { name: "user_NAME", type: "text",title: "用户名", width: 75 ,editing:false,inserting:false},
                    { name: "vaccine_ID", type: "text",title: "疫苗id", width: 75 ,editing:false},
                    { name: "vaccine_name", type: "text",title: "疫苗名" ,editing:false,inserting:false},
                    { name: "animal_ID", type: "text",title: "动物id" ,editing:false},
                    { name: "animal_name", type: "text",title: "动物名" ,editing:false,inserting:false},
                    { name: "vaccination_time", type: "text",title: "接种时间" ,editing:false},
                    { name: "remarks", type: "text",title: "备注" },
                    { type: "control" }

                ]
            });

        });