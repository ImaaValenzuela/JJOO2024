package repositories

import data.User

object UserRepository {

    private val users = mutableListOf<User>()

    init {
        users.add(User(1504L, "bbayarri", "abc123", "Brian", "Bayarri", 3500000.50, "2022/12/10", "admin"))
        users.add(User(2802L, "AHOZ", "lock_password", "Aylen", "Hoz", 200000.50, "2021/01/11", "admin"))
        users.add(User(1510L, "Diegote", "12345", "Diego", "Gonzalez", 12000000.0, "2018/04/15", "admin"))
    }


    fun login(nickname: String, password: String): User? {
        val user = users.find { it.nickName == nickname && it.password == password }
        return user //TODO Implementar solucion para validar e iniciar sesion un usuario
    }


    fun add(user: User) {
        users.add(user)
    }

    fun remove(id: Long) {
        val userToRemove = users.find { it.id == id }
        if (userToRemove != null) {
            users.remove(userToRemove)
            println("User with ID $id has been removed.")
        } else {
            println("User with ID $id not found.")
        }
    }

    fun get(): List<User>{ // Agregamos un get
        return users
    }

    fun findByNickname(nickname: String): User?{ // Buscamos usuarios por nickname
        return users.find { it.nickName == nickname }
    }

    fun findById(id: Long): User?{ // Buscamos usuarios por nickname
        return users.find { it.id == id }
    }


}