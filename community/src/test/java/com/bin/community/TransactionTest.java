package com.bin.community;

import com.bin.TestPackage.TransactionServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransactionTest {
    @Autowired
    private TransactionServiceTest transactionServiceTest;
    @Test
    public void TransactionTest1(){
        System.out.println(transactionServiceTest.save1());
    }
}
