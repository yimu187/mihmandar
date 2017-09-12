package tech.mihmandar.utility.enums;

/**
 * Created by Murat on 9/11/2017.
 */
public enum  ErrorEnums {

    DOSYA_BULUNMADI("Dosya Bulunamadı"),
    DOSYA_OKUNURKEN_HATA_OLUSTU("Dosya Okunurken Hata Oluştu"),
    HATALI_SIFRE("Hatalı Şifre. Şifreyi Tekrar Giriniz");

    ErrorEnums(String type){
        this.type = type;
    }

    private String type;

    @Override
    public String toString() {
        return type;
    }
}
