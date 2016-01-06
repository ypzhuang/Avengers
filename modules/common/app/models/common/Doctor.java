package models.common;

import play.data.format.Formats;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by ypzhuang on 15/9/23.
 */
@Entity
@Table(name = "t_doctor")
public class Doctor  extends Model {
    @Id
    public Long id;

    public String doctorName;
    public String nickname;
    public String img;
    public String detail;
    public Integer gender;
    public Integer age;
    public String email;
    public String tel;
    public String gid;
    public Integer isDelete;


    @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date createTime;

}
