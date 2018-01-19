package com.fast.boot.fastpoi;

import static com.google.common.io.Resources.getResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.fast.boot.fastpoi.bo.CustomerIno;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

/**
 * @author: junqing.li
 * @date: 18/1/14
 */
public class ExcelHelperTest {

    @Test
    public void readListMapTest() throws IOException {

        InputStream inputStream = getResource("easyexcel-test.xlsx").openStream();

        List<Map<String, String>> maps = ExcelHelper.read(inputStream);

        System.out.println(maps);

    }

    @Test
    public void readClassTest() throws IOException {

        InputStream inputStream = getResource("easyexcel-test.xlsx").openStream();

        List<CustomerIno> list = ExcelHelper.read(inputStream, CustomerIno.class);

        System.out.println(list);

    }

    @Test
    public void writeClassTest() throws IOException {

        List<CustomerIno> list = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {

            CustomerIno customerIno = new CustomerIno();
            customerIno.setCreditAmount(100.01 + i);
            customerIno.setCustomerId(1000000L + i);
            customerIno.setCustomerName("测试-" + i);
            customerIno.setIdNo("18908034455566666" + i);
            customerIno.setTerms(1 + i);
            customerIno.setPhone("19890880" + 1);
            list.add(customerIno);
        }

        byte[] bytes = ExcelHelper.write(list, CustomerIno.class);

        Files.write(bytes, new File("/Users/iluoxuan/Desktop/test-create-excel.xlsx"));
    }
}
