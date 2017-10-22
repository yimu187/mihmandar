package tech.mihmandar.core.common.enums;

/**
 * Created by Murat on 10/8/2017.
 */
public enum EnumSoftwareLanguages {

    JAVA("Java"),
    Python("Python");

    private String name;

    EnumSoftwareLanguages(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
