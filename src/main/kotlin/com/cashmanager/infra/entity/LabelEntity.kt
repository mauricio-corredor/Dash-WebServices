package com.cashmanager.infra.entity

import com.cashmanager.domain.model.LabelDomain
import com.dash.infra.entity.UserEntity
import javax.persistence.*

@Entity
@Table(name = "label")
data class LabelEntity(
    @Id
    @SequenceGenerator(name = "label-seq-gen", sequenceName = "label_id_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "label-seq-gen")
    @Column(name = "id", unique = true, nullable = false)
    val id: Int,

    @Column(name = "label")
    val label: String = "",

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    val user: UserEntity
) {
    fun toDomain() = LabelDomain(this.id, this.label, this.user.id)
}
