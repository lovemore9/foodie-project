package com.imooc.config;

import com.imooc.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 类简要说明
 *
 * @author wangyujin
 * @version 1.0
 * </pre>
 * Created on 2020-03-16 21:11
 * </pre>
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderJob {

    private final OrderService orderService;

    //集群使用会有问题，每个单体都会去执行
    //对数据库全表搜索，性能问题

    //后续使用延时队列解决问题
    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void autoCloseOrder() {
        orderService.clouseOrder();
    }
}
