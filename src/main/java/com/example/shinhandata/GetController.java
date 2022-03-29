package com.example.shinhandata;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GetController {
    
    @GetMapping(value = "/idx")
    public ApiVO getByApi (String idx){
        ApiVO apiVO = new ApiVO();
        return apiVO;
    }
    
    public TimeResultVO[] getByTime (TimeVO timeVO){
        TimeResultVO[] timeResultVOS = new TimeResultVO[0];
        return timeResultVOS;
    }
}

