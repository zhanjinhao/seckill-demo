package cn.addenda.seckilldemo;

import cn.addenda.businesseasy.lock.LockService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author addenda
 * @datetime 2022/12/4 15:12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LockTest {

    @Autowired
    private LockService lockService;

    @Test
    public void test1() throws Exception {
        new Thread(() -> {
            boolean b = lockService.tryLock("123");
            System.out.println("t1-" + b);
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lockService.unlock("123");
        }).start();

        Thread.sleep(1000);

        new Thread(() -> {
            boolean b = lockService.tryLock("123");
            System.out.println("t2-" + b);
            lockService.unlock("123");
        }).start();

        Thread.sleep(10000000);
    }

}
