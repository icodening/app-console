package cn.icodening.console.server.service.impl;

import cn.icodening.console.common.model.PushData;
import cn.icodening.console.server.service.NotifyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author icodening
 * @date 2021.06.08
 */
@Component
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void notify(PushData pushData, List<String> addresses) {
        try {
            final String json = objectMapper.writeValueAsString(pushData);
            final HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
            final HttpEntity<Object> requestEntity = new HttpEntity<>(json, httpHeaders);
            CompletableFuture.supplyAsync(() -> addresses.stream()
                    .map(address -> {
                        final ResponseEntity<String> exchange = restTemplate.exchange(address, HttpMethod.POST, requestEntity, String.class);
                        return exchange.getStatusCode().is2xxSuccessful();
                    }).collect(Collectors.groupingBy(bool -> bool)))
                    .whenComplete((ret, ex) -> {
                        if (ex != null) {
                            ex.printStackTrace();
                        }
                        int successCount = 0, failCount = 0;
                        if (ret.get(Boolean.TRUE) != null) {
                            successCount = ret.get(Boolean.TRUE).size();
                        }
                        if (ret.get(Boolean.FALSE) != null) {
                            failCount = ret.get(Boolean.FALSE).size();
                        }
                        System.out.println("notify instances success: " + successCount + ", fail: " + failCount);
                    });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
