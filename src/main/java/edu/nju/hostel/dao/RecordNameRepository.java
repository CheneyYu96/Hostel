package edu.nju.hostel.dao;

import edu.nju.hostel.entity.InRecordName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/11
 */
public interface RecordNameRepository extends JpaRepository<InRecordName, Integer> {
    List<InRecordName> findByInRecordId(int inRecordId);
}
