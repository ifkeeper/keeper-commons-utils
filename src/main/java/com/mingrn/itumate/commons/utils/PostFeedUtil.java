package com.mingrn.itumate.commons.utils;

import com.mingrn.itumate.commons.utils.date.DateTimeUtil;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * 帖子工具类
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 * @date 2019/10/7 17:18
 */
public class PostFeedUtil {

    private PostFeedUtil() {
    }

    /**
     * 用户帖子ID
     * <br>
     *
     * 帖子ID(postId)设计格式如下所示:
     * <pre>
     *       uId(建议6位)       timeCompress       seq
     *     +-----------+      +-----------+     +---+
     *     |x|x|x|x|x|x|   +  |x|x|x|x|x|x|  +  |x|x|
     *     +-----------+      +-----------+     +---+
     * </pre>
     *
     * 帖子 ID 生成规则为 UID + 36进制编码[当前时间秒数 - 系统发布时间秒数](6位) + seq(2位)
     * postId首6位为uId(可用户标识用户), 每一位是 [\d\w] 这36个字符中的某一个, 建议6位即可表示
     * 21亿不同的用户. 后续时间戳(精确到秒)可以标识70年范围内的任意一秒, 单个用户每秒发放的帖子不超
     * 过两位 seq 表达的最大值.<br>
     *
     * timeCompress 可设计为:
     *  - 帖子发布的时间减去 sns 系统初次发布的时间点中间间隔的秒(下位用字符 s 标识), 并进行36禁止编码.
     *    s 值为 60466175 时 36进制编码字符为 zzzzz, 也是五位最大允许值, 当 s 值大于 60466175 时
     *    timeCompress 值为6位.
     *    s 值为 1679615 时 36进制编码字符为 zzzz, 也是四位最大允许值. 因此该值通常为 6 位.
     * <br>
     *
     * 这样设计后, timeCompress 的字母随时间(粒度为秒)连续递增, 可以充分利用 DB 的范围扫描. 由于 uId 是
     * 固定的可用于标识某个用户, 在查看该用户的帖子时可利用如下 SQL 实现:
     * <sql>
     *     select * from post where postId like 'uId%';
     * </sql>
     *
     * 对于查询指定日期范围内的帖子, 只需要计算好帖子查询起始日期即可利用如下 SQL 实现(注意 seq 值):
     * <sql>
     *     select * from post where postId between postId1 and postId2;
     * </sql>
     *
     * 由于查询的同一个用户的帖子, 所以所有 postId 的前缀都是相同的, 如果查询这个用户某个时间范围内的帖子,
     * 那么 6 位 timeCompress 的前几位也是相同. 例如查询10分钟以内的帖子前4位 timeCompress 一定相同.
     * 由于 DB 的聚簇索引采用 B+ 树类似的存储, 相同前缀的数据相邻存放, 这样一来上述 sql 使用 DB 的 rangeScan,
     * 避免了回表造成的随机读.
     *
     * @param uId       uId当为 [\d\w] 组成, 可用户标识用户id信息
     * @param benchmark 系统发布时间, 精确到毫秒数
     * @param seq       用户每秒发送帖子个数, 最大 99, 最小 1
     */
    public static String postId(@NotNull String uId, long benchmark, int seq) {

        Objects.requireNonNull(uId, "uId can't be null");

        if (seq > 99 || seq < 1) {
            throw new RuntimeException(" seq Must be between 1 and 99 ");
        }

        // 获取系统发布时间距当前秒数, 并进行36进制编码
        long diff = (System.currentTimeMillis() - benchmark) / 1000;
        String base36 = Long.toString(diff, 36);

        String fill = seq + "";

        return uId + base36 + (fill.length() == 2 ? fill : "0" + fill);
    }

    /**
     * 用户周帖Id
     * <br>
     *
     * 场景: 当前系统有十亿+用户, 其中某些用户是 V 用户, 该用户的帖子通常为热点数据, 有些
     * 用户可能会进行查阅 V 用户的历史帖子, 假设 V 用户每天发表 5+ 帖子, 一周就是 35+ 帖子.
     * 所以当热点用户较多时直接存储在 DB 中会存在很大程度的压力.
     *
     * 因此可以将该用户的帖子进行分组, 这里按周进行分组(也可以自行按月分组). 每周的贴子都存在
     * 指定周组下, 将这个数据存放在 cache 中, 这样在查询某个用户某周的帖子时可直接根据组进行
     * 获取.<br>
     *
     * 周帖ID设计格式如下所示:
     * <pre>
     *       uId(建议6位)        timestamp
     *     +-----------+      +-----------+
     *     |x|x|x|x|x|x|   +  |x|x|x|x|x|x|
     *     +-----------+      +-----------+
     * </pre>
     *
     * timestamp 可设计为:
     *  - 帖子发布时间所处周的第一天(舍去时分秒)减去 sns 系统初次发布的时间点中间间隔的秒, 假
     *    设帖子发布时间为 2019-10-10 号, 则周一为 2019-10-07, 所以 2019-10-07 00:00:00
     *    的时间戳(粒度为秒) 为 1570377600. 得到的 timestamp 为当前时间减去 1570377600.
     *    或者直接读取为 1570377600 (直接采用 1570377600 id 长度会相应的变长, 在 redis 中不建议).
     *
     * 数据可以利用 Redis 进行存储, 数据存储格式为:
     * - key:   uId + 时间戳(单位秒, 精确到星期) 或 uId + 时间戳(单位秒, 精确到星期) - sns 系统初次发布时间(精确到秒)
     * - value: redis 的 hash 类型
     *    - field 为 postId {@link #postId(String, long, int) postId}
     *    - value 为帖子内容.
     * - expire: 可设置为 1 星期, 即最多同时存在两星期的数据(假设每帖平均长度为 0.1KB, 1亿用户每天发3帖语句数量为 400G).
     *
     * @param uId       uId当为 [\d\w] 组成, 可用户标识用户id信息
     * @param benchmark 系统发布时间, 精确到毫秒数
     */
    public static String postWeekId(@NotNull String uId, long benchmark) {
        Objects.requireNonNull(uId, "uId can't be null");

        // 当前所属周第一天毫秒数 - 系统发布时间毫秒数, 或者直接采用当前时间戳
        long diff = (DateTimeUtil.curWeekFirstDay().getTime() - benchmark) / 1000;

        return uId + diff;
    }
}