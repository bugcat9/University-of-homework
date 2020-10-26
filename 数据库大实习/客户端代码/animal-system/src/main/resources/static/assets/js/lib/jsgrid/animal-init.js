        $(function() {
            $("#animaltable").jsGrid({
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
                deleteConfirm: "Do you really want to delete the animal?",
                controller:db,
                fields: [
                    { name: "animal_ID", type: "text",title: "动物id",width: 100 ,editing:false},
                    { name: "animal_name", type: "text",title: "动物名字", width: 150 },
                    { name: "animal_species", type: "text",title: "动物种类", width: 200 },
                    { name: "animal_age", type: "text",title: "动物年龄" },
                    { name: "shelter_ID", type: "text",title: "收容所id" },
                    { type: "control" }

                ]
            });

        });