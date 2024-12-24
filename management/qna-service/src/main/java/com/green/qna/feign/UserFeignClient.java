package com.green.qna.feign;


import com.green.qna.Dto.UserReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="user-service", url="${user-service}")
public interface UserFeignClient {

    //갯유저 생성시 주소 바꿀것
    @GetMapping("user/sign/getuser")
    UserReqDto getUser(@RequestHeader("Authorization") String authorization);


}
