package com.xiangkai.community.errorcode;

public enum ErrorCode implements BaseErrorCode {

    SUCCESS(0, "请求成功"),
    UNKNOWN_ERROR(-1, "未知错误"),
    SYSTEM_INTERNAL_ERROR(-2, "系统内部错误"),

    USER_NOT_FOUND(10001, "没有找到此用户"),
    USERNAME_NOT_BLANK(10002, "用户名不能为空"),
    USERNAME_EXIST(10003, "用户名已经存在"),
    USERTYPE_ERROR(100031, "用户类型不正确"),
    USER_UNLOGIN(100032, "用户未登录"),
    USER_NO_AUTHORITY(100033, "用户无权限"),
    PHONE_WROND(10004, "手机号不正确"),
    SMS_CODE_ERROR(10007, "手机验证码不正确"),
    PHONE_EXIST(10008, "手机号已经存在"),
    USER_EMPTY_EXCEPTION(10009, "用户名、手机号或者邮箱为空"),
    USER_TOKEN_EXCEPTION(10010, "从TOKEN中未查到相关用户信息"),
    USERNAME_PASSWORD_EXCEPTION(10011, "用户名或者密码错误"),

    EMAIL_SERVER_ECCEPTION(10012, "阿里云邮件服务端出错"),
    EMAIL_CLIENT_ECCEPTION(10013, "阿里云邮件客户端出错"),
    EMAIL_SEND_ECCEPTION(10014, "阿里云邮件发送出错"),
    EMAIL_WROND(10015, "邮箱不正确"),
    EMAIL_CODE_WROND(10016, "邮箱验证码不正确"),
    EMAIL_EXIST(10017, "邮箱已经存在"),

    LOGIN_METHOD_WROND(10018, "登录方式不正确"),
    CODE_EMPTY(10019, "验证码不为空"),
    PASSWORD_EMPTY(10020, "密码不为空"),
    TOKEN_EXCEPTION(10021, "TOKEN认证出错"),

    USER_AUTH_FAILD(10022, "用户认证失败"),

    USER_ACCESS_DENIED(10023, "用户无权限登录"),

    CODE_SEND_FAILD(10030, "验证码发送失败"),

    ACTION_MONGODB_ERROR(10100, "操作MONGODB数据库出错"),
    OPERATION_TOO_FREQUENT(10101, "请求过于频繁，请稍候再试"),

    GOLD_COINS_INSUFFICIENT(10025,"金币余额不足"),
    CODE_EXIST(10023,"编号已存在"),
    TESTCODE_EXIST(10033,"答题码已存在"),
    TESTCODE_NOEXIST(10034,"答题码不存在"),
    TESTCODE_ERROR(10036,"提交数已达上限"),
    TESTCENTER_NOEXIST(10035,"测试题不存在"),
    NAME_EXIST(10024,"名称已存在"),
    NOT_EXIST_EMAIL(10025,"该企业用户没有分配邮箱"),
    MAIL_REACH_MAX(10026,"达到收取邮箱上限"),
    MAIL_NEW_NOTEXIST(10027,"邮箱中没有可导入的简历"),
    PWD_CONFIRM_ERROR(10029,"两次密码不一致"),
    PWD_ERROR(10030,"密码不正确"),
    ENTER_OR_TALENT_NOT_EXITS(10028,"企业或人才库简历不存在"),
    PHONE_EMPTY(10031, "手机号不能为空"),
    EMAIL_EMPTY(10032, "邮箱不能为空"),
    NO_USABLE_MAIL(10040,"没有可用邮箱"),

    REQUESTRECORD_EXIST(10050,"简历已投递"),
    TALENTRESERVER_EXIST(10051,"简历已存在于人才储备库中，请勿重复添加"),

    DEVICE_ID_EMPTY(10052,"设备ID:deviceId不能为空"),
    DELETE_CONNECT_ERROR(10053,"删除connect出错");

    private Integer code;

    private String message;


    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Result<Object> build() {
        return new Result<>(code, message);
    }
}
