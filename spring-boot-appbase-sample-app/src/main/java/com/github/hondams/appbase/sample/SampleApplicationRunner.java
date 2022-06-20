package com.github.hondams.appbase.sample;

import org.mapstruct.factory.Mappers;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SampleApplicationRunner implements ApplicationRunner {

    private final TestDataMapper mapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        TestData data = new TestData();
        data.setProp1("a");
        data.setProp2("b");
        TestData2 data2 = this.mapper.toTestData2(data);
        log.info("data={}", data);
        log.info("data2={}", data2);
    }
}
