package cn.icodening.console.cloud.router.common;

import cn.icodening.console.extension.Extensible;
import org.springframework.http.HttpRequest;

/**
 * @author icodening
 * @date 2021.08.09
 */
@Extensible
public interface HttpRequestExtractor extends KeySourceExtractor<HttpRequest> {
}
