package tech.mihamdar.ui.presentation.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Murat on 3/26/2017.
 */
public class QrCodeTest {

    @Test
    public void createQrCode() throws IOException, WriterException {
        String qrCodeData = "Hello World!";
        String filePath = "QRCode.png";
        String charset = "UTF-8";
        Map hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset),
                BarcodeFormat.QR_CODE, 200, 200, hintMap);

        File qrCode = File.createTempFile("QrCode", ".png");
        String path = qrCode.getPath();
        System.out.println(path);

        MatrixToImageWriter.writeToPath(matrix, filePath.substring(filePath.lastIndexOf('.') + 1), Paths.get(path));
    }

    @Test
    public void readQrCode() throws NotFoundException, IOException {
        Map hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        String filePath = "C:\\Users\\Murat\\AppData\\Local\\Temp\\QrCode1426515283312783896.png";

        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(
                        ImageIO.read(new FileInputStream(filePath)))));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,hintMap);
        System.out.println(qrCodeResult.getText());
    }
}
