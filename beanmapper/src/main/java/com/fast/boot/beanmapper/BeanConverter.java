package com.fast.boot.beanmapper;

/**
 * Created by junqing.li on 17/2/28.
 * 自定义转换接口
 */
public interface BeanConverter<S, T> {

	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	T convert(S source, T target);
}
