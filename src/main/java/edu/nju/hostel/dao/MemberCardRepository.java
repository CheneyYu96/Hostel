package edu.nju.hostel.dao;

import edu.nju.hostel.entity.MemberCard;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author yuminchen
 * @date 2017/3/5
 * @version V1.0
 */
public interface MemberCardRepository extends JpaRepository<MemberCard, Integer> {
}
