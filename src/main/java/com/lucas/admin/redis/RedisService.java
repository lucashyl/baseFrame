package com.lucas.admin.redis;

import com.lucas.admin.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hyl on 2019/3/1.
 */
@Component("RedisService")
public class RedisService {

    /**
     * Jedis对象池 所有Jedis对象均通过该池租赁获取 哨兵模式
     */
//    @Autowired
//    private static JedisSentinelPool sentinelPool;

    /**
     * 注入JedisSentinelPool 哨兵模式
     */
//    @Autowired
//    public  void setSentinelPool(JedisSentinelPool sentinelPool) {
//        RedisService.sentinelPool = sentinelPool;
//    }

    /**
     * 单机版
     */
    @Autowired
    private static JedisPool jedisPool;

    /**
     * JedisPool 单机版
     */
    @Autowired
    public void setJedisPool(JedisPool jedisPool) {
        RedisService.jedisPool = jedisPool;
    }

    /**
     * 缓存数据成功
     */
    private final static String CACHE_INFO_SUCCESS = "OK";


    /**
     * 获取到Jedis
     */
    private static Jedis getJedis() {
        Jedis jedis;
        try {
//            jedis = sentinelPool.getResource();
            jedis = jedisPool.getResource();
            return jedis;
        } catch (JedisConnectionException e) {
            throw e;
        }
    }

    /**
     * 更改缓存时间
     *
     * @param key
     * @param timeout
     * @return
     */
    public boolean setExpire(String key, int timeout) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (timeout != -1) {
                jedis.expire(key, timeout);
            }
            return true;
        } finally {
            releaseRedis(jedis);
        }
    }

    /**
     * 更改缓存时间
     *
     * @param key
     * @param timeout 毫秒
     * @return
     */
    public boolean setPexpire(String key, long timeout) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (timeout != -1) {
                jedis.pexpire(key,timeout);
            }
            return true;
        } finally {
            releaseRedis(jedis);
        }
    }

    /**
     * 缓存String类型的数据并设置超时时间 时间秒
     */
    public boolean set(String key, String value, int timeout) {
        String result;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.set(key, objectSerialiable(value));
            if (timeout != -1) {
                jedis.expire(key, timeout);
            }
            if (CACHE_INFO_SUCCESS.equals(result)) {
                return true;
            } else {
                return false;
            }
        } finally {
            releaseRedis(jedis);
        }
    }

    /**
     *
     * @param key
     * @param value
     * @param timeout
     * @param db  指定数据库 从0开始
     * @return
     */
    public boolean setDB(String key, String value, int timeout,int db) {
        String result;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.select(db);
            result = jedis.set(key, objectSerialiable(value));
            if (timeout != -1) {
                jedis.expire(key, timeout);
            }
            if (CACHE_INFO_SUCCESS.equals(result)) {
                return true;
            } else {
                return false;
            }
        } finally {
            releaseRedis(jedis);
        }
    }

    /**
     * 获取String类型的数据
     */
    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            String strKey = jedis.get(key);
            if (StringUtils.isBlank(strKey)) {
                return "";
            }
            return objectDeserialization(strKey).toString();
        } catch (Exception e) {
            throw e;
        } finally {
            releaseRedis(jedis);
        }
    }

    /**
     * 获取对应的库
     * @param key
     * @param db 指定数据库 从0开始
     * @return
     */
    public String getDB(String key,int db) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.select(db);
            String strKey = jedis.get(key);
            if (StringUtils.isBlank(strKey)) {
                return "";
            }
            return objectDeserialization(strKey).toString();
        } catch (Exception e) {
            throw e;
        } finally {
            releaseRedis(jedis);
        }
    }

    /**
     * 获取模糊key
     *
     * @param patternKeys
     * @return
     */
    public Set<String> keys(String patternKeys) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            Set<String> set = jedis.keys(patternKeys);
            return set;
        } catch (Exception e) {
            throw e;
        } finally {
            releaseRedis(jedis);
        }
    }

    /**
     * 模糊匹配 对应的DB
     * @param patternKeys
     * @param db
     * @return
     */
    public Set<String> keysDB(String patternKeys,int db) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.select(db);
            Set<String> set = jedis.keys(patternKeys);
            return set;
        } catch (Exception e) {
            throw e;
        } finally {
            releaseRedis(jedis);
        }
    }

    /**
     * key 中储存的数字值增value
     *
     * @param key
     * @param value 增加值
     * @return
     */
    public long incrby(String key, int value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.incrBy(key, value);
        } finally {
            releaseRedis(jedis);
        }
    }

    /**
     * 释放Jedis
     */
    public static void releaseRedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }


    /**
     * 通过key删除缓存中数据
     */
    public boolean del(String key) {
        Long num;
        Jedis jedis = null;
        Boolean result = false;
        try {
            jedis = getJedis();
            num = jedis.del(key);
            if (num.equals(1L)) {
                result = true;
            }
        } finally {
            releaseRedis(jedis);
        }
        return result;
    }

    public boolean delDB(String key,Integer db) {
        Long num;
        Jedis jedis = null;
        Boolean result = false;
        try {
            jedis = getJedis();
            jedis.select(db);
            num = jedis.del(key);
            if (num.equals(1L)) {
                result = true;
            }
        } finally {
            releaseRedis(jedis);
        }
        return result;
    }

    /**
     * 通过key删除缓存中数据
     */
    public boolean del(String[] key) {
        Long num;
        Jedis jedis = null;
        Boolean result = false;
        try {
            jedis = getJedis();
            num = jedis.del(key);
            if (num.equals(1L)) {
                result = true;
            }
        } finally {
            releaseRedis(jedis);
        }
        return result;
    }

    /**
     * 获取过期时间
     */
    public Long ttl(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            Long ttl = jedis.ttl(key);
            return ttl;
        } catch (Exception e) {
            throw e;
        } finally {
            releaseRedis(jedis);
        }
    }


    /**
     * 判断是否存在key
     *
     * @param key
     * @return
     */
    public static boolean hasKey(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.exists(key);
        } catch (Exception e) {
            throw e;
        } finally {
            releaseRedis(jedis);
        }
    }

    /**
     * 缓存Map赋值
     *
     * @param key
     * @param field map 的key
     * @param value map 的value
     * @return -5：Jedis实例获取失败
     */
    public static boolean hset(String key, String field, String value, int timeout) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.hset(key, field, value);
            if (timeout != -1) {
                jedis.expire(key, timeout);
            }
            return true;
        } catch (Exception e) {
            throw e;
        } finally {
            releaseRedis(jedis);
        }
    }


    /**
     * 获取缓存Map指定key的值
     *
     * @param key
     * @param field map 的key
     */
    public static Object hget(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            Object o = jedis.hget(key, field);
            return o;
        } catch (Exception e) {
            throw e;
        } finally {
            releaseRedis(jedis);
        }
    }

    /**
     * 获取map所有的字段和值
     *
     * @param key
     * @return
     */
    public static Map<String, String> hgetAll(String key) {
        Map<String, String> map = new HashMap<String, String>();
        Jedis jedis = null;
        try {
            jedis = getJedis();
            map = jedis.hgetAll(key);
        } catch (Exception e) {
            throw e;
        } finally {
            releaseRedis(jedis);
        }
        return map;
    }

    /**
     * 设置对象 时间秒
     *
     * @param key
     * @param obj
     * @return
     */
    public static String setObject(String key, Object obj, int timeout) {
        Jedis jedis = getJedis();
        String result = null;
        try {
            result = jedis.set(key, objectSerialiable(obj));
            if (timeout != -1) {
                jedis.expire(key, timeout);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            releaseRedis(jedis);
        }
        return result;
    }

    /**
     * 获取对象
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Object getObject(String key) {
        Jedis jedis = getJedis();
        Object result = null;
        try {
            String data = jedis.get(key);
            if (!StringUtils.isEmpty(data)) {
                result = objectDeserialization(data);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            releaseRedis(jedis);
        }
        return result;
    }

    /**
     * 队列
     *
     * @param key
     * @param value
     * @param timeout
     * @return
     */
    public boolean lpush(String key, String value, int timeout) {
        Long result;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.lpush(key, value);
            if (timeout != -1) {
                jedis.expire(key, timeout);
            }
            if (null != result) {
                return true;
            } else {
                return false;
            }
        } finally {
            releaseRedis(jedis);
        }
    }

    /**
     * 取出队列数据
     */
    public String rpop(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.rpop(key);
        } catch (Exception e) {
            throw e;
        } finally {
            releaseRedis(jedis);
        }
    }


    //对象序列化为字符串
    public static String objectSerialiable(Object obj) {
        String serStr = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            serStr = byteArrayOutputStream.toString("ISO-8859-1");
            serStr = java.net.URLEncoder.encode(serStr, "UTF-8");

            objectOutputStream.close();
            byteArrayOutputStream.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return serStr;
    }

    //字符串反序列化为对象
    public static Object objectDeserialization(String serStr) {
        Object newObj = null;
        try {
            String redStr = java.net.URLDecoder.decode(serStr, "UTF-8");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            newObj = objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return newObj;
    }


    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";//即当key不存在时，我们进行set操作；若key已经存在，则不做任何操作
    private static final String SET_WITH_EXPIRE_TIME = "PX";//给这个key加一个过期的设置，具体时间由expireTime决定

    /**
     * 尝试获取分布式锁 参考https://www.cnblogs.com/linjiqin/p/8003838.html
     *
     * @param lockKey            锁key
     * @param acquireTimeoutInMS 等待时间 毫秒
     * @param lockTimeoutInMS    锁的超时时间 毫秒
     * @return 是否获取成功
     */
    public String acquireLockWithTimeout(String lockKey, long acquireTimeoutInMS, int lockTimeoutInMS) {
        String lockKeys = "lock:" + lockKey;
        String retIdentifier = null;
        String identifier = UUID.randomUUID().toString();
        Jedis jedis = null;
        try {
            jedis = getJedis();
            long end = System.currentTimeMillis() + acquireTimeoutInMS;
            while (System.currentTimeMillis() < end) {
                String result = jedis.set(lockKeys, identifier, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, lockTimeoutInMS);
                if (LOCK_SUCCESS.equals(result)) {
                    retIdentifier = identifier;
                    break;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (Exception e) {
            return "err";
        } finally {
            releaseRedis(jedis);
        }
        return retIdentifier;
    }

    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁key
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseLock(String lockKey, String requestId) {
        String lockKeys = "lock:" + lockKey;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(lockKeys), Collections.singletonList(requestId));
            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw e;
        } finally {
            releaseRedis(jedis);
        }

    }


    /**
     * redis scan 游标遍历的迭代器
     *
     * @param key
     */
//    public static Set<String> scan(String key) {
//        Set<String> set = new HashSet<>();
//        Jedis jedis = getJedis();
//        // 游标初始值为0
//        String cursor;
//        cursor = ScanParams.SCAN_POINTER_START;
//        ScanParams scanParams = new ScanParams();
//        scanParams.match(key);
//
//        // 代表每次遍历的槽位,并不是返回的条数
//        scanParams.count(5000);
//        while (true) {
//            //使用scan命令获取数据，使用cursor游标记录位置，下次循环使用
//            ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
//            cursor = scanResult.getStringCursor();// 返回0 说明遍历完成
//            List<String> list = scanResult.getResult();
//            long t1 = System.currentTimeMillis();
//            for (int m = 0; m < list.size(); m++) {
//                String resKey = list.get(m);
////                System.out.println(mapentry);
//                //jedis.del(key, mapentry);
//                set.add(resKey);
//            }
////            long t2 = System.currentTimeMillis();
////            System.out.println("获取" + list.size()
////                    + "条数据，耗时: " + (t2 - t1) + "毫秒,cursor:" + cursor);
//            if ("0".equals(cursor)) {
//                break;
//            }
//        }
//        return set;
//    }




}
