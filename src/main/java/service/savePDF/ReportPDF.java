package service.savePDF;

import domain.DTO;
import domain.Message;
import domain.User;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import service.MainService;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.time.LocalDate;


public class ReportPDF {
    private final MainService mainService;

    public ReportPDF(MainService mainService) {
        this.mainService = mainService;
    }

    public void createPDFforMessages(User user, User userFriend, LocalDate firstDate, LocalDate secondDate) {
        try (PDDocument document = new PDDocument()) {
            PDPage messageByFriendPage = new PDPage();
            document.addPage(messageByFriendPage);

            PDPageContentStream pageContentStreamM = new PDPageContentStream(document, messageByFriendPage);
            writeTitle(pageContentStreamM, "Messages send to you by " + userFriend.getFirstName() + " " + userFriend.getLastName());

            pageContentStreamM.beginText();
            pageContentStreamM.setFont(PDType1Font.TIMES_ROMAN, 12);
            pageContentStreamM.newLineAtOffset(100, 650);
            for (Message message : mainService.getMessageToUserBySpecificFriend(user, userFriend, firstDate, secondDate)
            ) {
                pageContentStreamM.showText("On " + message.getData().toLocalDate().toString() + " they sent you: " + message.getMessage());
                pageContentStreamM.newLineAtOffset(0, -15);
            }
            pageContentStreamM.endText();

            pageContentStreamM.close();

            document.save("D:\\Messages.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void createPDFforActivities(User user, LocalDate firstDate, LocalDate secondDate) {
        try (PDDocument document = new PDDocument()) {
            PDPage friendsPage = new PDPage();
            document.addPage(friendsPage);

            PDPageContentStream pageContentStream = new PDPageContentStream(document, friendsPage);
            writeTitle(pageContentStream, "New friends");

            pageContentStream.beginText();
            pageContentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            pageContentStream.newLineAtOffset(100, 650);
            for (DTO friendship : mainService.getUserFriendsByMonth(user.getId(), firstDate, secondDate)
            ) {
                pageContentStream.showText("You are friends with " + friendship.getFirstNameDto() + " " + friendship.getLastNameDto() + " since " + friendship.getDateDto().toLocalDate().toString());
                pageContentStream.newLineAtOffset(0, -15);
            }
            pageContentStream.endText();

            PDPage messagesPage = new PDPage();
            document.addPage(messagesPage);

            PDPageContentStream pageMContentStream = new PDPageContentStream(document, messagesPage);
            writeTitle(pageMContentStream, "Messages send to you");

            pageMContentStream.beginText();
            pageMContentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            pageMContentStream.newLineAtOffset(100, 650);
            for (Message message : mainService.getMessageToUser(user, firstDate, secondDate)
            ) {
                pageMContentStream.showText(message.getFrom().getFirstName() + " " + message.getFrom().getLastName() + " wrote to you: " + message.getMessage() + " on " + message.getData().toLocalDate().toString());
                pageMContentStream.newLineAtOffset(0, -15);
            }
            pageMContentStream.endText();

            pageContentStream.close();
            pageMContentStream.close();

            document.save("D:\\Activities.pdf");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void writeTitle(PDPageContentStream pd, String title) throws IOException {
        pd.beginText();
        pd.setFont(PDType1Font.TIMES_ROMAN, 16);
        pd.newLineAtOffset(200, 700);
        pd.showText(title);
        pd.endText();
    }

}
