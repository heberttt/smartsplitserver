package com.smartsplit.receiptparserservice.DTO;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TransformDataDTO {
    @NotEmpty
    private List<String> rec_texts;
}
