package kafka.springkafka.main.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
         
@Controller
public class MainController {
            
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String topicName = "test";
    private String consumer_python = "";
    private String consumer_cpp =  "";
    private int cnt = 0;
    private int cpp_cnt = 0;
    private int python_cnt = 0;
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
            
    @RequestMapping("/kafka.do")
    public String kafka(String msg, Model model) {

        int count = Integer.parseInt(msg);

        LocalDateTime date = LocalDateTime.now();
        String dateStr = date.format(fmt);

        for (int i = 0; i < count; i++) {
            //System.out.println(i);
            kafkaTemplate.send(topicName, "8080 send " + dateStr + " : " + i+ ",   ");
            cnt ++;
        }

        while(true){
            System.out.println("cnt : " + cnt +" python_cnt : "+ python_cnt + " cpp_cnt : " + cpp_cnt + " ");

            if(cpp_cnt == cnt && python_cnt == cnt){
                model.addAttribute("consumer_python",consumer_python);
                model.addAttribute("consumer_cpp",consumer_cpp);
                consumer_python = "";
                consumer_cpp = "";
                python_cnt = 0;
                cpp_cnt = 0;
                cnt = 0;

                return "index";
            }
        }

    }
            
    @KafkaListener(topics = topicName,groupId = "python")
    public void listen(String message) {
        //System.out.println("Received Msg From test Topic" + message);
        consumer_python += message;
        python_cnt ++;
    }
            
    /**
      * groupId 를 다르게 하여 내용을 보여주기 위해 일부로 다르게 설정하였음.
      * @param headers header에 담긴 내용을 보여준다.
      * @param payload 넘어온 문자열에 대해 보여줌.
      */
    @KafkaListener(topics = topicName, groupId = "cpp")
    public void listen2(@Headers MessageHeaders headers,@Payload String payload) {
        /*System.out.println("=======================================");
        System.out.println("Consume Headers : " + headers.toString());
        System.out.println("=======================================");
        System.out.println("PayLoad : " + payload);
        System.out.println("=======================================");*/
        consumer_cpp += payload;
        cpp_cnt ++;
    }
            
}
