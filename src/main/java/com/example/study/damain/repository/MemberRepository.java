package com.example.study.damain.repository;

import com.example.study.damain.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = {"authorities"})
    Optional<Member> findByEmail(String email);

    @Override
    @EntityGraph(attributePaths = {"authorities"})
    Optional<Member> findById(Long aLong);
}
