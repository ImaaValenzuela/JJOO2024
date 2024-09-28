package data

data class User(
    val id: Long,
    val nickName: String,
    val password: String,
    val name: String,
    val surname: String,
    var money: Double = 200000.0,
    val createdDate: String
)
