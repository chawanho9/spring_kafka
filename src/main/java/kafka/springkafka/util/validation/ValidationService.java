package kafka.springkafka.util.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ValidationService {

    /* 특수문자 제거
     * 테스트 문장을 그대로 리턴해주기 위해 " , ' , 개행문자 만 삭제
     */
    public String dataValidation(String data){
		/*String match = "[^\uAC00-\uD7A30-9a-zA-Z]";
		data = data.replaceAll(match," ").trim();*/
        data = data.replaceAll("\"", "");
        data = data.replaceAll("\'", "");
        data = data.replaceAll("\n", " ");
        System.out.println(data);
        return data;
    }

    /* 문장 자르기
     * 1.이전 인덱스(prevIndex)부터 설정값(maxLength)으로 기준잡고 기준에 가장 가까이 있는 공백까지 자름
     */
    public List<String> dataSplitFuntion(String data){

        List<String> resultList = new ArrayList<>();

        int maxLength = 300;
        int prevIndex = 0;

        while(true){
            int targetIndex = data.indexOf(" ",prevIndex+maxLength);

            if (targetIndex == -1){
                String substring = data.substring(prevIndex);
                resultList.add(substring);
                return resultList;
            }
            String substring = data.substring(prevIndex,targetIndex);
            prevIndex = targetIndex;

            resultList.add(substring);
        }
    }
}

