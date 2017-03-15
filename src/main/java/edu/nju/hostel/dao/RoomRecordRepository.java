package edu.nju.hostel.dao;

import edu.nju.hostel.entity.RoomRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/14
 */
public interface RoomRecordRepository extends JpaRepository<RoomRecord,Integer>{
    List<RoomRecord> findByRoomId(int roomId);

    RoomRecord findByOrderId(int orderId);
}
