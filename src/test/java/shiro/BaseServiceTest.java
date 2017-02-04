package shiro;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring/*.xml",})
public class BaseServiceTest extends AbstractJUnit4SpringContextTests {
    static {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        lc.reset();
        try {
            try {
                configurator.doConfigure(ResourceUtils.getFile("classpath:logback.xml"));//加载logback配置文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (JoranException e) {
            e.printStackTrace();
        }
        //PropertyConfigurator.configure("/home/lhy/Workspaces/MyEclipse_data/gooddeep/src/main/java/config/log4j.properties");//加载logj配置文件
    }

}
