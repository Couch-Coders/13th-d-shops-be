package com.example.demo.service;

import com.example.demo.consts.UserActiveStatus;
import com.example.demo.dto.CompanyDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Company;
import com.example.demo.entity.User;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Slf4j
@Service
@AllArgsConstructor
//@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public User getUser(String email) {
        return userRepository.findByEmail(email)//.orElseThrow()
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER, "존재하지 않는 유저입니다."));
    }

    public User getActiveUser(String email) {
        User user = getUser(email);
        if (!user.isActiveUser())
            throw new CustomException(ErrorCode.NOT_CORRECT_USER, "비활성화 유저입니다. 다시 로그인 해주세요");
        return user;
    }

    public User addUser(String email) {
        User user = User.builder()
                .email(email)
                .company(null)
                .userActiveStatus(UserActiveStatus.ACTIVE)
                .build();
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String email) {
        User user = getUser(email);
        user.deactivateUser();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public void activateUser(User user) {
        user.activateUser();
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER, "존재하지 않는 유저입니다."));
    }

    @Transactional // 트랜젝셕
    public User modifyUser(UserDTO userDTO) {
//        if (userDTO.getName() == null || userDTO.getName().equals(""))
        if (userDTO.getUserEntryNo() == null || userDTO.getName().equals(""))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자 id가 비었습니다.");

        Optional<User> userOptional = userRepository.findById(userDTO.getUserEntryNo());
        if (!userOptional.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자가 존재하지 않습니다.");
        log.info("userOptional: "+userOptional);

        Company existingCompany = userOptional.get().getCompany();
        Company updatedCompany = userDTO.getCompany();

        User existingUser = userOptional.get();

        if (existingCompany == null && updatedCompany != null) {
            log.info("Enter1");
            // 없으면 받은거 저장
            //updatedCompany.setUser(userOptional);
//            updatedCompany.setUser(updatedCompany.getUser());
//            userDTO.setCompany(updatedCompany);
        }
        else if (existingCompany != null && updatedCompany != null) {
            // 기존것이 있으면 수정해라
            log.info("Enter2");
            //입력이 없으면 수정 안하게
            if(updatedCompany.getName() != null)
                existingCompany.setName(updatedCompany.getName());
            if(updatedCompany.getEmail() != null)
                existingCompany.setEmail(updatedCompany.getEmail());
            if(updatedCompany.getPhone() != null)
                existingCompany.setPhone(updatedCompany.getPhone());

            userDTO.setCompany(existingCompany);
        }

        // 넘어온것만 수정할 수 있을까?
        // 성공
        // 조금더 쉬운 방법 없을까?
        if(userDTO.getName() == null)
            userDTO.setName(existingUser.getName());
        if(userDTO.getPhone() == null)
            userDTO.setPhone(existingUser.getPhone());

        User user = User.builder()
                .userEntryNo(userOptional.get().getUserEntryNo())
                .email(userOptional.get().getEmail())
                .registeredDate(userOptional.get().getRegisteredDate())
                .userActiveStatus(userOptional.get().getUserActiveStatus())
                .name(userDTO.getName())
                .phone(userDTO.getPhone())
                .company(userDTO.getCompany())
                .build();

        return userRepository.save(user);
    }
}