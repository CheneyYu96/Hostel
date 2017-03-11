package edu.nju.hostel.entity;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/11
 */
@Entity
@Table(name = "out_record")
public class OutRecord {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "in_record_id")
    private int inRecordId;

    private LocalDate date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInRecordId() {
        return inRecordId;
    }

    public void setInRecordId(int inRecordId) {
        this.inRecordId = inRecordId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
