package tech.mihmandar.core.common.enums;

/**
 * Created by Murat on 9/11/2017.
 */
public enum EnumGender {

    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    private String gender;

    EnumGender(String gender){
        this.gender = gender;
    }


    @Override
    public String toString() {
        return gender;
    }
}
