package models.common;

import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import play.Logger;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Date;
import java.util.List;


/**
 * Created by ypzhuang on 15/9/16.
 */

@Entity
@Table(name = "t_indicator")
public class Indicator extends Model{
    @Id
    public Long id;

    public String showname;

    public String code;

    @Column(length=1000)
    public String description;

    public Date createtime;

    public Integer category;
    //0 血常规
    //1 肝功能
    //2 甲状腺
    //3 病毒两对半
    //5 肾功能
    //4 其他

    public String unit;

    public Integer type ;  //用于区分指标类型 1:阴阳 0：数值 (port from IndicatorValueEntity)

    public boolean isDoctorFilter ;  //是否医生筛选动态的指标

    public boolean logInSlider;  //filter是否是对数函数(log)显示

    public Long upperlimit; //filter上限

    public Long lowerlimit; //filter下限

    @Override
    public String toString() {
        return "Indicator{" +
                "id=" + id +
                ", showname='" + showname + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", createtime=" + createtime +
                ", category=" + category +
                ", unit='" + unit + '\'' +
                ", type=" + type +
                ", isDoctorFilter=" + isDoctorFilter +
                ", logInSlider=" + logInSlider +
                ", upperlimit=" + upperlimit +
                ", lowerlimit=" + lowerlimit +
                '}';
    }

    public static Finder<Long, Indicator> find = new Finder<>(Long.class, Indicator.class);


    public static List<Indicator> filterBy(String name){
        Logger.debug("search indciator  with param [{}]", name);
        return Indicator.find.where()
                .or(
                        Expr.ilike("showname", "%" + name + "%"),
                        Expr.ilike("code", "%" + name + "%")
                )
                .findList();
    }

    public static Indicator findById(Long id){
        Logger.debug("find indciator  with param [{}]", id);
        return Indicator.find.byId(id);

    }


    public static List<Indicator> search(String filter,int category, int page,int pageSize) {
        return getPage(filter,category, page, pageSize).getList();
    }

    public static int count(String filter,int category, int page,int pageSize) {
        return getPage(filter, category, page, pageSize).getTotalRowCount();
    }


    private static Page<Indicator> getPage(String filter,int category,int page,int pageSize){
        Logger.debug("search indicator with param [{}],[{}],[{}],[{}]", filter, category, page, pageSize);

        ExpressionList<Indicator> expression = Indicator.find.where();
        if (filter != null && !"".equals(filter)) {
            expression
                    .or(
                            Expr.ilike("showname", "%" + filter + "%"),
                            Expr.ilike("code", "%" + filter + "%")
                    );
        }

        if (category != -1) {
            expression.eq("category",category);
        }


        return expression
                .findPagingList(pageSize)
                .setFetchAhead(false)
                .getPage(page);
    }



    public static List<Indicator> findAll(){
       return Indicator.find.all();
    }


}
