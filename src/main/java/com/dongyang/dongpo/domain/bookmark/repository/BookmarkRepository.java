package com.dongyang.dongpo.domain.bookmark.repository;


import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.bookmark.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Page<Bookmark> findByMember(final Member member, final Pageable pageable);

    Optional<Bookmark> findByStoreAndMember(final Store store, final Member member);

    @Query("SELECT COUNT(b) > 0 FROM Bookmark b WHERE b.store = :store AND b.member = :member")
    boolean existsByStoreAndMember(final Store store, final Member member);

    Long countByStore(final Store store);
}
