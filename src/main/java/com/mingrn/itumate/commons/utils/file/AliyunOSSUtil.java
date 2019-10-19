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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 阿里云 OSS 工具类
 *
 * <br>在使用该阿里云OSS工具前请确定已配置好 {@code endpoint}、
 * {@code bucketName}、{@code accessKeyId}、{@code accessKeySecret}.
 * 具体见 CLASSPATH/META-INFO/spring-configuration-metadata.json
 *
 * 注意: 在使用时应将该工具类注册为 bean, 如使用 <code>@ComponentScan</code> 组件
 * 进行扫描该包.
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019/10/11 13:06
 */
@Component
public class AliyunOSSUtil implements EnvironmentAware {

    private AliyunOSSUtil() {
    }

    /** 阿里云OSS 端点 */
    private static String endpoint = null;
    /** 阿里云 bucket 名称, 即三级域名 */
    private static String bucketName = null;
    /** 阿里云OSS 授权账号, 建议 RAM 授权 */
    private static String accessKeyId = null;
    /** 阿里云OSS 授权密匙, 建议 RAM 授权 */
    private static String accessKeySecret = null;
    /** 防盗链, 即访问白名单, 如 https://*.example.com */
    private static List<String> bucketRefererList = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(AliyunOSSUtil.class);

    @SuppressWarnings("unchecked")
    @Override
    public void setEnvironment(Environment environment) {
        endpoint = environment.getProperty("aliyun.oss.endpoint");
        bucketName = environment.getProperty("aliyun.oss.bucketName");
        accessKeyId = environment.getProperty("aliyun.oss.accessKeyId");
        accessKeySecret = environment.getProperty("aliyun.oss.accessKeySecret");
        bucketRefererList = environment.getProperty("aliyun.oss.bucketReferer", List.class);
    }

    /**
     * 阿里云 Bucket 创建
     *
     * <br>阿里云OSS没有文件夹概念,在存储时 {@literal bucketName} 相当于三级域名.
     * 例如, 存储的阿里云OSS Endpoint 是: oss-cn-hangzhou.aliyuncs.com, 设置 bucketName 后
     * 即可使用链接 http(s)://bucketName.oss-cn-hangzhou.aliyuncs.com 进行访问.
     *
     * @return {@linkplain true} 创建成功, {@linkplain false} 创建失败
     */
    public static boolean createBucket() {
        return createBucket(bucketName);
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
    public static boolean createBucket(@NotNull String bucketName) {
        LOGGER.info(">>>>>>>>>> 请求创建阿里云OSS Bucket[{}] <<<<<<<<<<", bucketName);
        LOGGER.info(">>>>>>>>>> 正在请求登录阿里云OSS <<<<<<<<<<");
        OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            checkedOssEnvironment();
            return createBucket(oss, new CreateBucketRequest(bucketName));
        } finally {
            oss.shutdown();
            LOGGER.info(">>>>>>>>>> 阿里云OSS Bucket[{}] 创建完成, 关闭连接 <<<<<<<<<<", bucketName);
        }
    }

    public static boolean createBucket(OSS oss, CreateBucketRequest request) {
        return createBucket(oss, request, CannedAccessControlList.Default);
    }

    public static boolean createBucket(OSS oss, CreateBucketRequest request, CannedAccessControlList controlList) {

        if (Objects.isNull(request) || StringUtils.isEmpty(request.getBucketName())) {
            throw new IllegalArgumentException("阿里云 bucketName 不能为空");
        }

        if (!oss.doesBucketExist(request.getBucketName())) {
            LOGGER.info("阿里云OSS Bucket[{}] 不存在, 进行创建 Bucket: {}", request.getBucketName(), request.getBucketName());
            // 创建 bucket
            oss.createBucket(request);
            // 设置白名单
            setBucketReferer(oss, request.getBucketName());
            // 设置访问权限
            setBucketAcl(oss, request.getBucketName(), controlList);
        } else {
            LOGGER.info("阿里云OSS Bucket[{}] 已存在, 无需进行创建", request.getBucketName());
        }
        return oss.doesBucketExist(bucketName);
    }

    /**
     * bucket 设置防盗链
     *
     * <br>防盗链通过 {@link #bucketRefererList refererList} 设置白名单, 设置后
     * 仅允许指定的域名访问 OSS 资源.
     * 注意: 当 {@link #bucketRefererList refererList} 不为空时表示设置防盗链.
     * 具体有关防盗链见阿里云OSS官方文档:
     * <a href="https://help.aliyun.com/document_detail/31869.html?spm=a2c4g.11186623.6.605.41d11537aTkaVE">设置防盗链</a>
     *
     * @return {@linkplain true} 防盗链设置成功, {@linkplain false} 设置失败
     */
    public static boolean setBucketReferer() {
        return setBucketReferer(bucketName);
    }

    public static boolean setBucketReferer(String bucketName) {
        LOGGER.info(">>>>>>>>>> 请求设置阿里云OSS Bucket[{}] 防盗链 <<<<<<<<<<", bucketName);
        LOGGER.info(">>>>>>>>>> 正在请求登录阿里云OSS <<<<<<<<<<");
        OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            checkedOssEnvironment();
            return setBucketReferer(oss, bucketName);
        } finally {
            oss.shutdown();
            LOGGER.info(">>>>>>>>>> 阿里云OSS文件上传完成, 关闭连接 <<<<<<<<<<");
        }
    }

    public static boolean setBucketReferer(OSS oss, String bucketName) {
        return setBucketReferer(oss, bucketName, bucketRefererList);
    }

    public static boolean setBucketReferer(OSS oss, String bucketName, List<String> bucketRefererList) {

        BucketReferer referer;
        SetBucketRefererRequest refererRequest;
        LOGGER.info(">>>>>>>>>> 正在设置阿里云OSS bucket[ {} ] 防盗链 <<<<<<<<<<", bucketName);
        if (!CollectionUtils.isEmpty(bucketRefererList) && bucketRefererList.size() > 0) {
            referer = new BucketReferer(false, bucketRefererList);
            refererRequest = new SetBucketRefererRequest(bucketName, referer);
        } else {
            referer = new BucketReferer();
            refererRequest = new SetBucketRefererRequest(bucketName, referer);
        }
        oss.setBucketReferer(refererRequest);

        List<String> refererList = oss.getBucketReferer(bucketName).getRefererList();
        LOGGER.info(">>>>>>>>>> 阿里云OSS bucket[{}] 防盗链[ {} ]设置完成 <<<<<<<<<<", bucketName, refererList.toString());
        return !CollectionUtils.isEmpty(refererList) && refererList.size() > 0;
    }

    /**
     * 设置 bucket 访问权限值
     */
    public static void setBucketAcl(){
        LOGGER.info(">>>>>>>>>> 请求设置阿里云OSS Bucket[{}] 访问权限 <<<<<<<<<<", bucketName);
        LOGGER.info(">>>>>>>>>> 正在请求登录阿里云OSS <<<<<<<<<<");
        OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            checkedOssEnvironment();
            setBucketAcl(oss, bucketName);
        } finally {
            oss.shutdown();
            LOGGER.info(">>>>>>>>>> 阿里云OSS bucket[{}] 权限设置完成, 关闭连接 <<<<<<<<<<", bucketName);
        }
    }

    public static void setBucketAcl(OSS oss, String bucketName){
        setBucketAcl(oss, bucketName, CannedAccessControlList.Default);
    }

    public static void setBucketAcl(OSS oss, String bucketName, CannedAccessControlList controlList) {
        LOGGER.info(">>>>>>>>>> 正在设置阿里云OSS bucket[{}] 权限, 权限值:[{}] <<<<<<<<<<", bucketName, controlList.toString());
        SetBucketAclRequest aclRequest = new SetBucketAclRequest(bucketName, controlList);
        oss.setBucketAcl(aclRequest);
    }

    /**
     * 删除阿里云 bucket
     *
     * <br>危险操作, bucket 一旦删除该 bucket 下的所有文件都将被清除.并且无法恢复, 在调用该方法之前
     * 请确保该 bucket 下无文件或已做好备份操作.
     *
     * @return {@linkplain true} 删除成功, {@linkplain false} 删除失败
     */
    @Deprecated
    public static boolean deleteBucket() {
        return deleteBucket(bucketName);
    }

    /**
     * @param bucketName 文件存储容器, 三级域名
     */
    @Deprecated
    public static boolean deleteBucket(@NotNull String bucketName) {
        LOGGER.info(">>>>>>>>>> 请求删除阿里云OSS Bucket[{}] <<<<<<<<<<", bucketName);
        LOGGER.info(">>>>>>>>>> 正在请求登录阿里云OSS <<<<<<<<<<");
        OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            checkedOssEnvironment();
            return deleteBucket(oss, bucketName);
        } finally {
            oss.shutdown();
        }
    }

    private static boolean deleteBucket(OSS oss, @NotNull String bucketName) {

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
    public static String putObject(@NotNull String objectName, InputStream file) {
        return putObject(bucketName, objectName, file);
    }

    /**
     * 阿里云文件上传
     *
     * @param bucketName 文件存储容器, 三级域名
     * @return 文件上传地址
     * @see #putObject(String, InputStream)
     */
    public static String putObject(@NotNull String bucketName, @NotNull String objectName, InputStream file) {
        return putObject(bucketName, objectName, null, file);
    }

    /**
     * 阿里云文件上传
     *
     * @param metadata 自定义原数据信息
     * @return 文件上传地址
     * @see #putObject(String, InputStream)
     */
    public static String putObject(@NotNull String bucketName, @NotNull String objectName, ObjectMetadata metadata, InputStream file) {

        if (StringUtils.isEmpty(objectName)) {
            throw new IllegalArgumentException("阿里云OSS文件名 [" + objectName + "] 不能为空");
        }

        LOGGER.info(">>>>>>>>>> 请求阿里云OSS文件上传 <<<<<<<<<<");
        checkedOssEnvironment();
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

            LOGGER.info(">>>>>>>>>> 上传状态  : {} ", msg.isSuccessful() ? "成功" : "失败");
            LOGGER.info(">>>>>>>>>> 响应状态码: {} ", msg.getStatusCode());
            LOGGER.info(">>>>>>>>>> 文件流长度: {} ", msg.getContentLength());
            LOGGER.info(">>>>>>>>>> 文件Url  : {} ", msg.getUri());

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
        checkedOssEnvironment();
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

    /**
     * 检查阿里云 OSS 认证信息
     */
    private static void checkedOssEnvironment() {
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