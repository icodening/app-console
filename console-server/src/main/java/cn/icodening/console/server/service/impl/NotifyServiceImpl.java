package cn.icodening.console.server.service.impl;

import cn.icodening.console.server.model.PushData;
import cn.icodening.console.server.service.NotifyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

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
            httpHeaders.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(json.getBytes().length));
            final HttpEntity<Object> requestEntity = new HttpEntity<>(json, httpHeaders);
            for (String address : addresses) {
                final ResponseEntity<String> exchange = restTemplate.exchange(address, HttpMethod.POST, requestEntity, String.class);
                final HashMap<String, String> resultMap = new HashMap<>(2);
                if (exchange.getStatusCode().is2xxSuccessful()) {
                    System.out.println("push success");
                }
            }
//            CompletableFuture.supplyAsync(() -> addresses.stream()
//                    .map(address -> {
//                        final ResponseEntity<String> exchange = restTemplate.exchange(address, HttpMethod.POST, requestEntity, String.class);
//                        final HashMap<String, String> resultMap = new HashMap<>(2);
//                        resultMap.put("address", address);
//                        if (exchange.getStatusCode().is2xxSuccessful()) {
//                            resultMap.put("success", Boolean.TRUE.toString());
//                            return resultMap;
//                        }
//                        resultMap.put("success", Boolean.FALSE.toString());
//                        return resultMap;
//                    }).collect(Collectors.groupingBy(map
//                            -> Boolean.parseBoolean(map.get("success")))))
//                    .whenComplete((result, ex) -> {
//                        if (ex != null) {
//                            ex.printStackTrace();
//                            return;
//                        }
//                        final int successCount = result.get(true).size();
//                        final int failCount = result.get(false).size();
//                        //FIXME log
//                        System.out.println("success: " + successCount + ", fail: " + failCount);
//                    });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }
}
