package tech.mihmandar.utility.runner;

import tech.mihmandar.utility.dto.RunResultDto;

/**
 * Created by Murat on 10/28/2017.
 */
public interface IRunner {

    static RunResultDto run(String fileName, String sourceCode){
        return new RunResultDto();
    }

}
