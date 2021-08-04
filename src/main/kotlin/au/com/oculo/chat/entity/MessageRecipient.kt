package au.com.oculo.chat.entity

import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "message_recipient", schema = "public")
data class MessageRecipient(
    @Id
    @GeneratedValue(generator = "sequence_message_recipient_id")
    @GenericGenerator(
        name = "message-recipient-sequence-generator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = [
            Parameter(name = "sequence_name", value = "message_recipient_id_seq"),
            Parameter(name = "initial_value", value = "1"),
            Parameter(name = "increment_size", value = "1")]
    )
    val id: Long = -1,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message_id")
    val message: Message,
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    val recipient: User,
)