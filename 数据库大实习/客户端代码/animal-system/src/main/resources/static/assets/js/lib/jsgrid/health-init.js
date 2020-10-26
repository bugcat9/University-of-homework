        $(function() {
            $("#healthtable").jsGrid({
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
                deleteConfirm: "Do you really want to delete the health indormation?",
                controller:db,
                fields: [
                    { name: "animal_ID", type: "text",title: "动物id",width: 100 ,editing:false},
                    { name: "animal_name", type: "text",title: "动物名字", width: 150,editing:false,inserting: false },
                    { name: "user_ID", type: "text",title: "用户id", width: 200 ,editing:false},
                    { name: "user_name", type: "text",title: "用户名", width: 200 ,editing:false,inserting: false},
                    { name: "health_information", type: "text",title: "健康信息", width: 200 },
                    { name: "check_date", type: "text",title: "检查时间" ,editing:false},
                    { name: "remarks", type: "text",title: "备注" },
                    { type: "control" }

                ]
            });

        });