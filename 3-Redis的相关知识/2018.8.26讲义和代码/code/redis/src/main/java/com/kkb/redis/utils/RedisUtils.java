package com.kkb.redis.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {

	private static int port = 6379;
	private static String host = "192.168.10.133";
	private static JedisPool pool;

	static {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMinIdle(5);

		pool = new JedisPool(config, host, port);
	}

	public static Jedis getJedis() {
		return pool.getResource();
	}

	/**
	 * 获取分布式锁
	 * 
	 * @param lockKey
	 * @param requestId
	 * @param timeout
	 * @return
	 */
	public static boolean getLock(String lockKey, String requestId, int timeout) {
		// 获取Jedis对象，负责和远程redis服务器进行连接
		Jedis jedis = getJedis();
		// 参数三：NX和XX
		// 参数sin：EX和PX
		String result = jedis.set(lockKey, requestId, "NX", "EX", timeout);
		if (result == "OK") {
			return true;
		}

		return false;
	}

	public static synchronized boolean getLock2(String lockKey, String requestId, int timeout) {
		// 获取Jedis对象，负责和远程redis服务器进行连接
		Jedis jedis = getJedis();

		Long result = jedis.setnx(lockKey, requestId);
		if (result == 1) {
			// 设置有效期
			jedis.expire(lockKey, timeout);
			return true;
		}
		return false;
	}

	/***
	 * 释放分布式锁的代码
	 * @param lockKey
	 * @param requestId
	 */
	public static void releaseLock(String lockKey, String requestId) {
		// 获取Jedis对象，负责和远程redis服务器进行连接
		Jedis jedis = getJedis();
		if (requestId.equals(jedis.get(lockKey))) {
			jedis.del(lockKey);
		}
	}

	public static void main(String[] args) {
		poolConnect();
	}

	public static void singleConnect() {
		// Jedis单实例连接
		Jedis je = new Jedis(host, port);
		String result = je.get("k2");
		System.out.println(result);
		je.close();
	}

	public static void poolConnect() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMinIdle(5);

		JedisPool pool = new JedisPool(config, host, port);
		Jedis je = pool.getResource();

		String result = je.get("k2");
		System.out.println(result);

		je.close();
		pool.close();
	}
}
