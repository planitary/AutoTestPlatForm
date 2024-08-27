package com.planitary.atplatform.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddTCSProgressDTO {
    private List<AddTCSProgressInfoDTO> progressList;
    private AddProjectDTO projectInfo;
    private String setId;

}
