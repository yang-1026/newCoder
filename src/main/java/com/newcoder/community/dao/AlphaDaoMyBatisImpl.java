package com.newcoder.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * @author liuyang
 * @create 2023-02-07 11:25
 */

@Repository
@Primary
public class AlphaDaoMyBatisImpl implements AlphaDao{

    @Override
    public String select() {
        return "MyBatis";
    }
}
