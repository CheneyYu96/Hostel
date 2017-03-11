package edu.nju.hostel.entity;

import javax.persistence.*;

/**
 * @author yuminchen
 * @version V1.0
 * @date 2017/3/11
 */
@Entity
@Table(name = "record_name")
public class InRecordName {
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "record_id")
    private int inRecordId;

    private String name;

    @Column(name = "member_id")
    private int memberId;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }
}
