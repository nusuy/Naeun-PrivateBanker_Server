package com.naeun.naeun_server.domain.Record.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewRecordReqDto {
    @NotNull(message = "Record file required.")
    private MultipartFile file;
     
    @NotBlank(message = "Title required.")
    private String title;

    @NotBlank(message = "Length required.")
    private String length;
}
