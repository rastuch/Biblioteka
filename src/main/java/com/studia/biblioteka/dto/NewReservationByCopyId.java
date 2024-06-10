package com.studia.biblioteka.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NewReservationByCopyId {
    private Long userId;
    private Long copyId;

    public NewReservationByCopyId(Long userId, Long copyId) {
        this.userId = userId;
        this.copyId = copyId;
    }

    public NewReservationByCopyId() {
    }

}
