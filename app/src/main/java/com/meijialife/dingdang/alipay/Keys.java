package com.meijialife.dingdang.alipay;

/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 *
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */
//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	// 合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088911921151260";

	// 收款支付宝账号
	public static final String DEFAULT_SELLER = "info@jia-he-jia.com";

	// 商户私钥，自助生成
	public static final String PRIVATE = "MIICXQIBAAKBgQCok5VbhRvDGlKOuiZtciiVgql65DZk/MwMJVxeMmu/lrRE2lT3d7wxLdxr3CVn2imQkWRTHKPuhR/nrLwewVujJUGI214lw8oFdhzYnxKHkRlbCKnf6rMXB9UOdiOM+iNCSL3uEeoC5hxSJtLu1P0nZ3G4jA8vSTQJjQ3LBI3eZwIDAQABAoGAeeFSUDwZyWLDo8u0stahvw8cJ+zN4w/fGzz73mMIAhoctddBLTaVit0Ei7s8FCzodkyYQ3rmjzIS0jbSQE4+SLZLFmAeq184y1ibW/MWQe2csD08up9OadNoqHAOvlc4+Fdxp7HT4e81hnbo4Krt3m+6+pBiUQSmOE5G/9N2qUkCQQDdZwwmkl1xxLOzaRQs+tzkIH+bzcV7y49N2gh5ysqLlBrw6MhDVgvQooCDrVl1Ph7P/6Vqg+A/l+27K05icXjTAkEAwutKpJfEa5Bxfow3S6wR+kFsNZmtQ7FZshyLBn3+Shrd12cx5rLDJuAv6dnl8PscStOWg4/kvk9YoB+aQRMHnQJBANKH/a7j+UWIxVRyNEQ9XDT1jkKqngKE48Uk8nBV3NpzQFcbE+ur89o13DzaZzmsk4I4V6R8uI3gO6iCmqvTuQ8CQAqFzUfJca8k4xuj0jBhn67U7VSPcU0gO+ljpRW6o4m4nZVNWuJLcEGfBLcpLkKuK77WYV/E2uJzQiOS0S8GKfkCQQC80AbljK3nVXJVF0UgHbAhLNxsOaZcul4eIIq69IYg39WJYniFaz5VvaOa0Fip2FeB8Z01ORyY+Fbzjz5NvxRy";
	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
}
