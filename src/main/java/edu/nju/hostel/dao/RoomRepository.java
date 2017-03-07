package edu.nju.hostel.dao;

import edu.nju.hostel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/7
 */
public interface RoomRepository extends JpaRepository<Room, Integer> {
    @Query(value = "from room r where r.hot_id = ?1")
    List<Room> findByHotel(int hotelId);

    @Query(value = "from room r where r.hot_id = ?1 and r.number = ?2")
    Room findByHotelAndNumber(int hotelId, String roomNumber);
}
