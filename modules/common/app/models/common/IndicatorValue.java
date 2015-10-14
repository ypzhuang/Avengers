package models.common;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import play.Logger;
import play.db.ebean.Model;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by ypzhuang on 15/9/16.
 */

@Entity
@Table(name = "t_indicator_value")
public class IndicatorValue extends Model {
    @Id
    public Long id;

    //public Long iid;//indicatior id
    @ManyToOne
    @JoinColumn(name="iid")
    public Indicator indicator;

    public Integer category;//指标类别

    public String name;//用户指标实际名称

    public Long cid;//病例id


    public Double value;

    public Integer state;//high, low, normal

    public Integer type ;  //用于区分是否为特殊指标（非数值类的）
    public Double upperlimit;
    public Double lowerlimit;


    public Integer direction; //< = >

    public Integer calmethod; //运算方式


    public Long uid;  //用户id

    public Date testtime; //指标检验时间

    @ManyToOne
    @JoinColumn(name="ltr_id")
    public Ltr ltr;

    public Integer isDelete; //1 删除

    public Double standardValue;//换算后的标准值



    public static Finder<Long, IndicatorValue> find = new Finder<>(Long.class, IndicatorValue.class);

    public static List<IndicatorValue> findAllByLtrId(Long ltrId){
        Logger.debug("search indciator value with param [{}]", ltrId);
        return IndicatorValue.find.where()
                .eq("ltr.id",ltrId)
                .eq("isDelete",0)
                .orderBy("id asc").findList();
    }

}
