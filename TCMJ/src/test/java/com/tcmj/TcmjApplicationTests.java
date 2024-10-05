package com.tcmj;

import com.tcmj.controller.AllController;
import com.tcmj.controller.HerbController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TcmjApplicationTests {

    @Autowired
    AllController allController;

    @Test
    void contextLoads() {
        allController.getAllLinks();
    }

}
