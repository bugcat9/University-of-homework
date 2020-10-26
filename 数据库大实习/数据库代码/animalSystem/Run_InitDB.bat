ECHO on
ECHO 正在执行SQL脚本(创建表格,序列,视图,函数,存储过程,初始化数据)......
sqlplus zn/zn19980921 @.\Batch\Batch_InitDB.sql  >>.\Result.log
EXIT


