package kafka.springkafka.util.kafka;

import java.util.HashMap;
import java.util.Map;
         
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
         
@Configuration
public class KafkaConfiguration {
            
    private Environment env;

    @Autowired
    KafkaConfiguration(Environment env) {
                this.env = env;
    }
            
    public Map<String,Object> producerConfig() {

        Map<String,Object> props = new HashMap<>();

        //server host 지정
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG
                 ,env.getProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));

        // retries 횟수
        props.put(ProducerConfig.RETRIES_CONFIG
                 ,env.getProperty(ProducerConfig.RETRIES_CONFIG));

        //batch size 지정
        props.put(ProducerConfig.BATCH_SIZE_CONFIG
                 ,env.getProperty(ProducerConfig.BATCH_SIZE_CONFIG));

        //linger.ms
        props.put(ProducerConfig.LINGER_MS_CONFIG
                 ,env.getProperty(ProducerConfig.LINGER_MS_CONFIG));

        //buufer memory size 지정
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG
                 ,env.getProperty(ProducerConfig.BUFFER_MEMORY_CONFIG));

        //key serialize 지정
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG
                 ,StringSerializer.class);

        //value serialize 지정
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG
                 ,StringSerializer.class);

        return props;
    }

    public ProducerFactory<String,String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }
}