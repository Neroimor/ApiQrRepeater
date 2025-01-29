package com.neroimor.QRrepeater.DAO.Repositorys;

import com.neroimor.QRrepeater.DAO.Model.UsersQR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserQRRepositroy extends JpaRepository<UsersQR, Integer> {
    Optional<UsersQR> findByName(String username);
}
