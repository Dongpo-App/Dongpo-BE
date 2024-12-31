package com.dongyang.dongpo.domain.bookmark.repository;


import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.bookmark.entity.StoreBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<StoreBookmark, Long> {
    List<StoreBookmark> findByMemberId(Long id);

    List<StoreBookmark> findByMember(Member member);

    Optional<StoreBookmark> findByStoreAndMember(Store store, Member member);

    boolean existsByStoreAndMember(Store store, Member member);

    Long countByStore(Store store);
}
