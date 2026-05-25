package com.Ishwarjit.Wolf_OVRN_backend.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInquiriesGroupResponse {
    private final UserResponse user;
    private final List<InquiryResponse> inquiries;
}
