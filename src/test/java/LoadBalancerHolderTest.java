import com.h.system.tinynignx.loadbalance.LoadBalancerHolder;

public class LoadBalancerHolderTest {
    public static void main(String[] args) throws Throwable {

        LoadBalancerHolder lh = LoadBalancerHolder.getInstance();
        lh.init();
    }
}
