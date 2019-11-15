package com.chenay.web;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class WebApplicationTests {

    @Test
    public void contextLoads() {
		String cf = null;
		try {
			cf = new File("").getCanonicalPath();
			String fileName = cf + File.separator+ "eFactory WebMobile_20190619_v1.xlsx";
            fileName = "D:\\y.chen5\\IdeaProject\\chenayPlatform\\web\\src\\test\\java\\com\\chenay\\web\\eFactory WebMobile_20190619_v1.xlsx";
// 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
			EasyExcel.read(fileName, DemoData.class, new DemoDataListener())
                    .sheet()
                    .doRead();
		} catch (IOException e) {
			e.printStackTrace();
		}

    }


    @Data
    public class DemoData {
        private String string;
        private Date date;
        private Double doubleData;
    }

    // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
    public static class DemoDataListener extends AnalysisEventListener<DemoData> {
        private static final Logger LOGGER = LoggerFactory.getLogger(DemoDataListener.class);
        /**
         * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
         */
        private static final int BATCH_COUNT = 5;
        List<DemoData> list = new ArrayList<DemoData>();

        @Override
        public void invoke(DemoData data, AnalysisContext context) {
            LOGGER.info("解析到一条数据:{}", JSON.toJSONString(data));
            list.add(data);
            if (list.size() >= BATCH_COUNT) {
                saveData();
                list.clear();
            }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            saveData();
            LOGGER.info("所有数据解析完成！");
        }

        /**
         * 加上存储数据库
         */
        private void saveData() {
            LOGGER.info("{}条数据，开始存储数据库！", list.size());
            LOGGER.info("存储数据库成功！");
        }
    }
}
