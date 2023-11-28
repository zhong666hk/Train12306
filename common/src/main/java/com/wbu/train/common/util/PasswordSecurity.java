package com.wbu.train.common.util;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serial;
import java.io.Serializable;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;

public class PasswordSecurity {
    private static String algorithm = "RSA"; // 非对称的算法
    private static String randomAlgorithm = "DES"; // 对称的算法
    private static PrivateKey sendPrivateKey;
    private static PublicKey sendPublicKey;
    private static PublicKey receivePublicKey;
    private static PrivateKey receivePrivateKey;


    /**
     * @param message 发送的消息
     * @return 返回要发送加密后的消息
     */
    public static String send(String message) throws Exception {
        // 1.发送方将原文-->hash-->发送方私钥钥加密
        String md5MessageString = SecureUtil.md5(message);
        System.out.println("md5加密后\t" + md5MessageString);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, sendPrivateKey);
        // 对hash值加密
        byte[] bytes = cipher.doFinal(md5MessageString.getBytes());
        // 转码
        String encode = Base64.encode(bytes);
        // 获取发送的公钥
        byte[] encoded = sendPublicKey.getEncoded();
        String sendPublicKeyString = Base64.encode(encoded);
        SendMessage sendMessage = new SendMessage(message, encode, sendPublicKeyString);
        // 随机对称的密钥
        String randomKey = RandomUtil.randomString(8);
        System.out.println("生成的随机密钥为=\t" + randomKey);

        Cipher cipher1 = Cipher.getInstance(randomAlgorithm);
        // 创建加密规则 1.表示key的字节 2.表示加密的类型
        SecretKeySpec secretKeySpec = new SecretKeySpec(randomKey.getBytes(), randomAlgorithm);
        cipher1.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        // 转为json字符串
        String jsonStr = JSONUtil.toJsonStr(sendMessage);
        byte[] sendMessageBytes = cipher1.doFinal(jsonStr.getBytes());
        // 打包的密文信息--sendMessage
        String sendMessageBodyEncode = Base64.encode(sendMessageBytes);

        // 再用接收方的公钥加密随机的对称密钥  对称加密封装
        Cipher cipher2 = Cipher.getInstance(algorithm);
        cipher2.init(Cipher.ENCRYPT_MODE, receivePublicKey);
        // 随机密钥的打包
        byte[] randomKeyByte = cipher2.doFinal(randomKey.getBytes());

        // 封装随机密钥和信息体
        String randomKeyString = Base64.encode(randomKeyByte);
        SendMessageFinal sendMessageFinal = new SendMessageFinal(sendMessageBodyEncode, randomKeyString);
        return JSONUtil.toJsonStr(sendMessageFinal);
    }


    public static void receive(String message) throws Exception {
        // 收到消息-->解密为 SendMessageFinal
        SendMessageFinal sendMessageFinal = JSONUtil.toBean(message, SendMessageFinal.class);
        String randomKeyString = sendMessageFinal.getRandomKeyString();
        // base64解析
        byte[] randomKeyByte = Base64.decode(randomKeyString);
        //1.接收私钥解开随机对称密钥
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, receivePrivateKey);
        byte[] bytes = cipher.doFinal(randomKeyByte);
        String randomKey = new String(bytes);
        System.out.println("接收的随机密钥=\t" + randomKey);
        //2. 随机密钥解开消息体
        String receiveMessageBodyEncode = sendMessageFinal.getMessageBodyEncode();
        byte[] receiveMessageBodyByte = Base64.decode(receiveMessageBodyEncode.getBytes());
        Cipher cipher1 = Cipher.getInstance(randomAlgorithm);
        SecretKeySpec secretKeySpec = new SecretKeySpec(randomKey.getBytes(), randomAlgorithm);
        cipher1.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] messageBodyByte = cipher1.doFinal(receiveMessageBodyByte);
        // 3.解密后转化类型
        SendMessage sendMessage = JSONUtil.toBean(new String(messageBodyByte), SendMessage.class);
        // 获取发送方的公钥
        String sendPublicKeyString = sendMessage.getSendPublicKeyString();
        // 获取 接收的消息
        String receiveMessage = sendMessage.getMessage();
        // 获取消息的 hash值
        String receiveEncode = sendMessage.getEncode();
        System.out.println("获取到的消息为\t" + receiveMessage);
        //4.将接收的消息和 解密后的签名比较。判断是否被修改
        String receiveMessageMD5 = SecureUtil.md5(receiveMessage);
        // 签名解码
        byte[] receiveDecoded = Base64.decode(receiveEncode);
        Cipher cipher2 = Cipher.getInstance(algorithm);
        //创建公钥
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(sendPublicKeyString));
        PublicKey sendPublicKey = keyFactory.generatePublic(keySpec);
        cipher2.init(Cipher.DECRYPT_MODE, sendPublicKey);
        byte[] receiveDECRYPTMD5Byte = cipher2.doFinal(receiveDecoded);
        String receiveDECRYPTMD5 = new String(receiveDECRYPTMD5Byte);
        //是否相等的hash
        if (receiveDECRYPTMD5.equals(receiveMessageMD5)) {
            System.out.println("内容签名一样，未被修改");
        } else {
            System.out.println("消息已经泄露");
        }
    }

    public static void main(String[] args) throws Exception {
        generatorPairKey(algorithm);
        String sendMessage = send("钟");
        receive(sendMessage);
    }


    /**
     * 生成两对公私钥
     */
    public static void generatorPairKey(String algorithm) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        sendPublicKey = keyPair.getPublic();
        sendPrivateKey = keyPair.getPrivate();

        KeyPairGenerator keyPairGenerator2 = KeyPairGenerator.getInstance(algorithm);
        KeyPair keyPair2 = keyPairGenerator2.generateKeyPair();
        receivePublicKey = keyPair2.getPublic();
        receivePrivateKey = keyPair2.getPrivate();
    }
}

/**
 * 数字信封体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class SendMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String message; // 发送的消息
    private String encode; // 消息MD5后的hash值
    private String sendPublicKeyString; // 发送方的公钥
}

/**
 * 最终的 数字信封
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class SendMessageFinal implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private String MessageBodyEncode;// 消息体
    private String randomKeyString;// 随机密钥
}
