package com.naeun.naeun_server.domain.Record.dto.Gemini;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConvItemDto {
    private String content;
    private Long start_index;
    private Long end_index;
    private String reason;
}
