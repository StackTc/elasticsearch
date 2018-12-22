package com.qinwell.elasticsearch.mapper.user;

import com.qinwell.elasticsearch.entity.user.AppUser;
import com.qinwell.elasticsearch.entity.user.AppUserExample;

/**
 * AppUserMapper继承基类
 */
public interface AppUserMapper extends MyBatisBaseDao<AppUser, AppUser, AppUserExample> {
}