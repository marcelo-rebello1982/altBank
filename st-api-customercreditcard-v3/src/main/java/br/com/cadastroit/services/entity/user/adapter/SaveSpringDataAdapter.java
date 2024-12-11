package br.com.cadastroit.services.entity.user.adapter;
import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.cadastroit.services.entity.user.dto.UserRequestDTO;
import br.com.cadastroit.services.entity.user.dto.UserResponseDTO;
import br.com.cadastroit.services.entity.user.model.UserModel;
import br.com.cadastroit.services.entity.user.port.SavePort;
import br.com.cadastroit.services.entity.user.repository.UserModelRepository;

@Component
public class SaveSpringDataAdapter implements SavePort<UserRequestDTO, UserResponseDTO> {

    private final ModelMapper mapper;

    private final UserModelRepository userModelRepositoryr;

    public SaveSpringDataAdapter(ModelMapper mapper, UserModelRepository userModelRepositoryr) {
        this.mapper = mapper;
        this.userModelRepositoryr = userModelRepositoryr;
    }

    @Override
    @Transactional(readOnly = false)
    public UserResponseDTO execute(UserRequestDTO userRequestDTO) {
        UserModel user = this.mapper.map(userRequestDTO, UserModel.class); 
        user.setPasswordUser(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setCreateAt(LocalDateTime.now());
        try{
            this.userModelRepositoryr.save(user);
            return this.mapper.map(user, UserResponseDTO.class);
        }catch(Exception exception){
            throw new RuntimeException("Error trying save a new User {} " + exception.getMessage());
        }
    }
}