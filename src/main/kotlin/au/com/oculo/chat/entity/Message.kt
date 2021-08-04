package au.com.oculo.chat.entity

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import javax.persistence.*

@Entity
@Table(name = "message", schema = "public")
data class Message(
    @Id
    @GeneratedValue(generator = "sequence_message_id")
    @GenericGenerator(
        name = "message-sequence-generator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = [
            Parameter(name = "sequence_name", value = "message_id_seq"),
            Parameter(name = "initial_value", value = "1"),
            Parameter(name = "increment_size", value = "1")]
    )
    val id: Long = -1,
    val content: String,
    @OneToOne
    @JoinColumn(name = "sender_id")
    val sender: User,
    @OneToMany(mappedBy = "message", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var recipients: List<MessageRecipient> = emptyList(),
)