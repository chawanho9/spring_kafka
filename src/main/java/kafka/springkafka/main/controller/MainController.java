package kafka.springkafka.main.controller;

import java.util.HashMap;
import java.util.Map;

import kafka.springkafka.util.kafka.KafkaMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @Autowired
    private KafkaMessageService kafkaMessageService;

    @RequestMapping("/main/requestKafka.do")
    @ResponseBody
    public  Map<String, Object> requestKafka(@RequestParam Map<String, String> param){
        Map<String, Object> resultMap = new HashMap<>();
        String data = param.get("jsonData").toString();
        Map<String,Object> resultMessage = kafkaMessageService.sendMessage("ner-in",data);
        resultMap.put("consumers",resultMessage);
        return resultMap;
    }
}
