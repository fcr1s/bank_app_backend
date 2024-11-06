package com.cris.bank_app_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BankAppBackendApplicationTests {

    @Test
    void contextLoads() {
        String dbHost = System.getenv("DB_HOST");
        System.out.println("DB_HOST: " + dbHost); // Verifica que se esté leyendo correctamente
    }

}
