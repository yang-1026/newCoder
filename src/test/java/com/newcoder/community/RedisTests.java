package com.newcoder.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;

import java.util.concurrent.TimeUnit;

/**
 * @author liuyang
 * @create 2023-02-20 22:51
 */

@SpringBootTest
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;


    //测试字符串
    @Test
    public void testStrings() {
        String redisKey = "test:count";

        redisTemplate.opsForValue().set(redisKey, 1);

        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));
    }

    //测试哈希
    @Test
    public void testHashes() {
        String redisKey = "test:user";

        HashOperations ops = redisTemplate.opsForHash();
        ops.put(redisKey,"id",1);
        ops.put(redisKey,"username","刘阳");

        System.out.println(ops.get(redisKey,"id"));
        System.out.println(ops.get(redisKey,"username"));
    }


    //测试列表
    @Test
    public void testLists() {
        String redisKey = "test:ids";

        ListOperations ops = redisTemplate.opsForList();
        ops.leftPush(redisKey,101);
        ops.leftPush(redisKey,102);
        ops.leftPush(redisKey,103);

        System.out.println(ops.size(redisKey));
        System.out.println(ops.index(redisKey,0));
        System.out.println(ops.range(redisKey,0,2));
        System.out.println(ops.leftPop(redisKey));
        System.out.println(ops.leftPop(redisKey));
        System.out.println(ops.leftPop(redisKey));
    }


    //测试集合
    @Test
    public void testSets() {
        String redisKey = "test:teachers";

        SetOperations ops = redisTemplate.opsForSet();
        ops.add(redisKey,"刘备", "关羽", "张飞", "赵云", "诸葛亮");

        System.out.println(ops.size(redisKey));
        System.out.println(ops.pop(redisKey));
        System.out.println(ops.members(redisKey));
    }


    //测试有序集合
    @Test
    public void testSortedSets() {
        String redisKey = "test:students";

        ZSetOperations ops = redisTemplate.opsForZSet();
        ops.add(redisKey,"唐僧",80);
        ops.add(redisKey,"悟空",90);
        ops.add(redisKey,"八戒",50);
        ops.add(redisKey,"沙僧",70);
        ops.add(redisKey,"白龙马",60);

        System.out.println(ops.zCard(redisKey));
        System.out.println(ops.score(redisKey,"悟空"));
        System.out.println(ops.reverseRank(redisKey,"悟空"));
        System.out.println(ops.reverseRange(redisKey,0,2));
    }

    @Test
    public void testKeys() {
        redisTemplate.delete("test:user");

        System.out.println(redisTemplate.hasKey("test:user"));

        redisTemplate.expire("test:students",10, TimeUnit.SECONDS);
    }



    // 批量发送命令,节约网络开销.
    @Test
    public void testBoundOperations() {
        String redisKey = "test:count";
        BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        System.out.println(operations.get());
    }



    // 编程式事务。在提交事务之前的命令都不会被执行，提交后统一执行
    @Test
    public void testTransaction() {
        Object result = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String redisKey = "text:tx";

                // 启用事务
                operations.multi();
                operations.opsForSet().add(redisKey,"张三");
                operations.opsForSet().add(redisKey,"李四");
                operations.opsForSet().add(redisKey,"王五");

                System.out.println(operations.opsForSet().members(redisKey));
                // 提交事务
                return operations.exec();
            }
        });
        System.out.println(result);
    }


}
