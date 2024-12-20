package br.com.cadastroit.services.entity.user.adapter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.cadastroit.services.entity.user.dto.UserResponseDTO;
import br.com.cadastroit.services.entity.user.port.FindOnePort;
import br.com.cadastroit.services.entity.user.repository.UserModelRepository;

@Component
public class FindOneSpringDataAdapter implements FindOnePort<Long, UserResponseDTO> {

	private final ModelMapper mapper;

	private final UserModelRepository userModelRepositoryr;

	public FindOneSpringDataAdapter(ModelMapper mapper, UserModelRepository userModelRepositoryr) {
		this.mapper = mapper;
		this.userModelRepositoryr = userModelRepositoryr;
	}

	@Override
	public UserResponseDTO execute(Long in) {
		return mapper.map(userModelRepositoryr.findById(in).orElseThrow(), UserResponseDTO.class);
	}

}