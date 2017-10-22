package tech.mihmandar.utility.compiler;

import tech.mihmandar.utility.dto.CompileResultDto;

/**
 * Created by Murat on 10/23/2017.
 */
public interface ICompile {

    static CompileResultDto compile(String sourceCode){
        return new CompileResultDto();
    }

}
