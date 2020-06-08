package xuecheng.manage_cms_client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 韩浩辰
 * @date 2020/3/12 11:17
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class test {
    @Test
    public void test() {
        boolean a=true;
        boolean b=true;
        boolean c=false;
        boolean v=a && b || c;
        System.out.println(v);
    }
}
