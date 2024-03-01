package com.microservices.app.service.impl;

import com.microservices.app.entity.UserPermission;
import com.microservices.app.repository.UserPermissionRepository;
import com.microservices.app.service.QueryUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwitterQueryUserService implements QueryUserService {

    private final UserPermissionRepository userPermissionRepository;

    @Override
    public Optional<List<UserPermission>> findAllPermissionsByUsername(String username) {
        log.info("Finding permissions by username {}", username);
        return userPermissionRepository.findPermissionsByUsername(username);
    }
}
