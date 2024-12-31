package com.dongyang.dongpo.common.util.member;

import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberUtil {

    // 출생 연도에 따라 연령대 계산
    public String getAgeGroup(String birthyear){
        int age = LocalDate.now().getYear() - Integer.parseInt(birthyear);
        if(age < 20) return "10";
        else if(age < 30) return "20";
        else if(age < 40) return "30";
        else if(age < 50) return "40";
        else if(age < 60) return "50";
        else return "60";
    }
}
