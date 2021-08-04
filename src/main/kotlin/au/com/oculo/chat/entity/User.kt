package au.com.oculo.chat.entity

import javax.persistence.*

@Entity
@Table(name = "user", schema = "public")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: String = "",
    val email: String
)