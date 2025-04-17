/*
 * Copyright (c) 2022-present Charles7c Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.continew.admin.wework.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.digest.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Arrays;

/**
 * 企业微信消息加解密工具类
 * 
 * @author Charles7c
 * @since 2023/1/1 00:00
 */
@Slf4j
public class WXBizMsgCrypt {
    
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    
    private final String token;
    private final String encodingAesKey;
    private final String corpId;
    private final byte[] aesKey;
    
    /**
     * 构造函数
     * 
     * @param token          企业微信后台，开发者设置的token
     * @param encodingAesKey 企业微信后台，开发者设置的EncodingAESKey
     * @param corpId         企业的corpid
     */
    public WXBizMsgCrypt(String token, String encodingAesKey, String corpId) {
        this.token = token;
        this.encodingAesKey = encodingAesKey;
        this.corpId = corpId;
        this.aesKey = Base64.decode(encodingAesKey + "=");
    }
    
    /**
     * 验证URL函数
     * 
     * @param msgSignature 签名串，对应URL参数的msg_signature
     * @param timestamp    时间戳，对应URL参数的timestamp
     * @param nonce        随机串，对应URL参数的nonce
     * @param echoStr      随机串，对应URL参数的echostr
     * @return 解密之后的echostr
     */
    public String verifyUrl(String msgSignature, String timestamp, String nonce, String echoStr) {
        String signature = getSignature(token, timestamp, nonce, echoStr);
        if (!signature.equals(msgSignature)) {
            throw new RuntimeException("签名验证失败");
        }
        return decrypt(echoStr);
    }
    
    /**
     * 检验消息的真实性，并且获取解密后的明文
     * 
     * @param msgSignature 签名串，对应URL参数的msg_signature
     * @param timestamp    时间戳，对应URL参数的timestamp
     * @param nonce        随机串，对应URL参数的nonce
     * @param encryptMsg   密文，对应POST请求的数据
     * @return 解密后的原文
     */
    public String decryptMsg(String msgSignature, String timestamp, String nonce, String encryptMsg) {
        // 验证安全签名
        String signature = getSignature(token, timestamp, nonce, encryptMsg);
        if (!signature.equals(msgSignature)) {
            throw new RuntimeException("签名验证失败");
        }
        // 解密
        return decrypt(encryptMsg);
    }
    
    /**
     * 对明文进行加密
     * 
     * @param text 需要加密的明文
     * @return 加密后base64编码的字符串
     */
    public String encrypt(String text) {
        try {
            // 设置加密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            
            // 加密
            byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            
            // 使用BASE64对加密后的字符串进行编码
            return Base64.encode(encrypted);
        } catch (Exception e) {
            log.error("加密消息失败", e);
            throw new RuntimeException("加密消息失败");
        }
    }
    
    /**
     * 对密文进行解密
     * 
     * @param text 需要解密的密文
     * @return 解密得到的明文
     */
    public String decrypt(String text) {
        try {
            // 设置解密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            
            // 使用BASE64对密文进行解码
            byte[] encrypted = Base64.decode(text);
            
            // 解密
            byte[] original = cipher.doFinal(encrypted);
            String xmlContent = new String(original, CharsetUtil.CHARSET_UTF_8);
            
            // 验证企业微信的CorpID
            if (!xmlContent.contains(corpId)) {
                throw new RuntimeException("企业ID校验失败");
            }
            
            return xmlContent;
        } catch (Exception e) {
            log.error("解密消息失败", e);
            throw new RuntimeException("解密消息失败");
        }
    }
    
    /**
     * 用SHA1算法生成安全签名
     * 
     * @param token     票据
     * @param timestamp 时间戳
     * @param nonce     随机字符串
     * @param encrypt   密文
     * @return 安全签名
     */
    private String getSignature(String token, String timestamp, String nonce, String encrypt) {
        try {
            String[] array = new String[]{token, timestamp, nonce, encrypt};
            Arrays.sort(array);
            StringBuilder sb = new StringBuilder();
            for (String item : array) {
                sb.append(item);
            }
            return DigestUtil.sha1Hex(sb.toString());
        } catch (Exception e) {
            log.error("生成签名失败", e);
            throw new RuntimeException("生成签名失败");
        }
    }
}