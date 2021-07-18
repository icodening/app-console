package cn.icodening.console.cloud.router;

import java.util.List;

/**
 * 负载调用前服务过滤器
 *
 * @author icodening
 * @date 2021.07.17
 */
public interface LoadBalancePreFilter<T> {

    /**
     * 过滤原始服务列表
     *
     * @param originServerList 负载前原始服务列表
     * @return 过滤后的服务列表
     */
    List<T> filter(List<T> originServerList, String param);


    String name();

    default List<T> filter(List<T> originServerList) {
        return filter(originServerList, null);
    }
}
