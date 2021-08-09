package cn.icodening.console.cloud.router.common;

import cn.icodening.console.common.entity.RouterConfigEntity;
import cn.icodening.console.extension.ExtensionLoader;
import cn.icodening.console.util.CaseInsensitiveKeyMap;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author icodening
 * @date 2021.08.09
 */
public class HttpRequestRouterHelper {

    private static final HttpRequestRouterHelper INSTANCE = new HttpRequestRouterHelper();

    private final Map<String, HttpRequestExtractor> HTTP_REQUEST_EXTRACTOR = new CaseInsensitiveKeyMap<>(new ConcurrentHashMap<>(4));

    private final Map<String, ExpressionMatcher> EXPRESSION_MATCHER_MAP = new CaseInsensitiveKeyMap<>(new ConcurrentHashMap<>(4));

    private HttpRequestRouterHelper() {
        Map<String, ExpressionMatcher> expressionMatcherMap = ExtensionLoader.getExtensionLoader(ExpressionMatcher.class).getAllExtensionMap();
        Map<String, HttpRequestExtractor> httpRequestExtractorMap = ExtensionLoader.getExtensionLoader(HttpRequestExtractor.class).getAllExtensionMap();
        EXPRESSION_MATCHER_MAP.putAll(expressionMatcherMap);
        HTTP_REQUEST_EXTRACTOR.putAll(httpRequestExtractorMap);
    }

    public static String getTargetService(List<RouterConfigEntity> configs, HttpRequest httpRequest) {
        return INSTANCE.doGetTargetService(configs, httpRequest);
    }

    /**
     * 根据传入的配置以及请求对象，判断是否需要路由
     *
     * @param configs     配置项
     * @param httpRequest 请求
     * @return 目标service
     */
    private String doGetTargetService(List<RouterConfigEntity> configs, HttpRequest httpRequest) {
        String targetService = httpRequest.getURI().getHost();
        HttpHeaders headers = httpRequest.getHeaders();
        for (RouterConfigEntity config : configs) {
            String keySource = config.getKeySource();
            String key = config.getKeyName();
            if (!headers.containsKey(key)) {
                continue;
            }
            KeySourceExtractor<HttpRequest> requestTemplateKeySourceExtractor = HTTP_REQUEST_EXTRACTOR.get(keySource);
            String value = requestTemplateKeySourceExtractor.getValue(httpRequest, config.getKeyName());
            if (!StringUtils.hasText(value)) {
                continue;
            }
            String expression = config.getExpression();
            String matchType = config.getMatchType();
            ExpressionMatcher expressionMatcher = EXPRESSION_MATCHER_MAP.get(matchType);
            if (expressionMatcher.match(expression, value)) {
                targetService = config.getTargetService();
                break;
            }
        }
        return targetService;
    }
}
