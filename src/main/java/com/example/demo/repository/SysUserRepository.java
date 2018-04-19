package com.example.demo.repository;

import com.example.demo.domain.SysUser;
import com.example.demo.repository.support.CustomRepository;

public interface SysUserRepository extends CustomRepository<SysUser,Long> {

    SysUser findByUsernameAndPassword(String username, String password);
}
