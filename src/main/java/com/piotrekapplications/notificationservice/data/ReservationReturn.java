package com.piotrekapplications.notificationservice.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationReturn {
    private Duration remindMinutesBefore;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime reservationUntil;

    private String resourceName;

    private String assignTo;

}
