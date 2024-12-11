package br.com.cadastroit.services.entity.user.dto;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserRequestDTO {
    private Long id;
    private String userName;
    private String passwordUser;
    private String email;
    private List<RoleDTO> roles;
}