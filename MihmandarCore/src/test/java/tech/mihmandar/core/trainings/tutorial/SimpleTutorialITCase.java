package tech.mihmandar.core.trainings.tutorial;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tech.mihmandar.core.base.BaseITCase;
import tech.mihmandar.core.trainings.tutorial.dao.TutSubscriberDao;
import tech.mihmandar.core.trainings.tutorial.domain.TutSubscriber;
import tech.mihmandar.core.trainings.tutorial.enums.EnumTutAccountType;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by MURAT YILMAZ on 11/4/2016.
 */
public class SimpleTutorialITCase
//        extends BaseITCase
{

    @Autowired
    TutSubscriberDao tutSubscriberDao;

    @Test
    public void findAllSubscriber(){
        List<TutSubscriber> all = tutSubscriberDao.findAll();
        Assert.assertNotNull(all);
    }

    @Test
    public void saveTutSubscriber(){
        TutSubscriber tutSubscriber = new TutSubscriber();
        tutSubscriber.setAccountType(EnumTutAccountType.NORMAL);
        tutSubscriber.setEmail("yimu187@gmail.com");
        tutSubscriber.setUsername("yimu187");
        tutSubscriber.setPassword("yimu187");
        tutSubscriber = tutSubscriberDao.merge(tutSubscriber);
        Assert.assertNotNull(tutSubscriber.getId());
    }

    @Test
    public void extractTextFromPdf(){

        try {
            File file = new File("C:\\Temp\\Elastic Search Cook Book.pdf");
            PDDocument document = PDDocument.load(file);
            System.out.println(document.getNumberOfPages());

            PDFTextStripper reader = new PDFTextStripper();
            reader.setStartPage(1);
            reader.setEndPage(2);
            String pageText = reader.getText(document);
            System.out.println(pageText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
