package main.services

import data.User
import main.*
import repositories.UserRepository

object UserService {
    fun listUsers() {
        val users = UserRepository.get()
        if (users.isEmpty()) {
            println("No users available.")
            return
        }

        users.forEach { user ->
            println("""
                | UID          : ${user.id}
                | NickName     : ${user.nickName}
                | Money        : $${user.money}
                | Role         : ${user.rol}
                | Created Date : ${user.createdDate}
            """.trimMargin())
        }
    }
}