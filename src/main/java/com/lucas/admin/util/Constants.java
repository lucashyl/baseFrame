package com.lucas.admin.util;

public class Constants {
	/**
	 * shiro采用加密算法
	 */
	public static final String HASH_ALGORITHM = "SHA-1";
	/**
	 * 生成Hash值的迭代次数 
	 */
	public static final int HASH_INTERATIONS = 1024;
	/**
	 * 生成盐的长度
	 */
	public static final int SALT_SIZE = 8;

	/**
	 * 验证码
	 */
	public static final String VALIDATE_CODE = "validateCode";

	/**
	 *系统用户默认密码
	 */
	public static final String DEFAULT_PASSWORD = "123456";

	/**
	 * 定时任务状态:正常
	 */
	public static final Integer QUARTZ_STATUS_NOMAL = 0;
	/**
	 * 定时任务状态:暂停
	 */
	public static final Integer QUARTZ_STATUS_PUSH = 1;

	/**
	 * 评论类型：1文章评论
	 */
	public static final Integer COMMENT_TYPE_ARTICLE_COMMENT = 1;
	/**
	 * 评论类型：2.系统留言
	 */
	public static final Integer COMMENT_TYPE_LEVING_A_MESSAGE = 2;


	public final static String CONTEXT_USER_ID="contextUserId";
	public final static String CONTEXT_NAME="contextName";
	public final static String JWT_PRIVATE_KEY ="shop";
	public final static String RENEWAL_TIME =  "renewalTime";
	public final static String TOKEN = "token";

	public final static int LOGIN_EXPIRE_TIME_JWT = 60*60*1000; //登录缓存JWT时间 毫秒值,

	public final static int LOGIN_EXPIRE_TIME_REDIS = 60*60; //登录缓存redis时间 秒,

	public final static int REDIS_TIME_OUT_MS = 1000;//redis 分布式锁获取超时时间 毫秒

	public final static int REDIS_LOCK_EXPIRE = 5000;//redis 分布式锁过期时间  毫秒

	public final static int REDIS_LOCK = 10*1000; //redis锁时间

	public final static int REDIS_LOGIN_VALIDATE_CODE_TIME = 5*60 ; //手机验证码时间, 秒

}
