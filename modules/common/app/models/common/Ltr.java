package models.common;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import play.Logger;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_ltr")
public class Ltr  extends Model {

    @Id
    public Long id;

    public Long uid;

    public Long hid;//hospital id

    @Column(name="hosname")
    public String hospital;//医院名称

    public Date testtime;

    public Date createtime;

    public Integer category;

    public Integer finish;

    @Column(name="is_delete")
    public boolean delete;

    public Long failId;//失败识别的原因

    public String forage; //化验单年龄

    public Integer source; //化验单来源

    public Integer state ;  //识别状态

    @Column(name="dis_id")
    public Long disId;  //识别者id

    @Column(name="is_viewed_by_me")
    public boolean isViewedByMe;  //识别成功的单子自己是否查看过

    @Column(name="viewed_doctor_ids")
    public String viewedDoctorIds;  //识别成功的单子,查看过的医生IDs , e.g. '123, 1234, 12345'

    public Integer type; // 化验单类型，0:指数类型 1: 文本类型

    @Enumerated(EnumType.STRING)
    public LtrStatus status; //status new added

    @Column(name="agent_info")
    public String agentInfo;



    public static Finder<Long, Ltr> find = new Finder<>(Long.class, Ltr.class);

    public static List<Ltr> findAll() {

        return  Ltr.find.all();
    }

    public static List<Ltr> search(String filter,int page,int pageSize,Object... status) {
        return getPage(filter, null, page, pageSize, status).getList();
    }

    public static int count(String filter, int page,int pageSize,Object... status) {
        return getPage(filter, null, page, pageSize, status).getTotalRowCount();
    }

    public static List<Ltr> search(String filter,Boolean isException,int page,int pageSize,Object... status) {
        return getPage(filter, isException, page, pageSize, status).getList();
    }

    public static int count(String filter,Boolean isException, int page,int pageSize,Object... status) {
        return getPage(filter, isException, page, pageSize, status).getTotalRowCount();
    }


    private static Page<Ltr> getPage(String filter,Boolean isException,int page,int pageSize,Object... status){
        Logger.debug("search ltr with param [{}],[{}],[{}],[{}]", filter, page, pageSize, status);

        ExpressionList<Ltr> expression = Ltr.find.where();
        if (filter != null && !"".equals(filter)) {
            try {
                Long id = Long.parseLong(filter);
                expression.eq("id", id);
            }catch(NumberFormatException e) {
                Logger.debug("add hospital");
                expression.like("hospital", "%" + filter + "%");
            }
        }
        if (status != null && status.length > 0) {
            expression.in("status", status);
        }


        if (isException != null ) {
            if (isException.booleanValue()) {
                expression.isNotNull("failId");
//            } else {
//                expression.isNull("failId");
            }
        }

        return expression
                .orderBy("id desc")
                .findPagingList(pageSize)
                .setFetchAhead(false)
                .getPage(page);
    }




}
