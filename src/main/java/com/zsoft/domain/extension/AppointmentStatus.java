package com.zsoft.domain.extension;

public enum AppointmentStatus {
    PENDING ("Pending"),
    CANCELED ("Canceled"),
    PASSED ("Passed"),
    FREE ("Free"),
    BUSY ("Busy");

    private final String status;

    AppointmentStatus(String status) {
        this.status = status;
    }
}
