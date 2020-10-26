一  初次配置数据库大致步骤说明:

1)创建表空间和用户
  需要修改的东西有 先修改initialize.bat文件中的system用户的密码和数据库sid
  然后修改.\spaceanduser\ 目录下的createtablespace.sql文件中的表空间
数据文件的目录,根据数据库创建的磁盘来更改;
  createuser.sql文件中的用户名和密码写为了zn/zn19980921可以选者修改
2)执行BAT文件 "sql\Run_InitDB.bat"
该脚本的功能:完成数据库表格
该脚本执行完毕后会在当前文件夹下生成一个日志文件"Result.log",记录脚本执行结果

二 批处理文件的说明
1)Run_InitDB.bat
该批处理文件在初次初始化数据库时使用
功能:完成数据库表格,序列,函数,视图,存储过程,触发器的创建,并完成基础数据的初始化
该文件的执行将会对数据库中已有数据造成影响,请谨慎使用
2)Run_InitProc.bat
该批处理文件用于初始化视图,函数,存储过程,触发器
该文件的执行不会对数据库中原有数据造成影响
3)使用方法
Run_InitDB.bat,Run_InitProc.bat这两个BAT文件在使用之前需要按实际情况作一些修改
修改方法:用记事本打开BAT文件,找到字符串"sqlplus username/passwd@servername"
将其按实际情况修改,如:"sqlplus zn/zn19980921"

