package com.cxb.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Autowired
    private RedisLock redisLock;

    @GetMapping("/")
    @Transactional(rollbackFor = Exception.class)
    public String testLock() {
        String token = null;
        String result = null;
        try {
            token = redisLock.lock("lock_name", 10000, 11000);
            if (token != null) {
                System.out.println("我拿到了锁哦...");
                // 执行业务代码
                Thread.sleep(15000L);
                result = "我拿到了锁哦, 执行业务代码";
            } else {
                System.out.println("我没有拿到锁唉...");
                result = "我没有拿到锁唉";
            }
        }
        catch (Exception e){
            System.out.println("处理业务逻辑报错...");
            result = "处理业务逻辑报错";
        }
        finally {
            if (token != null) {
                redisLock.unlock("lock_name", token);
                System.out.println("释放了锁...");
            }
        }
        return result;
    }
}
