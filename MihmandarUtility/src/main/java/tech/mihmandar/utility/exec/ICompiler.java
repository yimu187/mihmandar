package tech.mihmandar.utility.exec;

import tech.mihmandar.utility.dto.CompileResultDto;

/**
 * Created by Murat on 10/23/2017.
 */
public interface ICompiler {

    static CompileResultDto compile(String sourceCode){
        return new CompileResultDto();
    }

}
