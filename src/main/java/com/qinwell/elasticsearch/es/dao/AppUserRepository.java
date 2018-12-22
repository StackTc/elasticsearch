package com.qinwell.elasticsearch.es.dao;

import com.qinwell.elasticsearch.es.entity.AppUserEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * <p>
 * Project Name: Qinwell
 * <br>
 * Description:
 * <br>
 * File Name: AppUserRepository
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州勤淮科技有限公司
 * <br>
 *
 * @author lintiancheng
 * @create time：2018/12/23 1:17 AM
 * @version: v1.0
 */
public interface AppUserRepository extends ElasticsearchRepository<AppUserEs,Long> {

}