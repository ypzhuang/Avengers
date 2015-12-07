package models.common;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by ypzhuang on 15/12/7.
 */
@Entity
@Table(name = "t_user_n_machine")
public class Device extends Model {

    @Id
    public Long id;

    public  Long uid;

    public String machineid;

    @Column(name="create_time")
    public Date createTime;

    @Column(name="client_type")
    public String clientType; //0: iOS, 1: Android

    public static Finder<Long, Device> find = new Finder<>(Long.class, Device.class);

    public static List<Device> findAllByUserId(Long uid) {
        return Device.find.where().eq("uid", uid).findList();
    }

}
