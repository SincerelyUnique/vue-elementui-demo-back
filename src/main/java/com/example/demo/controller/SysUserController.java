package com.example.demo.controller;

import com.example.demo.dto.MessageType;
import com.example.demo.dto.Msg;
import com.example.demo.domain.SysUser;
import com.example.demo.repository.SysUserRepository;
import com.example.demo.utils.ExampleMatcherUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/sysUser")
@CrossOrigin("*")
public class SysUserController {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<Msg<SysUser>> get(@PathVariable Long id) {
        SysUser foundEmployee = sysUserRepository.findById(id).orElse(null);
        Msg<SysUser> msg = new Msg<>(MessageType.MSG_TYPE_SUCCESS, null, foundEmployee);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Msg<Page<SysUser>>> list(Pageable pageable) {
        Page<SysUser> employees = sysUserRepository.findAll((PageRequest)pageable);
        Msg<Page<SysUser>> msg = new Msg<>(MessageType.MSG_TYPE_SUCCESS, null, employees);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Msg<SysUser>> save(@Valid @RequestBody SysUser sysUser) {
        SysUser savedEmployee = sysUserRepository.save(sysUser);
        Msg<SysUser> msg = new Msg<>(MessageType.MSG_TYPE_SUCCESS, null, savedEmployee);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Msg<SysUser>> update(@PathVariable Long id, @Valid @RequestBody SysUser employee) {
        SysUser employeeForUpdate = sysUserRepository.findById(id).orElse(null);
        modelMapper.map(employee,employeeForUpdate);
        SysUser updatedEmployee = sysUserRepository.save(employeeForUpdate);
        Msg<SysUser> msg = new Msg<>(MessageType.MSG_TYPE_SUCCESS, null, updatedEmployee);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping("/query")
    public ResponseEntity<Msg<Page<SysUser>>> queryByNameAndPhoneNum( @RequestParam(value = "name",required = false) String name,
                                                                       @RequestParam(value = "phoneNum",required = false) String phoneNum,
                                                                       Pageable pageable) {
        Example<SysUser> example = Example.of(new SysUser(name,phoneNum), ExampleMatcherUtils.generateStringContaningAndNullIgnoreMatcher());
        Page<SysUser> employees = sysUserRepository.findAll(example,(PageRequest)pageable);
        Msg<Page<SysUser>> msg = new Msg<>(MessageType.MSG_TYPE_SUCCESS, null, employees);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

}
