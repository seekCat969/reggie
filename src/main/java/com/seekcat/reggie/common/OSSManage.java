package com.seekcat.reggie.common;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


@Component
@Data
public class OSSManage {

    // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
    @Value("${oss.base-param.endpoint}")
    private String endpoint;

    // 填写Bucket名称，例如examplebucket。
    @Value("${oss.base-param.bucket-name}")
    private String bucketName;

    // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
    @Value("${oss.base-param.object-name}")
    private String objectName;

    // 填写Bucket所在地域。以华东1（杭州）为例，Region填写为cn-hangzhou。
    @Value("${oss.base-param.region}")
    private String region;

    private EnvironmentVariableCredentialsProvider credentialsProvider;

    public OSSManage() throws com.aliyuncs.exceptions.ClientException {
        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        this.credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
    }

    public String uploadImages(MultipartFile image) throws OSSException, ClientException, IOException {

        // 创建OSSClient实例。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();

        String category = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
        String[] split = UUID.randomUUID().toString().split("-");
        StringBuilder uuid = new StringBuilder();
        for (String s : split) {
            uuid.append(s);
        }
        String fileName = objectName + uuid + category;

        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, image.getInputStream());
            // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);

            // 上传文件。
            ossClient.putObject(putObjectRequest);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return fileName;
    }

    public void deleteImage(String fileName) throws OSSException, ClientException{
        // 创建OSSClient实例。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();

        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(bucketName, fileName);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
