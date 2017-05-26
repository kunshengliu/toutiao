package com.uestc.service;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);

	//设置Key
	private static String accessKey = "JW_cpuYEZ8LyolOGc9KxN50U9LjpH1IcpUIqp2Kn";
	private static String secretKey = "6hGG4zgx1KM8_WsdIiBZifGdJXDCrfazYakyDq_L";
	private static String bucketName="pppbucket";//设置存储空间
	private static String IMAGE_DOMAIN="http://oqjtgt6eg.bkt.clouddn.com/";
	private static Configuration cfg = new Configuration(Zone.zone2());
	
	
	
	public String upLoadSaveImage(MultipartFile file) throws IOException{
		int dotPos = file.getOriginalFilename().lastIndexOf(".");
		if(dotPos<0){
			return null;
		}
		String fileExt = file.getOriginalFilename().substring(dotPos+1).toLowerCase();
		
		String fileName = UUID.randomUUID().toString()+"."+fileExt;//新的文件名字
		
		Auth auth = Auth.create(accessKey, secretKey);//获取秘钥
		String token = auth.uploadToken(bucketName);//设置上传策略
	    UploadManager uploadManager = new UploadManager(cfg);//初始化上传管理器
	    Response response  = uploadManager.put(file.getBytes(), fileName, token);//进行上传’
	    //返回打印信息
	    if(response.isOK()&&response.isJson()){
	    	return IMAGE_DOMAIN+JSONObject.parseObject(response.bodyString()).get("key");
	    }else{
	    	logger.error("文件上传异常"+response.bodyString());
	    	return null;
	    }

	}
}
