package edu.nju.hostel.dao;

import edu.nju.hostel.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author yuminchen
 * @date 2017/3/5
 * @version V1.0
 */
public interface HotelRepository extends JpaRepository<Hotel, Integer>{
}
