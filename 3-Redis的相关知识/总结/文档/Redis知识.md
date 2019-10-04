# Redis相关知识

## 一.Redis 数据类型

*   String（字符类型）
*   Hash（散列类型）
*   List（列表类型）
*   Set（集合类型）
*   SortedSet（有序集合类型）

#### 一.String 类型
*   赋值：Set key value
*   取值：get key
*   取值并赋值：getset key value
*   递增数字：incr key
*   增加指定的整数：incrby key value
*   递减数字：decr key
*   减少指定的整数：decrby key value
*   仅当不存在时赋值
    *   exists job # job 不存在
    *   setnx job “programmer”  # job 设置成功
    *   SETNX job "code-farmer"  # 尝试覆盖 job ，失败
    *   GET job  # 没有被覆盖，programmer
*   向尾部追加值：**APPEND key value**
    *    set str hello
    *   append str " world!" ===> 现在str的值是 “hello world”
*   获取字符串长度：STRLEN key
*   同时设置/获取多个键值
    *   mset k1 v1 k2 v2 k3 v3 ===> { k1:v1, k2:v2, k3:v3 }
*   自增主键，实际案例
    *   INCR items:id ==> 2
    *   INCR items:id ==> 3

####  二. Hash 类型

<img src="/Users/hptg/Documents/Project/Spring/Java-Architecture-Master/3-Redis的相关知识/总结/文档/图片/Redis1.png" width=60% align=left />

*   赋值：**HSET key field value**
    *   hset user username zhangsan
*   赋多个值：**HMSET key field value [field value ...]**
    *   hmset user age 20 username lisi
*   取值：**HMGET key field [field ...]**
    *   hmget user age username ==> 20 和 lisi
*   获取所有字段值：**HGETALL key**
*   删除字段：**HDEL key field [field ...]**
    *   hdel user age
    *   hdel user age name
    *   hdel user age username
*   增加数字：**HINCRBY key field increment**
    *   hincrby user age 2    将用户的年龄加2

#### 三. List类型

*   向列表左边增加元素：**LPUSH key value [value ...]**
    *    lpush list:1 1 2 3
*   向列表右边增加元素：**RPUSH key value [value ...]**
    *   rpush list:1 4 5 6
*   

