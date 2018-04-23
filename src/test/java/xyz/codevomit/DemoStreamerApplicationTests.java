package xyz.codevomit;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import xyz.codevomit.demostreamer.BattlefieldApplication;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = BattlefieldApplication.class)
//@WebAppConfiguration
public class DemoStreamerApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(DemoStreamerApplicationTests.class);
    
    private MockMvc mockMvc;
    //@Autowired
    private WebApplicationContext webApplicationContext;

    //@Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
}
