package edu.nju.hostel.dao;

import edu.nju.hostel.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by yuminchen on 2017/3/5.
 */
public interface OrderRepository extends JpaRepository<Order,Integer> {
    List<Order> findByHotelId(int hotelId);
}
