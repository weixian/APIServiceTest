package com.wesai.RedisClient;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/*
 * 简要版本的redis客户端。实现数据的增、删、改、查。也可以直接调用jedis模块进程操作，不用本模块。
 */
public class RedisClient {
	private static JedisPool jedisPool;
	private static Jedis jedis;

	public static void main(String[] args) throws Exception {
    // 连接redis，进行写、追加、读、删除操作，最后释放连接到连接池，应用操作完毕，关闭连接池
		try {
      connect("172.16.129.222", 6379);

      // connect("10.2.2.12", 6382);
			// System.out.println(sadd("test", "redis"));
			// System.out.println(sadd("test", "-test"));
      System.out.println(
          hget("onlineItemScreenings:e3663d0c0f2526cddff479b8e1c0785d", "10004928146301503"));

      System.out.println(
          hmget("onlineItemScreenings:e3663d0c0f2526cddff479b8e1c0785d", "10004928146301503"));
          // System.out.println(hmget("onlineItemPrice:40a96062b0ce3518ba6a2ccf7cbed1b8:10004572145440819",
          // "6608a73115f9af86efd3623c7e8f94a3"));

			// System.out.println(delete("test"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			releaseResource();
			colse();
		}
	}

	/*
   * redis连接
   */
	public static void connect(String host, Integer port, Integer timeout,
			String password) throws Exception {
    // 为了保证线程安全性，这里采用连接池连接redis服务器
		jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout,
				password);
		if (jedisPool != null) {
			jedis = jedisPool.getResource();
		} else {
			throw new Exception("jedisPool is null");
		}

	}

	/*
   * 不需要权限验证的连接
   */
	public static void connect(String host, Integer port) throws Exception {
		connect(host, port, 60000, null);
	}

	/*
   * 需要权限验证的连接
   */
	public static void connect(String host, Integer port, String password)
			throws Exception {
		connect(host, port, 60000, password);
	}

	/*
   * 使用完毕，将连接放回连接池
   */
	public static void releaseResource() {
		if (jedis != null) {
			jedisPool.returnBrokenResource(jedis);

		}
	}

	/*
   * 应用退出时，关闭连接池
   */
	public static void colse() {
		jedisPool.destroy();
	}

	/*
   * 添加数据
   */
	public static String set(String key, String value) {
		return jedis.set(key, value);
	}

	/*
   * 拼接
   */
	public static Long append(String key, String value) {
		return jedis.append(key, value);
	}

	/*
   * 添加多个键值对
   */
	public static String mset(String... keysvalues) {
		return jedis.mset(keysvalues);
	}

	public static String mset(byte[]... keysvalues) {
		return jedis.mset(keysvalues);
	}

	public static String get(String key) {
		return jedis.get(key);
	}

	public static byte[] get(byte[] key) {
		return jedis.get(key);
	}

	/*
	 *
	 */
	public static List<String> mget(String... keys) {
		return jedis.mget(keys);
	}

  public static String hget(String keys, String fields) {
    return jedis.hget(keys, fields);
  }

  public static List<String> hmget(String keys, String... fields) {
    return jedis.hmget(keys, fields);
  }

	public static List<byte[]> mget(byte[]... keys) {
		return jedis.mget(keys);
	}

	/*
   * 添加map
   */
	public static String hmset(String key, Map<String, String> hash) {
		return jedis.hmset(key, hash);
	}

	public static String hmset(byte[] key, Map<byte[], byte[]> hash) {
		return jedis.hmset(key, hash);
	}

	/*
   * 删除
   */
	public static Long delete(byte[] key) {
		return jedis.del(key);
	}

	public static Long delete(byte[]... keys) {
		return jedis.del(keys);
	}

	public static Long delete(String key) {
		return jedis.del(key);
	}

	public static Long delete(String... keys) {
		return jedis.del(keys);
	}

	/*
   * 添加Set
   */
	public static Long sadd(String key, String... members) {
		return jedis.sadd(key, members);
	}

	public static Long sadd(byte[] key, byte... members) {
		return jedis.sadd(key, members);
	}

	/*
   * 获取Set
   */
	public static Set<String> smembers(String key) {
		return jedis.smembers(key);
	}

	public static Set<byte[]> smembers(byte[] key) {
		return jedis.smembers(key);
	}

	/*
   * 移除Set某个成员
   */
	public static Long srem(byte[] key, byte... member) {
		return jedis.srem(key, member);
	}

	/*
   * 移除Set某个成员
   */
	public static Long srem(String key, String... member) {
		return jedis.srem(key, member);
	}

}
