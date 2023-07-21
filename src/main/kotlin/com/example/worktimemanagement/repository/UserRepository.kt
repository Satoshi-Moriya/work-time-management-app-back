package com.example.worktimemanagement.repository

import com.example.worktimemanagement.entity.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Int> {

}