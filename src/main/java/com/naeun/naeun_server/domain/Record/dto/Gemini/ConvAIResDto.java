package com.naeun.naeun_server.domain.Record.dto.Gemini;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConvAIResDto {
    private ArrayList<ConvItemDto> result;
}
