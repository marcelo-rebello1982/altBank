package br.com.cadastroit.services.entity.user.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.cadastroit.services.entity.user.model.UserModel;

public interface UserModelRepository extends JpaRepository<UserModel, Long>, JpaSpecificationExecutor<UserModel> {
    UserDetails findByUserName(String userName);
}