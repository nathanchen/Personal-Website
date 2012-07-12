package com.nathanchen.utils;

/**
 * 事情的严重级别
 * 
 * 用来统一化规定事情的严重级别
 * 
 * */
public enum CodeLevel
{
	SUCCESS,	// 操作成功，有组件受影响
	INFO,		// 正常操作，没有组件受影响
	FAIL,		// 操作失败，没有组件受影响
	ERROR,		// 操作失败，有组件受影响，需要关注
	FATAL		// 严重错误，导致系统宕机，需要关注
}
