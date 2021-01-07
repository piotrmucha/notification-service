package com.piotrekapplications.notificationservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.UUID;

@Service
public class UserAuthService {
    private final GmailService gmailService;
    private final HashMap<UUID, String> userMap = new HashMap<>();
    private final String authMessages = "Dzień dobry,\n\ndziękujemy za założenie konta w serwise reservation tool.\n" +
            "Żeby potwierdzić konto kliknij w link: http://www.localhost:8050/api/v1/notification/userRegister/%s\n\n Pozdrawiamy.";


    @Autowired
    private UserService userService;
    public UserAuthService(GmailService gmailService) {
        this.gmailService = gmailService;
    }

    public void sendAuthRequest(String userMail) {
        UUID uuid = UUID.randomUUID();
        try {
            gmailService.sendMessage(userMail,"Założenie konta w reservation-tool",String.format(authMessages,uuid));
            userMap.put(uuid,userMail);
        } catch (MessagingException | IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    public boolean handleConfirmation(UUID uuid) {
        if( userMap.containsKey(uuid)) {
            String mail = userMap.get(uuid);
            return userService.confirmUser(mail);
        }
        return false;
    }
}
