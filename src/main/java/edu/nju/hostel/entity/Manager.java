package edu.nju.hostel.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 *
 * @author yuminchen
 * @date 2017/2/19
 * @version V1.0
 */
@Entity
@Table(name = "manager")
public class Manager {
    @Id
    @GeneratedValue
    private int id;

    @Column(length = 32)
    private String name;

    @Column(length = 32)
    private String password;

    public Manager() {
    }

    public Manager(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
