package edu.nju.hostel.dao;

import edu.nju.hostel.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author yuminchen
 * @date 2017/2/24
 * @version V1.0
 */
public interface ManagerRepository extends JpaRepository<Manager,Integer> {

}
