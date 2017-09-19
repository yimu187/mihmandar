package tech.mihmandar.utility.enums;

/**
 * Created by Murat on 9/11/2017.
 */
public enum  ErrorEnums {

    DOSYA_BULUNMADI("Dosya Bulunamadı"),
    DOSYA_OKUNURKEN_HATA_OLUSTU("Dosya Okunurken Hata Oluştu"),
    HATALI_SIFRE("Hatalı Şifre. Şifreyi Tekrar Giriniz"),
    ILLEGAL_CHARACTER_IN_BASE64_ENCODED_DATA("Length of Base64 encoded input string is not a multiple of 4."),
    LENGTH_OF_BASE64_ENCODED_INPUT_STRING_IS_NOT_A_MULTIPLE_OF_4("Length of Base64 encoded input string is not a multiple of 4.");

    ErrorEnums(String type){
        this.type = type;
    }

    private String type;

    @Override
    public String toString() {
        return type;
    }
}
