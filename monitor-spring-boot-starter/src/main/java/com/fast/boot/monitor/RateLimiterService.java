package com.fast.boot.monitor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * @author: junqing.li
 * @date: 17/8/7
 */
public class RateLimiterService {


  private static LoadingCache<String, AtomicInteger> rate = CacheBuilder.newBuilder()
      .expireAfterWrite(1, TimeUnit.MINUTES).build(new CacheLoader<String, AtomicInteger>() {
        @Override
        public AtomicInteger load(String key) throws Exception {
          return new AtomicInteger(0);
        }
      });


  /**
   * 递增
   * 
   * @param key
   * @return
   * @throws ExecutionException
   */
  public static boolean enter(String key, int rateLimit) {

    return rate.getUnchecked(key).incrementAndGet() <= rateLimit;
  }



}
