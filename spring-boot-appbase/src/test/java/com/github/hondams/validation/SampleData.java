package com.github.hondams.validation;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import com.github.hondams.appbase.validation.Correlation;
import lombok.Data;

@Data
@Correlation(SampleDataCorrelatioValidator.class)
public class SampleData {

    @NotBlank
    private String prop1;

    @Size(min = 1, max = 4)
    private String prop2;

    @Size(min = 1, max = 4)
    private List<String> prop3 = new ArrayList<>();

    @Valid
    private SampleSubData subData = new SampleSubData();

    @Data
    public class SampleSubData {

        @NotBlank
        private String subProp1;

        @Size(min = 1, max = 4)
        private String subProp2;
    }
}
