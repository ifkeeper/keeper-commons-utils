package com.mingrn.itumate.commons.utils.file;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.Objects;

/**
 * 阿里云 OSS 工具类
 *
 * <br>在使用该阿里云OSS工具前请确定已配置好 {@code endpoint}、
 * {@code bucketName}、{@code accessKeyId}、{@code accessKeySecret}.
 * 具体见 CLASSPATH/META-INFO/additional-spring-configuration-metadata.json
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019/10/11 13:06
 */
@Component
public class AliyunOSSUtil implements EnvironmentAware {

    private static Environment environment;

    /** 阿里云容器地址, 容器, 账号, 密匙 */
    private static String endpoint, bucketName, accessKeyId, accessKeySecret = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(AliyunOSSUtil.class);

    private AliyunOSSUtil() {
    }

    @Override
    public void setEnvironment(Environment environment) {
        AliyunOSSUtil.environment = environment;
        endpoint = environment.getProperty("aliyun.oss.endpoint");
        bucketName = environment.getProperty("aliyun.oss.bucketName");
        accessKeyId = environment.getProperty("aliyun.oss.accessKeyId");
        accessKeySecret = environment.getProperty("aliyun.oss.accessKeySecret");
    }

    /**
     * 阿里云 Bucket 创建
     *
     * <br>阿里云OSS没有文件夹概念,在存储时 {@literal bucketName} 相当于三级域名.
     * 例如, 存储的阿里云OSS Endpoint 是: oss-cn-hangzhou.aliyuncs.com, 设置 bucketName 后
     * 即可使用链接 http(s)://bucketName.oss-cn-hangzhou.aliyuncs.com 进行访问.
     *
     * @param bucketName 文件存储容器, 三级域名
     * @return {@linkplain true} 创建成功, {@linkplain false} 创建失败
     */
    public static boolean createBucket(String bucketName) {
        LOGGER.info(">>>>>>>>>> 请求创建阿里云OSS Bucket[{}] <<<<<<<<<<", bucketName);
        LOGGER.info(">>>>>>>>>> 正在请求登录阿里云OSS <<<<<<<<<<");
        OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            checkedOSSEnvironment();
            return createBucket(oss, new CreateBucketRequest(bucketName));
        } finally {
            oss.shutdown();
        }
    }

    private static boolean createBucket(OSS oss, CreateBucketRequest request) {

        if (!oss.doesBucketExist(bucketName)) {
            LOGGER.info("阿里云OSS Bucket[{}] 不存在, 进行创建 Bucket: {}", bucketName, bucketName);
            if (Objects.isNull(request) || StringUtils.isEmpty(request.getBucketName())) {
                throw new IllegalArgumentException("阿里云 bucketName 不能为空");
            }
            oss.createBucket(request);
        } else {
            LOGGER.info("阿里云OSS Bucket[{}] 已存在, 无需进行创建", bucketName);
        }
        return oss.doesBucketExist(bucketName);
    }

    /**
     * 删除阿里云 bucket
     *
     * <br>危险操作, bucket 一旦删除该 bucket 下的所有文件都将被清除.并且无法恢复, 在调用该方法之前
     * 请确保该 bucket 下无文件或已做好备份操作.
     *
     * @param bucketName 文件存储容器, 三级域名
     * @return {@linkplain true} 删除成功, {@linkplain false} 删除失败
     */
    @Deprecated
    public static boolean deleteBucket(String bucketName) {
        LOGGER.info(">>>>>>>>>> 请求删除阿里云OSS Bucket[{}] <<<<<<<<<<", bucketName);
        LOGGER.info(">>>>>>>>>> 正在请求登录阿里云OSS <<<<<<<<<<");
        OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            checkedOSSEnvironment();
            return deleteBucket(oss, bucketName);
        } finally {
            oss.shutdown();
        }
    }

    private static boolean deleteBucket(OSS oss, String bucketName) {

        if (!oss.doesBucketExist(bucketName)) {
            LOGGER.info("阿里云OSS Bucket[{}] 不存在, 无需进行删除", bucketName);
            return true;
        }
        oss.deleteBucket(bucketName);
        return !oss.doesBucketExist(bucketName);
    }

    /**
     * 阿里云文件上传
     *
     * <br>阿里云OSS没有文件夹概念, 以阿里云OSS Endpoint: oss-cn-hangzhou.aliyuncs.com 为例.
     * 在存储文件 filename.suffix 成功后该文件互联网访问地址为 http(s)://bucketName.oss-cn-hangzhou.aliyuncs.com/filename.suffix
     * 如想要为文件归类可以使用虚拟文件夹实现. 如想要将 IMG 与 Flash 文件分类可以使用如下形式:
     * <p>
     * - IMG:   http(s)://bucketName.oss-cn-hangzhou.aliyuncs.com/img/filename.suffix
     * - Flash: http(s)://bucketName.oss-cn-hangzhou.aliyuncs.com/flash/filename.suffix
     * <p>
     * 即在文件前增加虚拟文件夹: /img/filename.suffix, /flash/filename.suffix 实现.
     * <p>
     * 另外, {@code bucketName} 使用系统配置, 不提供自定义方法. {@code bucketName} 为三级域名, 想要区分文件类别应在
     * {@code objectName} 上增加虚拟文件夹进行区分实现
     *
     * @param objectName 文件名
     * @param file       文件流
     * @return 文件上传地址
     */
    public static String putObject(String objectName, InputStream file) {
        return putObject(objectName, null, file);
    }

    /**
     * 阿里云文件上传
     *
     * @param metadata 自定义原数据信息
     * @see #putObject(String, InputStream)
     */
    public static String putObject(String objectName, ObjectMetadata metadata, InputStream file) {

        if (StringUtils.isEmpty(objectName)) {
            throw new IllegalArgumentException("阿里云OSS文件名 [" + objectName + "] 不能为空");
        }

        LOGGER.info(">>>>>>>>>> 请求阿里云OSS文件上传 <<<<<<<<<<");
        checkedOSSEnvironment();
        LOGGER.info(">>>>>>>>>> 正在请求登录阿里云OSS <<<<<<<<<<");
        OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            LOGGER.info(">>>>>>>>>> 正在检查 Bucket[ {} ] <<<<<<<<<<", bucketName);
            createBucket(oss, new CreateBucketRequest(bucketName));
            BucketInfo info = oss.getBucketInfo(bucketName);
            LOGGER.info(">>>>>>>>>> 阿里云 Bucket[ {} ] 信息如下：<<<<<<<<<<", bucketName);
            LOGGER.info(">>>>>>>>>> 数据中心: {}", info.getBucket().getLocation());
            LOGGER.info(">>>>>>>>>> 创建时间: {}", info.getBucket().getCreationDate());
            LOGGER.info(">>>>>>>>>> 用户标志: {}", info.getBucket().getOwner());

            oss.putObject(bucketName, objectName, file, metadata);
            LOGGER.info(">>>>>>>>>> 阿里云OSS Bucket[{}] 上传文件[{}] 执行完成, 响应信息如下: <<<<<<<<<<", bucketName, objectName);

            OSSObject ossObject = oss.getObject(bucketName, objectName);
            ResponseMessage msg = ossObject.getResponse();

            LOGGER.info(">>>>>>>>>> 是否上传成功: [{}] , 响应状态码: [{}], 内容长度 [{}], 文件Url: [{}] <<<<<<<<<<",
                    msg.isSuccessful(), msg.getStatusCode(), msg.getContentLength(), msg.getUri());

            return msg.getUri();
        } finally {
            oss.shutdown();
            LOGGER.info(">>>>>>>>>> 阿里云OSS文件上传完成, 关闭连接 <<<<<<<<<<");
        }
    }

    /**
     * 删除阿里云OSS文件
     *
     * <br>危险操作,删除后将不可恢复.在删除前请确定已做好备份操作.
     *
     * @param objectName 文件名
     * @return {@linkplain true} 删除成功, {@linkplain false} 删除失败
     */
    public static boolean deleteObject(@NotNull String objectName) {

        if (StringUtils.isEmpty(objectName)) {
            throw new IllegalArgumentException("阿里云OSS文件名 [" + objectName + "] 不能为空");
        }

        LOGGER.info(">>>>>>>>>> 请求删除阿里云OSS文件 <<<<<<<<<<");
        checkedOSSEnvironment();
        LOGGER.info(">>>>>>>>>> 正在请求登录阿里云OSS <<<<<<<<<<");
        OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            if (!oss.doesObjectExist(bucketName, objectName)) {
                LOGGER.info(">>>>>>>>>> 阿里云OSS Bucket[{}] 文件[{}] 不存在, 无需进行删除 <<<<<<<<<<", bucketName, objectName);
                return true;
            }

            LOGGER.info(">>>>>>>>>> 正在检查 Bucket[ {} ] <<<<<<<<<<", bucketName);
            createBucket(oss, new CreateBucketRequest(bucketName));
            BucketInfo info = oss.getBucketInfo(bucketName);
            LOGGER.info(">>>>>>>>>> 阿里云 Bucket[ {} ] 信息如下：<<<<<<<<<<", bucketName);
            LOGGER.info(">>>>>>>>>> 数据中心: {}", info.getBucket().getLocation());
            LOGGER.info(">>>>>>>>>> 创建时间: {}", info.getBucket().getCreationDate());
            LOGGER.info(">>>>>>>>>> 用户标志: {}", info.getBucket().getOwner());

            oss.deleteObject(bucketName, objectName);
            LOGGER.info(">>>>>>>>>> 删除阿里云OSS Bucket[{}] 文件[{}] 执行完成 <<<<<<<<<<", bucketName, objectName);

            return !oss.doesObjectExist(bucketName, objectName);
        } finally {
            oss.shutdown();
            LOGGER.info(">>>>>>>>>> 删除阿里云OSS文件完成, 关闭连接 <<<<<<<<<<");
        }
    }

    /** 检查阿里云 OSS 认证信息 */
    private static void checkedOSSEnvironment() {
        LOGGER.info(">>>>>>>>>> 正在检查阿里云OSS授权认证信息 <<<<<<<<<<");
        if (StringUtils.isEmpty(endpoint)) {
            throw new IllegalArgumentException("You Do not Set aliyun OSS access Endpoint( Endpoint Can't be set to Null or Empty ), " +
                    "You can be set by KEY[aliyun.oss.endpoint] in the configuration file(For example: application.properties)");
        }

        if (StringUtils.isEmpty(bucketName)) {
            throw new IllegalArgumentException("You Do not Set aliyun OSS Bucket Name( BucketName Can't be set to Null or Empty ), " +
                    "You can be set by KEY[aliyun.oss.bucketName] in the configuration file(For example: application.properties)");
        }

        if (StringUtils.isEmpty(accessKeyId)) {
            throw new IllegalArgumentException("You Do not Set aliyun OSS Authorized Account( Authorized Account Can't be set to Null or Empty ), " +
                    "You can be set by KEY[aliyun.oss.accessKeyId] in the configuration file(For example: application.properties)");
        }

        if (StringUtils.isEmpty(accessKeySecret)) {
            throw new IllegalArgumentException("You Do not Set aliyun OSS Authorized Secret( Authorized Secret Can't be set to Null or Empty ), " +
                    "You can be set by KEY[aliyun.oss.accessKeySecret] in the configuration file(For example: application.properties)");
        }
        LOGGER.info(">>>>>>>>>> 阿里云OSS授权认证信息检查完成 <<<<<<<<<<");
    }
}