package tech.mihmandar.core.trainings.tutorial.enums;

/**
 * Created by MURAT YILMAZ on 10/31/2016.
 */
public enum EnumTutAccountType {


    NORMAL("Normal Registration"),
    GOOGLE("Registration Over Google");


    private String type;

    EnumTutAccountType(String type) {
		this.type = type;
    }

}
