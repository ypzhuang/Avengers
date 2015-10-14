package models.common;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by ypzhuang on 15/9/23.
 */
@Entity
@Table(name = "t_doctor")
public class Doctor  extends Model {
    @Id
    public Long id;
}
