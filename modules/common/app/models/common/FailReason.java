package models.common;

/**
 * Created by ypzhuang on 15/9/24.
 */

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "t_fail_reason")
public class FailReason  extends Model {
    @Id
    public Long id;

    @Column(name="desciption",length=500)
    public String description;

    @Column(length=2000)
    public String content;

    public Date createTime;

    @Column(name="is_delete")
    public Boolean isDeleted;

    @Column(name="suggest",length=1000)
    public String suggestion;

    public static Finder<Long, FailReason> find = new Finder<>(Long.class, FailReason.class);

    public static List<FailReason> findAll() {
        return  FailReason.find.where().eq("isDeleted",false).findList();
    }

    public static FailReason findById(Long id) {
        return  FailReason.find.byId(id);
    }

    public  void deleteFailReason() {
        this.update();
    }

    public  void updateFailReason() {
        this.update();
    }
}
