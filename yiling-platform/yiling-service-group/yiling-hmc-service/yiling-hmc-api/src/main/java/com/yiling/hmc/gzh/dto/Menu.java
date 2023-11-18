package com.yiling.hmc.gzh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class Menu {


    /**
     * button
     */
    private List<ButtonDTO> button;
}
