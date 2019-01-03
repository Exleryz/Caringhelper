package com.weimore.entity

/**
 * @author Weimore
2018/12/6.
description:
 */
data class Result<T>(var flag: Boolean, var msg: String, var data: T)