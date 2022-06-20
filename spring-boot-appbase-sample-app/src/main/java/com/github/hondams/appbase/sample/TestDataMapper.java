package com.github.hondams.appbase.sample;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

// @Mapper(componentModel = "Spring")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TestDataMapper {


    // @Mapping(target = "prop1", source = "prop1")
    // @Mapping(target = "prop2", source = "prop1")
    //
    @Mapping(source = "prop2", target = "prop3")
    TestData2 toTestData2(TestData adata);
}
