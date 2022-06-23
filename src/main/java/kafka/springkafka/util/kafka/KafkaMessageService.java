package kafka.springkafka.util.kafka;

import kafka.springkafka.util.validation.ValidationService;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class KafkaMessageService {

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private String consumer_python = new String();
    private String consumer_cpp = new String();
    private int cnt = 0;
    private int cpp_cnt = 0;
    private int python_cnt = 0;

    @Autowired
    private ValidationService validationService;
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public Map<String,Object> sendMessage(String topic, String data) {

        Map<String, Object> resultMap = new HashMap<>();
        String tmp = "";

        //값 보내기전에 특수 문자 ,개행문자 제거해야됨 => " ' /n 만 제거하면될듯
        String validation_data = validationService.dataValidation(data);

        List<String> dataSplitFuntion = validationService.dataSplitFuntion(validation_data);

        for (String splitData : dataSplitFuntion) {
            //pyton에서 json형식으로 만들어놔서 양쪽에 "" 추가
            tmp = "\"" + splitData + "\"";
            splitData = tmp;

            kafkaTemplate.send(topic, splitData);
            cnt ++;
        }

        /* 리턴해주기
         * 모든 consumer의 값이 들어왔을 경우  return
         */
        while(true){
            if(cpp_cnt == cnt && python_cnt == cnt){
                System.out.println("consumer return");
                resultMap.put("consumer_python",consumer_python);
                resultMap.put("consumer_cpp",consumer_cpp);
                consumer_python = "";
                consumer_cpp = "";
                python_cnt = 0;
                cpp_cnt = 0;
                cnt = 0;

                return resultMap;
            }
        }
    }

    /**
     * groupId 를 다르게 하여 내용을 보여주기 위해 일부로 다르게 설정하였음.
     * @param headers header에 담긴 내용을 보여준다.
     * @param payload 넘어온 문자열에 대해 보여줌.
     */
    @KafkaListener(topics = "ner-out",groupId = "python")
    public void listen(@Headers MessageHeaders headers, @Payload String payload) {
        System.out.println("Consume Headers : " + headers.toString() + " PayLoad : " + payload);
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(payload);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = obj.toString();
        result = result.replace("\\", "" );
        consumer_python += result;
        python_cnt ++;
    }

    /**
     * groupId 를 다르게 하여 내용을 보여주기 위해 일부로 다르게 설정하였음.
     * @param headers header에 담긴 내용을 보여준다.
     * @param payload 넘어온 문자열에 대해 보여줌.
     */
    @KafkaListener(topics = "cpp-out", groupId = "cpp")
    public void listen2(@Headers MessageHeaders headers,@Payload String payload) {
        System.out.println("Consume Headers : " + headers.toString() + "PayLoad : " + payload);
/*        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(payload);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = obj.toString();
        result = result.replace("\\", "" );
        consumer_cpp += result;
        */
        consumer_cpp += payload;
        cpp_cnt ++;
    }
}
