        $(function() {

            $("#sheltertable").jsGrid({
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
                deleteConfirm: "Do you really want to delete the shelter?",
                controller: db,
                fields: [
                    { name: "shelter_id", type: "text",title: "收容所id",width: 100 ,editing:false},
                    { name: "shelter_name", type: "text",title: "收容所名字", width: 150 },
                    { name: "shelter_address", type: "text",title: "收容所地址", width: 200 },
                    { name: "postcode", type: "text",title: "收容所邮编" },
                    { name: "sum_rooms", type: "text",title: "总房间数" },
                    { name: "remain_rooms", type: "text",title: "剩余房间数" },
                    { name: "shelter_comment", type: "text",title: "备注" },
                    { type: "control" }
                ]
            });

        });