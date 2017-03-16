package edu.nju.hostel.dao;

import edu.nju.hostel.entity.ApproveItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/16
 */
public interface ApproveItemRepository extends JpaRepository<ApproveItem, Integer> {
    List<ApproveItem> findByHotelId(int hotelId);

    List<ApproveItem> findByRoomId(int roomId);

}
