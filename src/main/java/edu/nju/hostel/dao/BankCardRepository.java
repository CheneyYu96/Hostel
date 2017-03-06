package edu.nju.hostel.dao;

import edu.nju.hostel.entity.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 * @author yuminchen
 * @date 2017/3/5
 * @version V1.0
 */
public interface BankCardRepository extends JpaRepository<BankCard, Integer> {

    List<BankCard> findByCardId(String cardId);
}
