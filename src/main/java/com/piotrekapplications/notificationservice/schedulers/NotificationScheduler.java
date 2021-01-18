package com.piotrekapplications.notificationservice.schedulers;


import com.piotrekapplications.notificationservice.data.ReservationReturn;
import com.piotrekapplications.notificationservice.services.GmailService;
import com.piotrekapplications.notificationservice.services.ResourceService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class NotificationScheduler {
    private final GmailService gmailService;
    private final ResourceService resourceService;
    private final String message = "Hi,\n\nWe remind you of your resource reservation: %s.\nReservation " +
            " time expires in %s minutes.\n\nBest regards.";
    private final Set<String> notificationSent = new HashSet<>();

    public NotificationScheduler(GmailService gmailService, ResourceService resourceService) {
        this.gmailService = gmailService;
        this.resourceService = resourceService;
    }

    @Scheduled(fixedRate = 30000)
    public void sendNotification() {
        System.out.println("start");
        List<ReservationReturn> reservations = resourceService.getAllReservations();

        if (!reservations.isEmpty()) {
            reservations = reservations.stream()
                    .filter(e -> e.getRemindMinutesBefore() != null)
                    .collect(Collectors.toList());
            for (ReservationReturn reservation : reservations) {
                String assignTO = reservation.getAssignTo();
                String resourceName = reservation.getResourceName();
                LocalDateTime reservationUntil = reservation.getReservationUntil();
                Duration remindBefore = reservation.getRemindMinutesBefore();
                LocalDateTime current = LocalDateTime.now();
                LocalDateTime currentPlusRemind = current.plusMinutes(remindBefore.toMinutes());
                if (currentPlusRemind.isAfter(reservationUntil) && current.isBefore(reservationUntil) && !notificationSent.contains(
                        assignTO + resourceName)) {
                    sendNotification(reservation);
                    boolean result = resourceService.setNotificationForReservationSent(resourceName, assignTO);
                    if (!result) {
                        notificationSent.add(reservation.getAssignTo() + reservation.getResourceName());
                    }
                }
            }
        }
    }

    private void sendNotification(ReservationReturn reservation) {
        try {
            gmailService.sendMessage(reservation.getAssignTo(),
                    "Booking reminder",
                    String.format(message,
                            reservation.getResourceName(), reservation.getRemindMinutesBefore().
                                    toMinutes()));
        } catch (MessagingException | IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }
}
