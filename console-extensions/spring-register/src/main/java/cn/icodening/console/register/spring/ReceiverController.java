package cn.icodening.console.register.spring;

import cn.icodening.console.model.PushData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author icodening
 * @date 2021.06.08
 */
public class ReceiverController {

    @PostMapping("/configReceiver")
    @ResponseBody
    public Object receiverConfig(@RequestBody PushData pushData) {
        System.out.println(pushData);
        return "success";
    }
}
