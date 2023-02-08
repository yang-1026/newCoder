package com.newcoder.community.dao;

import org.springframework.stereotype.Repository;

/**
 * @author liuyang
 * @create 2023-02-07 11:22
 */


@Repository("alphaHibernate")
//自定义bean的名字:alphaHibernate，默认bean的名字为类名小写 alphaDaoHibernateImpl
public class AlphaDaoHibernateImpl implements AlphaDao{

    @Override
    public String select() {
        return "Hibernate";
    }
}
