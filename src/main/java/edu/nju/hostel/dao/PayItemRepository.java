package edu.nju.hostel.dao;

import edu.nju.hostel.entity.PayItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/16
 */
public interface PayItemRepository extends JpaRepository<PayItem, Integer>{

    List<PayItem> findByInRecordId(int inRecordId);

    List<PayItem> findByOrderId(int orderId);

    List<PayItem> findByHotelId(int hotelId);
}
