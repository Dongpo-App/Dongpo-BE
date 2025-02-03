package com.dongyang.dongpo.domain.member.entity;

import com.dongyang.dongpo.domain.member.dto.MyTitlesResponseDto;
import com.dongyang.dongpo.domain.member.enums.Title;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_title", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member", "title"})
})
@EntityListeners(AuditingEntityListener.class)
public class MemberTitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    private Member member;

    @Column(columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private Title title;

    @CreatedDate
    private LocalDateTime achieveDate;

    public MyTitlesResponseDto toMyTitlesResponse() {
        return MyTitlesResponseDto.builder()
                .description(this.title.getDescription())
                .achieveCondition(this.title.getAchieveCondition())
                .achieveDate(this.achieveDate)
                .build();
    }
}
