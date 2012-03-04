package halo.dao.annotation;

import halo.dao.partition.DbPartitionHelperDef;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

	String name();

	/**
	 * 指使用spring声明的对象id
	 * 
	 * @return
	 */
	String partitionid() default "";

	/**
	 * 进行分区所使用的类
	 * 
	 * @return
	 */
	Class<?> partitionClass() default DbPartitionHelperDef.class;
}