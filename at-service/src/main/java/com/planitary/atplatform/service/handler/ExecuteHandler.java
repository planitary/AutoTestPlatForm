package com.planitary.atplatform.service.handler;

import com.planitary.atplatform.model.dto.ExecuteDTO;
import com.planitary.atplatform.model.dto.ExecuteResponseDTO;

public interface ExecuteHandler {

    public ExecuteResponseDTO doInterfaceExecutor(ExecuteDTO executeDTO);
}
