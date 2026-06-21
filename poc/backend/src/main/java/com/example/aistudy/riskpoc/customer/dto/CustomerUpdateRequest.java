package com.example.aistudy.riskpoc.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerUpdateRequest(
        @NotBlank @Size(max = 80) String customerNameSample,
        @NotBlank @Size(max = 16) String customerType,
        @NotBlank @Size(max = 32) String mobileMasked,
        @NotBlank @Size(max = 64) String idNoMasked,
        @NotBlank @Size(max = 16) String status,
        @Size(max = 200) String remarkSample,
        @NotNull Integer version) {
}
