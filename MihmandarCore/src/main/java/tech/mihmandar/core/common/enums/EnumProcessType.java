package tech.mihmandar.core.common.enums;

/**
 * Created by Murat on 10/8/2017.
 */
public enum EnumProcessType {

    TRAINING("Eğitim", "Egitim"),
    LAB("Lab", "Lab"),
    EXAM("Sınav", "Sinav");

    private String processType;
    private String menuName;

    EnumProcessType(String processType, String menuName){
        this.processType = processType;
        this.menuName = menuName;
    }

    @Override
    public String toString() {
        return processType;
    }

}
