        $(function() {
            $("#vaccinetable").jsGrid({
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
                deleteConfirm: "Do you really want to delete the vaccine?",
                controller:db,
                fields: [
                    { name: "vaccine_ID", type: "text",title: "疫苗id",width: 100 ,editing:false},
                    { name: "vaccine_type", type: "text",title: "疫苗种类", width: 150 },
                    { name: "vaccine_name", type: "text",title: "疫苗名字", width: 200 },
                    { type: "control" }

                ]
            });

        });