package com.xtr.core.service.order;

import com.xtr.BaseTest;
import com.xtr.api.service.order.IdGeneratorService;
import com.xtr.comm.enums.BusinessEnum;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/8/4 13:37
 */
public class IdGeneratorServiceTest extends BaseTest {

    @Resource
    private IdGeneratorService idGeneratorService;

    /**
     * 测试1000个并发生成id会不会重复
     */
    @Test
    public void test1000(){

        int count = 1000;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(count);
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        for (int i = 0; i < count; i++)
            executorService.execute(new Task(cyclicBarrier, idGeneratorService));

        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class Task implements Runnable {

        private CyclicBarrier cyclicBarrier;
        private IdGeneratorService idGeneratorService;

        public Task(CyclicBarrier cyclicBarrier, IdGeneratorService idGeneratorService) {
            this.cyclicBarrier = cyclicBarrier;
            this.idGeneratorService = idGeneratorService;
        }

        @Override
        public void run() {
            try {
                // 等待所有任务准备就绪
                cyclicBarrier.await();
                // 测试内容
                System.out.println(idGeneratorService.getOrderId(BusinessEnum.COMPANY_RECHARGE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
