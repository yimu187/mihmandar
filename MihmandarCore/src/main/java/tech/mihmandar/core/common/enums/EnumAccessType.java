package tech.mihmandar.core.common.enums;

/**
 * Created by Murat on 10/8/2017.
 */
public enum EnumAccessType {
    PUBLIC("Genel"),
    PRIVATE("Özel"),
    SECRET("Gizli");

    private String type;

    EnumAccessType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
