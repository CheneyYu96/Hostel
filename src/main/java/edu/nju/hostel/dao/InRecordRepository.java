package edu.nju.hostel.dao;

import edu.nju.hostel.entity.InRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/11
 */
public interface InRecordRepository extends JpaRepository<InRecord,Integer> {
    List<InRecord> findByHotelId(int hotelId);
}
