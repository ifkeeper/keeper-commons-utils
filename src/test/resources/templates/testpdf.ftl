<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title></title>
    <style type="text/css">

        @page {
            size: 297mm 210mm;
        }

        body {
            color: #58666e;
            position: relative;
        }

        * {
            font-family: 'SimSun';
        }

        h3 {
            text-align: center;
        }

        .all {
            width: 100%;
            border-collapse: collapse;
            border-spacing: 0;
        }

        .border td {
            border: 1px solid #dcdfe6;
        }

        .line_height32 {
            line-height: 32px !important;
        }

        .line_height44 {
            line-height: 44px !important;
        }

        .font-smaller {
            font-size: 16px;
        }

        .content td {
            height: 32px;
            font-size: 14px;
        }

        .border-left td:first-child {
            border-left: 1px solid #dcdfe6;
        }

        .border-right td:last-child {
            border-right: 1px solid #dcdfe6;
        }

        .border-bottom td {
            border-bottom: 1px solid #dcdfe6;
        }

        tr, td {
            font-size: 18px;
        }

        h4 {
            height: 0;
        }

        .watermark {
            position: absolute;
            width: 300px;
            height: 300px;
            bottom: 0;
            right: 20px;
            background: rgba(0, 0, 0, 0);
            border-radius: 150px;
        }

        .watermark > img {
            height: 300px;
            width: 300px;
        }
    </style>
</head>
<body>
<table class="all">
    <tr class="border">
        <td rowspan="1" colspan="20"><h3>车辆查勘报告</h3></td>
    </tr>
    <tr class="border line_height32">
        <td colspan="3" class="th">保单号</td>
        <td colspan="7">6520****************0000</td>
        <td colspan="3" class="th">报告时间</td>
        <td colspan="7">2019/10/10</td>
    </tr>
    <tr class="border line_height32">
        <td colspan="3" class="th">查勘时间</td>
        <td colspan="7">2019/10/08</td>
        <td colspan="3" class="th">查勘地点</td>
        <td colspan="7">上海市xxx区xxx路xxx号</td>
    </tr>
    <tr class="border">
        <td colspan="20"><h4>被保车辆信息</h4></td>
    </tr>
    <tr class="border line_height32">
        <td colspan="3" class="th">车架号</td>
        <td colspan="5">LDCC********6092</td>
        <td colspan="3" class="th">车牌</td>
        <td colspan="3">云DJW***</td>
        <td colspan="3" class="th">使用性质</td>
        <td colspan="3">非营运</td>
    </tr>
    <tr class="border line_height32">
        <td colspan="3" class="th">品牌</td>
        <td colspan="5">宝马</td>
        <td colspan="3" class="th">型号</td>
        <td colspan="3">X5</td>
        <td colspan="3" class="th">是否使用新能源</td>
        <td colspan="3">是</td>
    </tr>
    <tr class="border line_height32">
        <td colspan="3" class="th">驾驶员姓名</td>
        <td colspan="5">张三</td>
        <td colspan="3" class="th">性别</td>
        <td colspan="3">男</td>
        <td colspan="3" class="th">驾驶证号码</td>
        <td colspan="3">XXXXXXX</td>
    </tr>
    <tr class="border line_height32">
        <td colspan="3" class="th">联系方式</td>
        <td colspan="5">138xxxxxxxx</td>
        <td colspan="3" class="th">准驾车型</td>
        <td colspan="3">c1</td>
        <td colspan="3" class="th"></td>
        <td colspan="3"></td>
    </tr>
    <tr class="border">
        <td colspan="20"><h4>三者车辆信息</h4></td>
    </tr>
    <tr class="border line_height32">
        <td colspan="3" class="th">车架号</td>
        <td colspan="5">LDCC********6092</td>
        <td colspan="3" class="th">车牌</td>
        <td colspan="3">云DJW***</td>
        <td colspan="3" class="th">使用性质</td>
        <td colspan="3">非营运</td>
    </tr>
    <tr class="border line_height32">
        <td colspan="3" class="th">品牌</td>
        <td colspan="5">宝马</td>
        <td colspan="3" class="th">型号</td>
        <td colspan="3">X5</td>
        <td colspan="3" class="th">是否使用新能源</td>
        <td colspan="3">是</td>
    </tr>
    <tr class="border line_height32">
        <td colspan="3" class="th">驾驶员姓名</td>
        <td colspan="5">张三</td>
        <td colspan="3" class="th">性别</td>
        <td colspan="3">男</td>
        <td colspan="3" class="th">驾驶证号码</td>
        <td colspan="3">XXXXXXX</td>
    </tr>
    <tr class="border line_height32">
        <td colspan="3" class="th">联系方式</td>
        <td colspan="5">138xxxxxxxx</td>
        <td colspan="3" class="th">准驾车型</td>
        <td colspan="3">c1</td>
        <td colspan="3" class="th"></td>
        <td colspan="3"></td>
    </tr>
    <tr class="border">
        <td colspan="20"><h4>事故信息</h4></td>
    </tr>
    <tr class="border line_height44">
        <td class="th">出险原因</td>
        <td colspan="19" class="font-smaller">
            <input type="checkbox"/>碰撞
            <input type="checkbox"/>倾覆
            <input type="checkbox"/>落水
            <input type="checkbox"/>火灾
            <input type="checkbox"/>爆炸
            <input type="checkbox"/>自然
            <input type="checkbox"/>外界物体倒塌
            <input type="checkbox"/>外界物体坠落
            <input type="checkbox"/>雷击
            <input type="checkbox"/>暴风
            <input type="checkbox"/>暴雨
            <input type="checkbox"/>洪水
            <input type="checkbox"/>车身划痕
            <input type="checkbox"/>玻璃单独破碎
            <input type="checkbox" value="1"/>测试
            <input type="checkbox"
                   checked="checked"/>其他（飞天）
        </td>
    </tr>
    <tr class="border line_height44">
        <td class="th">事故类型</td>
        <td colspan="19" class="font-smaller">
            <input type="checkbox" checked="checked"/>单方肇事
            <input type="checkbox"/>双方事故
            <input type="checkbox"/>多方事故
            <input type="checkbox"/>仅涉及财产损失
            <input type="checkbox"/>涉及人员伤亡
        </td>
    </tr>
    <tr class="border line_height44">
        <td class="th">处理方式</td>
        <td colspan="19" class="font-smaller">
            <input type="checkbox" checked="checked"/>交警
            <input type="checkbox"/>自行协商
            <input type="checkbox"/>保险公司
        </td>
    </tr>
    <tr class="border line_height44">
        <td class="th">预计事故责任划分</td>
        <td colspan="19" class="font-smaller">
            <input type="checkbox" checked="checked"/>全部
            <input type="checkbox"/>主要
            <input type="checkbox"/>同等
            <input type="checkbox"/>次要
            <input type="checkbox"/>无责
        </td>
    </tr>
    <tr class="border">
        <td colspan="20"><h4>车辆情况</h4></td>
    </tr>
    <tr class="border line_height44">
        <td class="th">车辆情况</td>
        <td colspan="19" class="font-smaller">
            <input type="checkbox"/>未上牌
            <input type="checkbox"/>车轮单独损坏
            <input type="checkbox"/>新增非原厂设备
            <input type="checkbox"/>划痕
            <input type="checkbox"/>凹陷
            <input type="checkbox"/>断裂
            <input type="checkbox" checked="checked"/>其他（两半
            ）
        </td>
    </tr>
    <tr class="border line_height44">
        <td class="th">车辆损伤部位</td>
        <td colspan="19" class="font-smaller">
            <input type="checkbox" checked="checked"/>发动机罩
            <input type="checkbox"/>前保险杠
            <input type="checkbox"/>后保险杆
            <input type="checkbox"/>左边叶子板
            <input type="checkbox"/>右边叶子板
            <input type="checkbox"/>行李箱盖
            <input type="checkbox"/>左边门
            <input type="checkbox"/>右边门
            <input type="checkbox"/>左下边梁
            <input type="checkbox"/>右下边梁
            <input type="checkbox"/>车顶盖
            <input type="checkbox"/>轮胎
            <input type="checkbox"/>玻璃
            <input type="checkbox"/>前大灯
            <input type="checkbox"/>尾灯
        </td>
    </tr>
    <tr class="border">
        <td colspan="20"><h4>查勘信息</h4></td>
    </tr>
    <tr class="content border-left border-right">
        <td colspan="18" class="th">被保险机动车驾驶人是否具有有效驾驶证</td>
        <td><input type="checkbox" checked="checked"/>是</td>
        <td><input type="checkbox"/>否</td>
    </tr>
    <tr class="content border-left border-right">
        <td colspan="18" class="th">被保险机动车驾驶人准驾车型与实际驾驶车辆是否相符</td>
        <td><input type="checkbox"/>是</td>
        <td><input type="checkbox" checked="checked"/>否</td>
    </tr>
    <tr class="content border-left border-right">
        <td colspan="18" class="th">被保险机动车驾驶人是否为酒后或醉酒驾车</td>
        <td><input type="checkbox" checked="checked"/>是</td>
        <td><input type="checkbox"/>否</td>
    </tr>
    <tr class="content border-left border-right">
        <td colspan="18" class="th">被保险机动车发生时候时的肇事人是否为保险合同约定的驾驶人员</td>
        <td><input type="checkbox"/>是</td>
        <td><input type="checkbox" checked="checked"/>否</td>
    </tr>
    <tr class="content border-left border-right">
        <td colspan="18" class="th" style="border-bottom: 1px solid #dcdfe6;">使用各种专用机械车，特种车的人员是否由国家有关部门核发的有效驾驶证</td>
        <td><input type="checkbox" style="border-bottom: 1px solid #dcdfe6;" checked="checked"/>是</td>
        <td><input type="checkbox" style="border-bottom: 1px solid #dcdfe6;"/>否</td>
    </tr>
</table>
<div class="watermark">
    <img src="https://test-one-road.oss-cn-hangzhou.aliyuncs.com/SECURITY/transparent.jpg"/>
</div>
</body>
</html>