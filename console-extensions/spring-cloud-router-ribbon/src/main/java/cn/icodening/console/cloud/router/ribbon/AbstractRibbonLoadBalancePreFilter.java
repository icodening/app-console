package cn.icodening.console.cloud.router.ribbon;

import cn.icodening.console.cloud.router.common.filter.LoadBalancePreFilter;
import com.netflix.loadbalancer.Server;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽象Ribbon Server负载前实例过滤器
 *
 * @author icodening
 * @date 2021.07.17
 */
public abstract class AbstractRibbonLoadBalancePreFilter<T extends Server> implements LoadBalancePreFilter<T> {

    @Override
    public List<T> filter(List<T> originServerList, String param) {
        if (originServerList == null || originServerList.isEmpty()) {
            return null;
        }
        return originServerList.stream()
                .filter(server -> isEligible(server, param))
                .collect(Collectors.toList());
    }

    /**
     * 由于其他框架扩展Server后与之对应的属性映射关系有些不一致，固需要抽象判断方法由子类实现
     * 判断服务实例是否满足条件
     *
     * @param server 服务实例
     * @return true表示满足条件，将被加入负载候选列表
     */
    protected abstract boolean isEligible(T server, String param);
}
