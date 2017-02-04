package shiro;

import com.myph.manage.common.shiro.session.ShiroRedis;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * INFO: info
 * User: zhaokai
 * Date: 2016/9/1 - 18:16
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */

public class ShrioRedisTest extends BaseServiceTest {
    @Autowired
    private ShiroRedis shiroRedis;

    @Test
    public void tt() {
        for(int i=0 ;i <10000; i++){
        String s = "testzk"+i;
        String session = "session"+i;
        try {
            shiroRedis.updateCached(s.getBytes(), session.getBytes(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }}

    }
}
