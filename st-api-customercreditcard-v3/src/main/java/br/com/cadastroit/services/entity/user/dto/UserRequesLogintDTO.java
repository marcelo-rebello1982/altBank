package br.com.cadastroit.services.entity.user.dto;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserRequesLogintDTO {

    private String userName;
    private String passwordUser;
}