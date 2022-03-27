package org.jeecg.modules.KM.common.init;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.KM.service.IKmEsMgntService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Order(1) //指定顺序
public class EsTemplateInit implements CommandLineRunner {
    @Autowired
    IKmEsMgntService iKmEsMgntService;

    @Override
    public void run(String... args) throws Exception {
        log.info("start check ES template and init...");
        iKmEsMgntService.initTemplate();
    }
}