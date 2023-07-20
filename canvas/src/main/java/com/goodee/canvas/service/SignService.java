package com.goodee.canvas.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goodee.canvas.mapper.SignMapper;
import com.goodee.canvas.vo.Sign;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class SignService {
	
	@Autowired
	private SignMapper signMapper;
	
	
	// 이미지의 경로를 저장 및 메타데이터로 DB에 저장할 데이터 생성
	 public void addSign(String sign, String path){
		 
		 log.debug("SignService.addSign(sign, path) param sign:"+sign);
		 log.debug("SignService.addSign(sign, path) param path:"+path);
		 
		 // base64로 인코딩된 sign을 받아서 type과, size를 구한뒤 base64로 디코딩 ->(이미지로 저장하기 위함)
		 String type = sign.toString().split(",")[0].split(";")[0].split(":")[1]; // MIME 타입
		 log.debug("SignService.addSign(sign, path) param type:"+type);
		 // MIME을 분기문을 통해서 확장자로 변환
		 String ext = getExtension(type);
		 log.debug("SignService.addSign(sign, path) param ext:"+ext);
		 
		 String data = sign.toString().split(",")[1];
		 log.debug("SignService.addSign(sign, path) param data:"+data);
		 byte[] decoderData = Base64.getDecoder().decode(data); // :의 값은 에러를 발생시킴
		 log.debug("SignService.addSign(sign, path) param decoderData:"+decoderData);
		 
		 int size = decoderData.length;
		 log.debug("SignService.addSign(sign, path) param size:"+size);
		 
		 // 저장시 사용할 파일이름, 확장자를 추가한다.
		 String signSavename = UUID.randomUUID().toString().replace("-", "") + ext;
		 log.debug("SignService.addSign(sign, path) param signSavename:"+signSavename);
		 
		 // vo에 값 저장
		 Sign s = new Sign();
		 s.setSignMember("user");
		 s.setSignSavename(signSavename);
		 s.setSignFiletype(type);
		 s.setSignFilesize(size);
		 
		 // mapper를 통해 myBatis를 이용해 db에 저장
		 signMapper.insertSign(s);
		 
		 // 파일 저장
		 File f = new File(path+signSavename); // 경로 + 파일이름
		 log.debug("SignService.addSign(sign, path) param f:"+f);
		 
		 // outputstrim 사용
		 try {
			FileOutputStream fileOutputStream  = new FileOutputStream(f);
			fileOutputStream.write(decoderData);
			fileOutputStream.close();
			log.debug("SignService.addSign(sign, path) param f.length():"+f.length());
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			
			throw new RuntimeException();
		}
		 
		 
	     // String imagePath = "path/to/save/image";
	     
         // saveImage(base64Data,imagePath);

         // Save metadata in database using repository(mapper)
         // ImageMetadata metadata=new ImageMetadata();
         
          // Set your metadata properties here like:
          //metadata.setPath(imagePath); 

         // signMapper.save(metadata);
	}
	
	// MIME 타입을 switch문을 통해 ext로 변환 
	public String getExtension(String mimeType) {
		 switch(mimeType) {
		 case "image/jpeg":
			 return ".jpg";
		 case "image/png":
		 	 return ".png";
		 default:
			 throw new IllegalArgumentException("Unknown mime type: " + mimeType);
		 }
	}

	/*
	//  Base64 문자열과 경로를 입력받아 해당 경로에 이미지 파일을 생성하는 메서드
	public void saveImage(String base64Data, String path) throws Exception {
	    byte[] data = Base64.getDecoder().decode(base64Data);
	    
	    FileOutputStream out = new FileOutputStream(new File(path));
	    out.write(data);
	}
	*/
}
