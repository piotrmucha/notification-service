package com.piotrekapplications.notificationservice.web;

import com.piotrekapplications.notificationservice.services.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notification/")
@CrossOrigin(origins = "*")
public class NotificationController {
    @Autowired
    @Qualifier("confirmationPage")
    private String confirmationPage;
    private final UserAuthService userAuthService;

    public NotificationController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @PostMapping("userRegister")
    @ResponseStatus(HttpStatus.CREATED)
    public void handleRegister(@RequestParam("clientMail") String mail){
       userAuthService.sendAuthRequest(mail);
    }
    @GetMapping("userRegister/{uuid}")
    public String handleConfirmation(@PathVariable("uuid") UUID uuid){
        boolean result = userAuthService.handleConfirmation(uuid);
        if(result) {
            return confirmationPage;
        } else {
            return "no happy";
        }
    }
}
