package com.anjia.unidbgserver.service;

import com.anjia.unidbgserver.config.UnidbgProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 单院测试
 *
 * @author AnJia
 * @since 2021-08-02 16:31
 */
@Slf4j
@RunWith(SpringRunner.class)
class TTEncryptTest {

    TTEncrypt ttEncrypt;

    @BeforeEach
    public void initTtEncrypt() {
        UnidbgProperties properties = new UnidbgProperties();
        properties.setDynarmic(false);
        properties.setVerbose(true);
        ttEncrypt = new TTEncrypt(properties);
    }

    @SneakyThrows @Test
    void getMtgsig() {
        byte[] data = ttEncrypt.ttEncrypt();
        log.info(new String(data));
    }
}