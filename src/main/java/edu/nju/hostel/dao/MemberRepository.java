package edu.nju.hostel.dao;

import edu.nju.hostel.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by yuminchen on 2017/3/3.
 */
public interface MemberRepository extends JpaRepository<Member,Integer> {

    List<Member> findByName(String name);
}
