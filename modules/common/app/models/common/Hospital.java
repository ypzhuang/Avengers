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
import java.util.List;

/**
 * Created by ypzhuang on 15/9/16.
 */

@Entity
@Table(name = "t_hospital")
public class Hospital extends Model {
    @Id
    public Long id;

    public String lat;

    public String lon;

    public String name;

    @Column(length=500)
    public String address;

    public String location;

    public static Finder<Long, Hospital> find = new Finder<>(Long.class, Hospital.class);



    public static List<Hospital> search(String filter,int page,int pageSize) {
        return getPage(filter, page, pageSize).getList();
    }

    public static int count(String filter,int page,int pageSize) {
        return getPage(filter, page, pageSize).getTotalRowCount();
    }


    private static Page<Hospital> getPage(String filter,int page,int pageSize){
        Logger.debug("search hospital with param [{}],[{}],[{}]", filter, page, pageSize);

        ExpressionList<Hospital> expression = Hospital.find.where();
        if (filter != null && !"".equals(filter)) {
            expression
            .or(
                Expr.like("name", "%" + filter + "%"),
                Expr.like("address", "%" + filter + "%")
            );
        }

        return expression
                .findPagingList(pageSize)
                .setFetchAhead(false)
                .getPage(page);
    }


}
