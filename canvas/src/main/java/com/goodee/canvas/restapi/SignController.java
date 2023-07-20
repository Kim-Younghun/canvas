package com.goodee.canvas.restapi;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goodee.canvas.service.SignService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class SignController {
	@Autowired
	private SignService signService;
	
	@PostMapping("/addSign")
	public String addSign(HttpServletRequest request, @RequestParam(name="sign") String sign) {
		String path = request.getServletContext().getRealPath("/sign/");
		log.debug("SignController.addSign() param sign"+sign);
		// Service Layer : BASE64 디코딩 -> 이미지로 변경 -> 저장소에 이미지 저장 -> Mapper Layer : 메타데이터 DB저장
		
		// 컨트롤러 실행시 service 실행
		signService.addSign(sign, path);
		
		return "YES";
	}
	
	
}
