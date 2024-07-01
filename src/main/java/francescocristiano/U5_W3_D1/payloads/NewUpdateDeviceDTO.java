package francescocristiano.U5_W3_D1.payloads;

import jakarta.validation.constraints.NotBlank;

public record NewUpdateDeviceDTO(
        @NotBlank(message = "Device type cannot be blank")
        String deviceType,
        @NotBlank(message = "Device status cannot be blank")
        String deviceStatus) {
}
